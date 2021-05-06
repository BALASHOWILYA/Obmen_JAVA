package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.android.gms.common.images.ImageManager;
import com.sad_ballala_projects.obmenknigami_java.R;
import com.sad_ballala_projects.obmenknigami_java.utils.ImagesManager;
import com.sad_ballala_projects.obmenknigami_java.utils.OnBitMapLoaded;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends PagerAdapter implements OnBitMapLoaded {
    private Activity context;
    private LayoutInflater inflater;
    private List<String> imagesUris;
    private List<Bitmap> bmList;
    private ImagesManager imagesManager;
    private boolean isFireBaseUri = false;



    //чтобы передавать с editActivity context


    public ImageAdapter(Activity context) {
        this.context = context;
        imagesManager = new ImagesManager(context, this);
        inflater = LayoutInflater.from(context);
        imagesUris = new ArrayList<>();
        bmList = new ArrayList<>();
    }

    @Override
    public int getCount() {

        int size;
        if(isFireBaseUri){
            size = imagesUris.size();
        } else{

            size = bmList.size();
        }

        return size;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.pager_item, container, false);
        ImageView imItem = view.findViewById(R.id.imageViewPager);


        if(isFireBaseUri){
            String uri = imagesUris.get(position);
            Picasso.get().load(uri).into(imItem);
        }
        else{
            imItem.setImageBitmap(bmList.get(position));
        }


        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    public void updateImages(List<String> images){
        if(isFireBaseUri) {
            imagesUris.clear();
            imagesUris.addAll(images);
            notifyDataSetChanged();
        } else{
            imagesManager.resizeMultiLargeImages(images);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void onBitMapLoaded(final List<Bitmap> bitmap) {
        context.runOnUiThread(() -> {
            bmList.clear();
            bmList.addAll(bitmap);
            notifyDataSetChanged();
        });

    }

    public void setFireBaseUri(boolean fireBaseUri) {
        isFireBaseUri = fireBaseUri;
    }
}
