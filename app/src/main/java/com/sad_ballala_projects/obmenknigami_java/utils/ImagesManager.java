package com.sad_ballala_projects.obmenknigami_java.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImagesManager {

    private Context context;
    private final int MAX_SIZE = 1000;
    private int width;
    private int height;
    private OnBitMapLoaded onBitMapLoaded;
    private int imageIndex = 0;

    public ImagesManager(Context context, OnBitMapLoaded onBitMapLoaded) {
        this.context = context;
        this.onBitMapLoaded = onBitMapLoaded;
    }

    public static Bitmap resizeImage(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float imageRatio = (float)width / (float)height;

        if(imageRatio > 1){
            if(width > maxSize) {
                width = maxSize;
                height = (int) (width / imageRatio);
            }

        } else {
            if(height > maxSize) {
                height = maxSize;
            width = (int)(height * imageRatio);
            }
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public int[] getImageSize(String uri){
        int[] size = new int[2];

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(uri));
            BitmapFactory.decodeStream(inputStream, null, options);
            size[0] = options.outWidth;
            size[1] = options.outHeight;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return size;
    }

    public void resizeLargeImage(final String uri){
        width = getImageSize(uri)[0];
        height = getImageSize(uri)[1];
        float imageRatio = (float)width / (float)height;
        if(imageRatio > 1){
            if(width > MAX_SIZE) {
                width = MAX_SIZE;
                height = (int) (width / imageRatio);
            }

        } else {
            if(height > MAX_SIZE) {
                height = MAX_SIZE;
                width = (int)(height * imageRatio);
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bm = Picasso.get().load(uri).resize(width,height).get();
                    onBitMapLoaded.onBitMapLoaded(bm, imageIndex );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }
}
