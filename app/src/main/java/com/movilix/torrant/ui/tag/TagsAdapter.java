

package com.movilix.torrant.ui.tag;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.databinding.TagsListItemBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TagsAdapter extends ListAdapter<TagItem, TagsAdapter.ViewHolder> {
    @NonNull
    private final OnClickListener listener;

    public TagsAdapter(@NonNull OnClickListener listener) {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TagsListItemBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.tags_list_item,
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TagItem item = getItem(position);
        if (item != null) {
            holder.bind(item, listener);
        }
    }

    static final DiffUtil.ItemCallback<TagItem> diffCallback =
            new DiffUtil.ItemCallback<TagItem>() {
                @Override
                public boolean areContentsTheSame(
                        @NonNull TagItem oldItem,
                        @NonNull TagItem newItem
                ) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(
                        @NonNull TagItem oldItem,
                        @NonNull TagItem newItem
                ) {
                    return oldItem.isSame(newItem);
                }
            };

    public interface OnClickListener {
        void onTagClicked(@NonNull TagItem item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final TagsListItemBinding binding;

        public ViewHolder(@NonNull TagsListItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(
                @NonNull TagItem item,
                OnClickListener listener
        ) {
            binding.name.setText(item.info.name);
            binding.color.setColor(item.info.color);
            binding.getRoot().setOnClickListener((v) -> {
                if (listener != null) {
                    listener.onTagClicked(item);
                }
            });
        }
    }
}
