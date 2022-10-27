package com.movilix.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.movilix.R;

public class image extends Dialog {
    Bitmap s;
    private Activity activity;

    public image(Activity activity, Bitmap s) {
        super(activity);
        this.s = s;
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.imagedailog);
        init();
    }


    void init() {
        ImageView imageView = findViewById(R.id.image);
        imageView.setImageBitmap(s);

    }

}
