

package com.movilix.torrant.ui.main.drawer;

public class NoTagsItem extends AbstractTagItem {
    @Override
    public boolean isSame(AbstractTagItem o) {
        return o instanceof NoTagsItem;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
