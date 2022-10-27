

package com.movilix.torrant.ui.main.drawer;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.databinding.DrawerEmptyTagsListItemBinding;
import com.movilix.databinding.DrawerNoTagsListItemBinding;
import com.movilix.databinding.DrawerTagsListItemBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends ListAdapter<AbstractTagItem, TagsAdapter.AbstractViewHolder> {
    private static final int TYPE_EMPTY_ITEM = 0;
    private static final int TYPE_TAG_ITEM = 1;
    private static final int TYPE_NO_TAGS_ITEM = 2;

    private AbstractTagItem selectedItem;

    @NonNull
    private final OnClickListener listener;

    public TagsAdapter(@NonNull OnClickListener listener) {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_EMPTY_ITEM: {
                DrawerEmptyTagsListItemBinding binding = DataBindingUtil.inflate(
                        inflater,
                        R.layout.drawer_empty_tags_list_item,
                        parent,
                        false
                );
                return new EmptyItemViewHolder(binding);
            }
            case TYPE_TAG_ITEM: {
                DrawerTagsListItemBinding binding = DataBindingUtil.inflate(
                        inflater,
                        R.layout.drawer_tags_list_item,
                        parent,
                        false
                );
                return new ItemViewHolder(binding);
            }
            case TYPE_NO_TAGS_ITEM: {
                DrawerNoTagsListItemBinding binding = DataBindingUtil.inflate(
                        inflater,
                        R.layout.drawer_no_tags_list_item,
                        parent,
                        false
                );
                return new NoTagsItemViewHolder(binding);
            }
        }

        throw new IllegalStateException("Unknown item type: " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        AbstractTagItem item = getItem(position);
        if (item != null) {
            holder.bind(item, listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        AbstractTagItem item = getItem(position);
        if (item instanceof com.movilix.torrant.ui.main.drawer.TagItem) {
            return TYPE_TAG_ITEM;
        } else if (item instanceof com.movilix.torrant.ui.main.drawer.EmptyTagItem) {
            return TYPE_EMPTY_ITEM;
        } else if (item instanceof com.movilix.torrant.ui.main.drawer.NoTagsItem) {
            return TYPE_NO_TAGS_ITEM;
        }

        throw new IllegalStateException("Unknown item: " + item);
    }

    public void setSelectedItem(@NonNull AbstractTagItem item) {
        selectedItem = item;
        int position = getCurrentList().indexOf(item);
        if (position == -1) {
            return;
        }
        notifyDataSetChanged();
    }

    public AbstractTagItem getSelectedItem() {
        return selectedItem;
    }

    static final DiffUtil.ItemCallback<AbstractTagItem> diffCallback =
            new DiffUtil.ItemCallback<AbstractTagItem>() {
                @Override
                public boolean areContentsTheSame(
                        @NonNull AbstractTagItem oldItem,
                        @NonNull AbstractTagItem newItem
                ) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(
                        @NonNull AbstractTagItem oldItem,
                        @NonNull AbstractTagItem newItem
                ) {
                    return oldItem.isSame(newItem);
                }
            };

    public interface OnClickListener {
        void onTagSelected(@NonNull AbstractTagItem item);

        void onTagMenuClicked(@NonNull AbstractTagItem item, int menuId);
    }

    static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        public AbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bind(
                @NonNull AbstractTagItem item,
                OnClickListener listener
        );
    }

    class ItemViewHolder extends AbstractViewHolder {
        @NonNull
        private final DrawerTagsListItemBinding binding;

        public ItemViewHolder(@NonNull DrawerTagsListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        @Override
        void bind(
                @NonNull AbstractTagItem item,
                OnClickListener listener
        ) {
            if (item instanceof com.movilix.torrant.ui.main.drawer.TagItem) {
                bind((com.movilix.torrant.ui.main.drawer.TagItem) item, listener);
            }
        }

        private void bind(
                @NonNull com.movilix.torrant.ui.main.drawer.TagItem item,
                OnClickListener listener
        ) {
            binding.name.setText(item.info.name);
            binding.color.setColor(item.info.color);
            binding.getRoot().setOnClickListener((v) -> {
                setSelectedItem(item);
                if (listener != null) {
                    listener.onTagSelected(item);
                }
            });
            binding.menu.setOnClickListener((v) -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.tag_item_popup);
                popup.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    if (listener != null) {
                        listener.onTagMenuClicked(item, menuItem.getItemId());
                    }
                    return true;
                });
                popup.show();
            });

            TypedArray a = itemView.getContext().obtainStyledAttributes(
                    new TypedValue().data,
                    new int[]{
                            R.attr.selectableDrawer,
                            R.attr.dialogRectRipple
                    });
            int colorIdx = item.isSame(selectedItem) ? 0 : 1;
            Drawable d = a.getDrawable(colorIdx);
            if (d != null) {
                itemView.setBackground(d);
            }
            a.recycle();
        }
    }

    class EmptyItemViewHolder extends AbstractViewHolder {
        @NonNull
        private final DrawerEmptyTagsListItemBinding binding;

        public EmptyItemViewHolder(@NonNull DrawerEmptyTagsListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        @Override
        void bind(
                @NonNull AbstractTagItem item,
                OnClickListener listener
        ) {
            if (item instanceof com.movilix.torrant.ui.main.drawer.EmptyTagItem) {
                bind((com.movilix.torrant.ui.main.drawer.EmptyTagItem) item, listener);
            }
        }

        private void bind(
                @NonNull com.movilix.torrant.ui.main.drawer.EmptyTagItem item,
                OnClickListener listener
        ) {
            binding.getRoot().setOnClickListener((v) -> {
                setSelectedItem(item);
                if (listener != null) {
                    listener.onTagSelected(item);
                }
            });

            TypedArray a = itemView.getContext().obtainStyledAttributes(
                    new TypedValue().data,
                    new int[]{
                            R.attr.selectableDrawer,
                            R.attr.dialogRectRipple
                    });
            int colorIdx = item.isSame(selectedItem) ? 0 : 1;
            Drawable d = a.getDrawable(colorIdx);
            if (d != null) {
                itemView.setBackground(d);
            }
            a.recycle();
        }
    }

    class NoTagsItemViewHolder extends AbstractViewHolder {
        @NonNull
        private final DrawerNoTagsListItemBinding binding;

        public NoTagsItemViewHolder(@NonNull DrawerNoTagsListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        @Override
        void bind(
                @NonNull AbstractTagItem item,
                OnClickListener listener
        ) {
            if (item instanceof com.movilix.torrant.ui.main.drawer.NoTagsItem) {
                bind((com.movilix.torrant.ui.main.drawer.NoTagsItem) item, listener);
            }
        }

        private void bind(
                @NonNull com.movilix.torrant.ui.main.drawer.NoTagsItem item,
                OnClickListener listener
        ) {
            binding.getRoot().setOnClickListener((v) -> {
                setSelectedItem(item);
                if (listener != null) {
                    listener.onTagSelected(item);
                }
            });

            TypedArray a = itemView.getContext().obtainStyledAttributes(
                    new TypedValue().data,
                    new int[]{
                            R.attr.selectableDrawer,
                            R.attr.dialogRectRipple
                    });
            int colorIdx = item.isSame(selectedItem) ? 0 : 1;
            Drawable d = a.getDrawable(colorIdx);
            if (d != null) {
                itemView.setBackground(d);
            }
            a.recycle();
        }
    }
}
