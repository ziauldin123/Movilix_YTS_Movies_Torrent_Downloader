

package com.movilix.torrant.ui.log;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.movilix.R;
import com.movilix.torrant.core.logger.LogEntry;
import com.movilix.databinding.ItemLogListBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

class LogAdapter extends PagedListAdapter<LogEntry, LogAdapter.ViewHolder>
{
    private ClickListener listener;

    LogAdapter(ClickListener listener)
    {
        super(diffCallback);

        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLogListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_log_list,
                parent,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        LogEntry entry = getItem(position);
        if (entry != null)
            holder.bind(entry, listener);
    }

    private static final DiffUtil.ItemCallback<LogEntry> diffCallback = new DiffUtil.ItemCallback<LogEntry>()
    {
        @Override
        public boolean areContentsTheSame(@NonNull LogEntry oldItem,
                                          @NonNull LogEntry newItem)
        {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(@NonNull LogEntry oldItem,
                                       @NonNull LogEntry newItem)
        {
            return oldItem.getId() == newItem.getId();
        }
    };

    public interface ClickListener
    {
        void onItemClicked(@NonNull LogEntry entry);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ItemLogListBinding binding;

        ViewHolder(ItemLogListBinding binding)
        {
            super(binding.getRoot());

            this.binding = binding;
        }

        void bind(@NonNull LogEntry entry, ClickListener listener)
        {
            itemView.setOnClickListener((v) -> {
                if (listener != null)
                    listener.onItemClicked(entry);
            });

            binding.tag.setText(entry.getTag());
            binding.msg.setText(entry.getMsg());
            binding.timeStamp.setText(entry.getTimeStampAsString());
        }
    }
}
