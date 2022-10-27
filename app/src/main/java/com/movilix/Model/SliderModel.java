package com.movilix.Model;

public class SliderModel {
    private String name;
    private int imageSource;

    public SliderModel (int imageSource, String name) {
        this.name = name;
        this.imageSource = imageSource;
    }

    public String getName() {
        return name;
    }

    public int getImageSource() {
        return imageSource;
    }
}
