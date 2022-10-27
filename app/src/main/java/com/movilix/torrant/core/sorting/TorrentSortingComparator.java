

package com.movilix.torrant.core.sorting;

import com.movilix.torrant.ui.main.TorrentListItem;

import java.util.Comparator;

public class TorrentSortingComparator implements Comparator<TorrentListItem>
{
    private TorrentSorting sorting;

    public TorrentSortingComparator(TorrentSorting sorting)
    {
        this.sorting = sorting;
    }

    public TorrentSorting getSorting()
    {
        return sorting;
    }

    @Override
    public int compare(TorrentListItem state1, TorrentListItem state2)
    {
        return TorrentSorting.SortingColumns.fromValue(sorting.getColumnName())
                .compare(state1, state2, sorting.getDirection());
    }
}
