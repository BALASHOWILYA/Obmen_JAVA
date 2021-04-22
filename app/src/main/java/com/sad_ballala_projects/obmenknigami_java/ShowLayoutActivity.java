package com.sad_ballala_projects.obmenknigami_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sad_ballala_projects.obmenknigami_java.adapter.ImageAdapter;
import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;


public class ShowLayoutActivity extends AppCompatActivity {
    private TextView tvTitle, tvExchange, tvDisc, tvTel;
    private ImageView imMain;
    private List<String> imagesUris;
    private ImageAdapter imageAdapter;
    private TextView vtImagesCounter;

    public ShowLayoutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_layout);
        init();
    }

    private void init(){

        imagesUris = new ArrayList<>();

        ViewPager vp = findViewById(R.id.view_pager);
        imageAdapter = new ImageAdapter(this);
        vp.setAdapter(imageAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String dataText = position + 1 + "/"  + imagesUris.size();
                vtImagesCounter.setText(dataText);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vtImagesCounter = findViewById(R.id.tvImagesCounter);
        tvTitle = findViewById(R.id.tvMainTitle);
        tvTel = findViewById(R.id.tvMainTel);
        tvExchange = findViewById(R.id.tvMainExchange);
        tvDisc = findViewById(R.id.tvMainDisc);

        if(getIntent() != null){
            Intent i = getIntent();
            tvTitle.setText(i.getStringExtra(MyConstants.TITLE));
            tvTel.setText(i.getStringExtra(MyConstants.TEL));
            tvExchange.setText(i.getStringExtra(MyConstants.CHANGE));
            tvDisc.setText(i.getStringExtra(MyConstants.DISC));
            String[] images = new String[3];
            images[0] = i.getStringExtra(MyConstants.IMAGE_ID);
            images[1] = i.getStringExtra(MyConstants.IMAGE_ID_2);
            images[2] = i.getStringExtra(MyConstants.IMAGE_ID_3);

            for (String s : images){
                if(!s.equals("empty"))
                {imagesUris.add(s);}
            }
            imageAdapter.updateImages(imagesUris);
            String dataText;
            if(imagesUris.size() > 0)
            dataText = 1 + "/"  + imagesUris.size();
            else dataText = 0 + "/"  + imagesUris.size();
            vtImagesCounter.setText(dataText);
           // Picasso.get().load(i.getStringExtra(MyConstants.IMAGE_ID)).into(imMain);

        }

    }
}