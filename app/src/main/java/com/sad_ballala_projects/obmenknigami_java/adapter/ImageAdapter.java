package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sad_ballala_projects.obmenknigami_java.R;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    int[] imageArray = {R.drawable.image_test_1, R.drawable.image_test_2, R.drawable.image_test_3};

    //чтобы передавать с editActivity context


    public ImageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.pager_item, container, false);
        ImageView imItem = view.findViewById(R.id.imageViewPager);
        imItem.setImageResource(imageArray[position]);
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
}
