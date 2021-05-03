package com.sad_ballala_projects.obmenknigami_java.accountHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sad_ballala_projects.obmenknigami_java.EditActivity;
import com.sad_ballala_projects.obmenknigami_java.MainActivity;
import com.sad_ballala_projects.obmenknigami_java.R;
import com.sad_ballala_projects.obmenknigami_java.screens.ChooseImagesActivity;
import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;

public class SignInAct extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        init();
    }

    private void init(){
        Button GoogleButton = findViewById(R.id.bSignGoogle);
        GoogleButton.setOnClickListener((v) ->{

                Intent i = new Intent(this, MainActivity.class);
                startActivityForResult(i, 10);

        });
    }



}
