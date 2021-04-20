package com.sad_ballala_projects.obmenknigami_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowLayoutActivity extends AppCompatActivity {
    private TextView tvTitle, tvExchange, tvDisc, tvTel;
    private ImageView imMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_layout);
    }

    private void init(){
        tvTitle = findViewById(R.id.tvMainTitle);
        tvTel = findViewById(R.id.tvMainTel);
        tvExchange = findViewById(R.id.tvMainExchange);
        tvDisc = findViewById(R.id.tvMainDisc);
    }
}