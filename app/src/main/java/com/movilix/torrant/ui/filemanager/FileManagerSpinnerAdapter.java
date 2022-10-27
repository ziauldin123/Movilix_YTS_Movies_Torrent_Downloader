

package com.movilix.torrant.ui.filemanager;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movilix.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/*
 * The adapter for filemanager storage spinner.
 */

public class FileManagerSpinnerAdapter extends BaseAdapter
{
    private static final String TAG = FileManagerSpinnerAdapter.class.getSimpleName();

    private static final String DROPDOWN = "dropdown";
    private static final String NON_DROPDOWN = "non_dropdown";

    private List<StorageSpinnerItem> items = new ArrayList<>();
    private String curPath;
    private Context context;

    public static class StorageSpinnerItem
    {
        private String name;
        private String storagePath;
        private long size;

        public StorageSpinnerItem(String name, String storagePath, long size)
        {
            this.name = name;
            this.storagePath = storagePath;
            this.size = size;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getStoragePath()
        {
            return storagePath;
        }

        public void setStoragePath(String storagePath)
        {
            this.storagePath = storagePath;
        }

        public long getSize()
        {
            return size;
        }

        public void setSize(long size)
        {
            this.size = size;
        }
    }

    public FileManagerSpinnerAdapter(Context context)
    {
        this.context = context;
    }

    public void clear()
    {
        items.clear();
    }

    public void addItem(StorageSpinnerItem item)
    {
        items.add(item);
    }

    public void addItems(List<StorageSpinnerItem> items)
    {
        this.items.addAll(items);
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public String getItem(int position)
    {
        return items.get(position).getStoragePath();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getDropDownView(int position, View view, ViewGroup parent)
    {
        if (view == null || !view.getTag().toString().equals(DROPDOWN)) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.filemanager_storage_spinner_item_dropdown, parent, false);
            view.setTag(DROPDOWN);
        }

        String nameAndSizeTemplate = context.getString(R.string.storage_name_and_size);
        TextView nameAndSize = view.findViewById(R.id.storage_name_and_size);
        TextView path = view.findViewById(R.id.storage_path);

        StorageSpinnerItem item = getStorageItem(position);
        if (item != null) {
            nameAndSize.setText(String.format(nameAndSizeTemplate, item.name,
                                Formatter.formatFileSize(context, item.size)));
            path.setText(item.getStoragePath());
        }

        return view;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null || !view.getTag().toString().equals(NON_DROPDOWN)) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.filemanager_storage_spinner_item, parent, false);
            view.setTag(NON_DROPDOWN);
        }

        TextView title = view.findViewById(R.id.storage_title);
        title.setText(getTitle());

        return view;
    }

    @Nullable
    public StorageSpinnerItem getStorageItem(int position)
    {
        return (position >= 0 && position < items.size()) ? items.get(position) : null;
    }

    public String getTitle()
    {
        return (curPath != null ? curPath : "");
    }

    public void setTitle(String curPath)
    {
        this.curPath = curPath;
        notifyDataSetChanged();
    }
}