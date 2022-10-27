

package com.movilix.torrant.ui.feeditems;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.torrant.core.utils.Utils;
import com.movilix.databinding.ItemFeedItemsListBinding;
import com.movilix.torrant.ui.Selectable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class FeedItemsAdapter extends ListAdapter<com.movilix.torrant.ui.feeditems.FeedItemsListItem, FeedItemsAdapter.ViewHolder>
        implements Selectable<com.movilix.torrant.ui.feeditems.FeedItemsListItem>
{
    private static final String TAG = FeedItemsAdapter.class.getSimpleName();

    private ClickListener listener;

    public FeedItemsAdapter(ClickListener listener)
    {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFeedItemsListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_feed_items_list,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bind(getItem(position), listener);
    }

    @Override
    public void submitList(@Nullable List<com.movilix.torrant.ui.feeditems.FeedItemsListItem> list)
    {
        if (list != null)
            Collections.sort(list);

        super.submitList(list);
    }

    @Override
    public com.movilix.torrant.ui.feeditems.FeedItemsListItem getItemKey(int position)
    {
        if (position < 0 || position >= getCurrentList().size())
            return null;

        return getItem(position);
    }

    @Override
    public int getItemPosition(com.movilix.torrant.ui.feeditems.FeedItemsListItem key)
    {
        return getCurrentList().indexOf(key);
    }

    private static final DiffUtil.ItemCallback<com.movilix.torrant.ui.feeditems.FeedItemsListItem> diffCallback = new DiffUtil.ItemCallback<com.movilix.torrant.ui.feeditems.FeedItemsListItem>()
    {
        @Override
        public boolean areContentsTheSame(@NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem oldItem,
                                          @NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem newItem)
        {
            return oldItem.equalsContent(newItem);
        }

        @Override
        public boolean areItemsTheSame(@NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem oldItem,
                                       @NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem newItem)
        {
            return oldItem.equals(newItem);
        }
    };

    public interface ClickListener
    {
        void onItemClicked(@NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem item);

        void onItemMenuClicked(int menuId, @NonNull com.movilix.torrant.ui.feeditems.FeedItemsListItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ItemFeedItemsListBinding binding;

        public ViewHolder(ItemFeedItemsListBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(com.movilix.torrant.ui.feeditems.FeedItemsListItem item, ClickListener listener)
        {
            Context context = itemView.getContext();

            binding.menu.setOnClickListener((v) -> {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.inflate(R.menu.feed_item_popup);

                Menu menu = popup.getMenu();
                MenuItem markAsRead = menu.findItem(R.id.mark_as_read_menu);
                MenuItem markAsUnread = menu.findItem(R.id.mark_as_unread_menu);
                if (markAsRead != null)
                    markAsRead.setVisible(!item.read);
                if (markAsUnread != null)
                    markAsUnread.setVisible(item.read);

                popup.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    if (listener != null)
                        listener.onItemMenuClicked(menuItem.getItemId(), item);
                    return true;
                });
                popup.show();
            });

            itemView.setOnClickListener((v) -> {
                if (listener != null)
                    listener.onItemClicked(item);
            });

            int styleAttr;
            if (item.read)
                styleAttr = android.R.attr.textColorSecondary;
            else
                styleAttr = android.R.attr.textColorPrimary;
            TypedArray a = context.obtainStyledAttributes(new TypedValue().data, new int[]{ styleAttr });
            binding.title.setTextColor(a.getColor(0, 0));
            a.recycle();
            Utils.setTextViewStyle(context, binding.title, (item.read ? R.style.normalText : R.style.boldText));
            binding.title.setText(item.title);

            binding.pubDate.setText(SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(new Date(item.pubDate)));
        }
    }
}
