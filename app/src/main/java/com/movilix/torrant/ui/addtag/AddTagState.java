

package com.movilix.torrant.ui.addtag;

import com.movilix.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class AddTagState extends BaseObservable {
    private Long existsTagId;
    private String name;
    private int color = -1;

    @Bindable
    public Long getExistsTagId() {
        return existsTagId;
    }

    public void setExistsTagId(Long existsTagId) {
        this.existsTagId = existsTagId;
        notifyPropertyChanged(BR.existsTagId);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyPropertyChanged(BR.color);
    }
}
