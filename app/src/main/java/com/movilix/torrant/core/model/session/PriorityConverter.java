

package com.movilix.torrant.core.model.session;

import android.util.SparseArray;

import com.movilix.torrant.core.model.data.Priority;

import androidx.annotation.NonNull;

class PriorityConverter
{
    private static final SparseArray<org.libtorrent4j.Priority> PRIOR_TO_LIB_PRIOR = new SparseArray<>();
    static {
        PRIOR_TO_LIB_PRIOR.put(Priority.IGNORE.value(), org.libtorrent4j.Priority.IGNORE);
        PRIOR_TO_LIB_PRIOR.put(Priority.LOW.value(), org.libtorrent4j.Priority.LOW);
        PRIOR_TO_LIB_PRIOR.put(Priority.TWO.value(), org.libtorrent4j.Priority.TWO);
        PRIOR_TO_LIB_PRIOR.put(Priority.THREE.value(), org.libtorrent4j.Priority.THREE);
        PRIOR_TO_LIB_PRIOR.put(Priority.DEFAULT.value(), org.libtorrent4j.Priority.DEFAULT);
        PRIOR_TO_LIB_PRIOR.put(Priority.FIVE.value(), org.libtorrent4j.Priority.FIVE);
        PRIOR_TO_LIB_PRIOR.put(Priority.SIX.value(), org.libtorrent4j.Priority.SIX);
        PRIOR_TO_LIB_PRIOR.put(Priority.TOP_PRIORITY.value(), org.libtorrent4j.Priority.TOP_PRIORITY);
    }

    private static final SparseArray<Priority> LIB_PRIOR_TO_PRIOR = new SparseArray<>();
    static {
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.IGNORE.swig(), Priority.IGNORE);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.LOW.swig(), Priority.LOW);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.TWO.swig(), Priority.TWO);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.THREE.swig(), Priority.THREE);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.DEFAULT.swig(), Priority.DEFAULT);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.FIVE.swig(), Priority.FIVE);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.SIX.swig(), Priority.SIX);
        LIB_PRIOR_TO_PRIOR.put(org.libtorrent4j.Priority.TOP_PRIORITY.swig(), Priority.TOP_PRIORITY);
    }

    public static org.libtorrent4j.Priority[] convert(@NonNull Priority[] priorities)
    {
        int n = priorities.length;
        org.libtorrent4j.Priority[] p = new org.libtorrent4j.Priority[n];
        for (int i = 0; i < n; i++) {
            Priority priority = priorities[i];
            if (priority == null) {
                p[i] = null;
                continue;
            }
            org.libtorrent4j.Priority converted = PRIOR_TO_LIB_PRIOR.get(priority.value());
            if (converted == null)
                converted = org.libtorrent4j.Priority.DEFAULT;

            p[i] = converted;
        }

        return p;
    }

    public static Priority[] convert(@NonNull org.libtorrent4j.Priority[] priorities)
    {
        int n = priorities.length;
        Priority[] p = new Priority[n];
        for (int i = 0; i < n; i++) {
            org.libtorrent4j.Priority priority = priorities[i];
            if (priority == null) {
                p[i] = null;
                continue;
            }
            Priority converted = LIB_PRIOR_TO_PRIOR.get(priority.swig());
            if (converted == null)
                converted = Priority.DEFAULT;

            p[i] = converted;
        }

        return p;
    }

    public static org.libtorrent4j.Priority convert(@NonNull Priority priority)
    {
        org.libtorrent4j.Priority converted = PRIOR_TO_LIB_PRIOR.get(priority.value());
        if (converted == null)
            converted = org.libtorrent4j.Priority.DEFAULT;

        return converted;
    }

    public static Priority convert(@NonNull org.libtorrent4j.Priority priority)
    {
        Priority converted = LIB_PRIOR_TO_PRIOR.get(priority.swig());
        if (converted == null)
            converted = Priority.DEFAULT;

        return converted;
    }
}
