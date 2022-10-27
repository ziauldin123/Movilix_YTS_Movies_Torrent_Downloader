

package com.movilix.torrant.ui.tag;

import com.movilix.torrant.core.model.data.entity.TagInfo;

import androidx.annotation.NonNull;

public class TagItem {
    @NonNull
    public final TagInfo info;

    public TagItem(@NonNull TagInfo info) {
        this.info = info;
    }

    public boolean isSame(TagItem o) {
        return info.id == o.info.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagItem tagItem = (TagItem) o;

        return info.equals(tagItem.info);
    }

    @Override
    public int hashCode() {
        return info.hashCode();
    }

    @Override
    public String toString() {
        return "TagItem{" +
                "info=" + info +
                '}';
    }
}
