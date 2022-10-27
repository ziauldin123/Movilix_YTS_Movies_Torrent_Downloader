

package com.movilix.torrant.core.sorting;

import com.movilix.torrant.ui.main.TorrentListItem;

public class TorrentSorting extends com.movilix.torrant.core.sorting.BaseSorting
{
    public enum SortingColumns implements SortingColumnsInterface<TorrentListItem>
    {
        none {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                return 0;
            }
        },
        name {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return item1.name.compareTo(item2.name);
                else
                    return item2.name.compareTo(item1.name);
            }
        },
        size {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return Long.compare(item1.totalBytes, item2.totalBytes);
                else
                    return Long.compare(item2.totalBytes, item1.totalBytes);
            }
        },
        progress {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return Integer.compare(item1.progress, item2.progress);
                else
                    return Integer.compare(item2.progress, item1.progress);
            }
        },
        ETA {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return Long.compare(item1.ETA, item2.ETA);
                else
                    return Long.compare(item2.ETA, item1.ETA);
            }
        },
        peers {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return Integer.compare(item1.peers, item2.peers);
                else
                    return Integer.compare(item2.peers, item1.peers);
            }
        },
        dateAdded {
            @Override
            public int compare(TorrentListItem item1, TorrentListItem item2,
                               Direction direction)
            {
                if (direction == Direction.ASC)
                    return Long.compare(item1.dateAdded, item2.dateAdded);
                else
                    return Long.compare(item2.dateAdded, item1.dateAdded);
            }
        };

        public static SortingColumns fromValue(String value)
        {
            for (SortingColumns column : SortingColumns.class.getEnumConstants())
                if (column.toString().equalsIgnoreCase(value))
                    return column;

            return SortingColumns.none;
        }
    }

    public TorrentSorting(SortingColumns columnName, Direction direction)
    {
        super(columnName.name(), direction);
    }
}
