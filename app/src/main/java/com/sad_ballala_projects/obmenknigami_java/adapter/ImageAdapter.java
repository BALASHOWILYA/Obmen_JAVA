package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sad_ballala_projects.obmenknigami_java.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<String> imagesUris;



    //чтобы передавать с editActivity context


    public ImageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        imagesUris = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return imagesUris.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.pager_item, container, false);
        ImageView imItem = view.findViewById(R.id.imageViewPager);
        imItem.setImageURI(Uri.parse(imagesUris.get(position)));
        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout)object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    public void updateImages(List<String> images){
        imagesUris.clear();
        imagesUris.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
