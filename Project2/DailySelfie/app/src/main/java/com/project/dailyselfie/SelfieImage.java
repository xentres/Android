package com.project.dailyselfie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

public class SelfieImage {

    private static final int THUMB_SIZE = 128;

    private final Drawable thumbImage;
    private final String title;
    private final String description;
    private final Drawable image;

    public SelfieImage(Context context, File image) {
        Bitmap rawThumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image.getPath()), THUMB_SIZE, THUMB_SIZE);
        this.thumbImage = new BitmapDrawable(context.getResources() ,rawThumb);
        this.image = Drawable.createFromPath(image.getPath());
        this.title = image.getName();
        Date lastModDate = new Date(image.lastModified());
        this.description = lastModDate.toString();
    }

    public Drawable getThumbImage() {
        return thumbImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public Drawable getImage() {
        return image;
    }

    public byte[] getRawImage() {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
}
