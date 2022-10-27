

package com.movilix.torrant.ui.main.drawer;

/*
 * Clickable item in group.
 */

public class DrawerGroupItem
{
    public long id;
    public int iconResId;
    public String name;

    public DrawerGroupItem(long id, int iconResId, String name)
    {
        this.id = id;
        this.iconResId = iconResId;
        this.name = name;
    }
}
