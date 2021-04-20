package com.sad_ballala_projects.obmenknigami_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;
import com.squareup.picasso.Picasso;


public class ShowLayoutActivity extends AppCompatActivity {
    private TextView tvTitle, tvExchange, tvDisc, tvTel;
    private ImageView imMain;

    public ShowLayoutActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_layout);
        init();
    }

    private void init(){
        tvTitle = findViewById(R.id.tvMainTitle);
        tvTel = findViewById(R.id.tvMainTel);
        tvExchange = findViewById(R.id.tvMainExchange);
        tvDisc = findViewById(R.id.tvMainDisc);
        imMain = findViewById(R.id.imMain);
        if(getIntent() != null){
            Intent i = getIntent();
            tvTitle.setText(i.getStringExtra(MyConstants.TITLE));
            tvTel.setText(i.getStringExtra(MyConstants.TEL));
            tvExchange.setText(i.getStringExtra(MyConstants.CHANGE));
            tvDisc.setText(i.getStringExtra(MyConstants.DISC));
            Picasso.get().load(i.getStringExtra(MyConstants.IMAGE_ID)).into(imMain);

        }

    }
}