

package com.movilix.torrant.ui.main.drawer;

/**
 * Represents empty tag without name
 */
public class EmptyTagItem extends AbstractTagItem {
    @Override
    public boolean isSame(AbstractTagItem o) {
        return o instanceof EmptyTagItem;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }
}
