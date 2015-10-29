package com.project.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        ImageView imageView = new ImageView(getApplicationContext());

        // Get the ID of the image and set it for this ImageView
        byte[] rawImage = intent.getByteArrayExtra(MainActivity.EXTRA_RES_IMG);
        Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length));
        imageView.setImageDrawable(image);
        imageView.setVisibility(ImageView.VISIBLE);

        setContentView(imageView);
    }
}
