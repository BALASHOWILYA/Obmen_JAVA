package com.sad_ballala_projects.obmenknigami_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sad_ballala_projects.obmenknigami_java.adapter.DataSender;
import com.sad_ballala_projects.obmenknigami_java.adapter.PostAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private NavigationView nav_view;
    private FirebaseAuth mAuth;
    private TextView userEmail;
    private AlertDialog dialog;
    private Toolbar toolbar;
    private PostAdapter.OnItemClickCustom onItemClickCustom;
    private RecyclerView rcView;
    private PostAdapter postAdapter;
    private DataSender dataSender;
    private DbManager dbManager;
    public static String MAUTH = "";
    private String current_cat = "научная литература";
    private final int EDIT_RES = 12;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLog", "On Create" );
        setContentView(R.layout.activity_main);
        addAds();
        init();
    }

    private void setOnItemClickCustom(){
        onItemClickCustom = new PostAdapter.OnItemClickCustom() {
            @Override
            public void onItemSelected(int position) {
                Log.d("MyLog", "Position : " + position);
            }
        };
    }

    // не придется еще раз запускать активити мы просто вернемся на наше старое активити и будет ждать ответа от editactivity должен отправить то что было в спинере
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_RES && resultCode == RESULT_OK && data != null){
            current_cat = data.getStringExtra("cat");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        getUserData();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adView != null){
            adView.resume();
        }

        if(current_cat.equals("my_ads")){
            dbManager.getMyAdsDataFromDb(mAuth.getUid());
        }
         else{
            dbManager.getDataFromBd(current_cat);
        }
        Log.d("MyLog", "On Resume" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adView != null){
            adView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adView != null){
            adView.destroy();
        }

    }

    public void onClickEdit(View View){
        if(mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser().isEmailVerified()){
            Intent i = new Intent(MainActivity.this, EditActivity.class);
            startActivityForResult(i, EDIT_RES);} else{
                showDialog(R.string.alert, R.string.email_not_verified);
            }
        }
    }

    private void getUserData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            userEmail.setText(currentUser.getEmail());
            MAUTH = mAuth.getUid();
        } else {
            userEmail.setText(R.string.not_reg);
            MAUTH = "";
        }
    }

    private void showDialog(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }


    private void init()
    {

        setOnItemClickCustom();
        rcView = findViewById(R.id.rcView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        List<NewPost> arrayPost = new ArrayList<>();

        postAdapter = new PostAdapter(arrayPost, this, onItemClickCustom);
        rcView.setAdapter(postAdapter);

        nav_view = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.toggle_open, R.string.toggle_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
        userEmail = nav_view.getHeaderView(0).findViewById(R.id.tvEmail);
        mAuth = FirebaseAuth.getInstance();
        getDataDB();
        dbManager = new DbManager(dataSender, this);
        //
        postAdapter.setDbManager(dbManager);

    }

    private void getDataDB(){
        dataSender = new DataSender() {
            @Override
            public void onDataReceived(List<NewPost> listData) {
                Collections.reverse(listData);
                postAdapter.updateAdapter(listData);
            }
        };
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.id_a_science_book:
                current_cat = "научная литература";
                dbManager.getDataFromBd("научная литература");
                break;

            case R.id.id_a_humorous_story:
                current_cat = "комедия";
                dbManager.getDataFromBd("комедия");
                break;
            case R.id.id_a_romance:
                current_cat = "романтика";
                dbManager.getDataFromBd("романтика");
                break;
            case R.id.id_a_detective_story:
                current_cat = "детектив";
                dbManager.getDataFromBd("детектив");
                break;
            case R.id.id_an_adventure_novel:
                current_cat = "приключения";
                dbManager.getDataFromBd("приключения");
                break;
            case R.id.id_a_fiction_book:
                current_cat = "фантастика";
                dbManager.getDataFromBd("фантастика");
                break;
            case R.id.id_a_science_fiction_book:
                current_cat = "фантастика";
                dbManager.getDataFromBd("научная фантастика");
                break;
            case R.id.id_a_drama:
                current_cat = "драма";
                dbManager.getDataFromBd("драма");
                break;
            case R.id.id_a_psychologyt:
                current_cat = "психология";
                dbManager.getDataFromBd("психология");
                break;
            case R.id.id_a_horror_story:
                current_cat = "ужасы";
                dbManager.getDataFromBd("ужасы");
                break;
            case R.id.id_a_fairy_tale:
                current_cat = "детская литература";
                dbManager.getDataFromBd("детская литература");
                break;
            case R.id.id_a_war_novel:
                current_cat = "боевик";
                dbManager.getDataFromBd("боевик");
                break;
            case R.id.id_a_historical_novel:
                current_cat = "история";
                dbManager.getDataFromBd("история");
                break;
            case R.id.id_an_autobiography:
                current_cat = "автобиография";
                dbManager.getDataFromBd("автобиография");
                break;
            case R.id.id_a_biography:
                current_cat = "биография";
                dbManager.getDataFromBd("биография");
                break;
            case R.id.id_a_sport:
                current_cat = "спорт";
                dbManager.getDataFromBd("спорт");
                break;
            case R.id.id_a_classic:
                current_cat = "классика";
                dbManager.getDataFromBd("классика");
                break;

            case R.id.id_my_ads:
                current_cat = "my_ads";
                dbManager.getMyAdsDataFromDb(mAuth.getUid());
                break;

            case R.id.id_textbooks_for_the_1st_grade:
                current_cat = "учебники за 1 класс";
                dbManager.getDataFromBd("учебники за 1 класс");
                break;
            case R.id.id_textbooks_for_the_2nd_grade:
                current_cat = "учебники за 2 класс";
                dbManager.getDataFromBd("учебники за 2 класс");
                break;
            case R.id.id_textbooks_for_the_3rd_grade:
                current_cat = "учебники за 3 класс";
                dbManager.getDataFromBd("учебники за 3 класс");
                break;
            case R.id.id_textbooks_for_the_4th_grade:
                current_cat = "учебники за 4 класс";
                dbManager.getDataFromBd("учебники за 4 класс");
                break;
            case R.id.id_textbooks_for_the_5th_grade:
                current_cat = "учебники за 5 класс";
                dbManager.getDataFromBd("учебники за 5 класс");
                break;
            case R.id.id_textbooks_for_the_6th_grade:
                current_cat = "учебники за 6 класс";
                dbManager.getDataFromBd("учебники за 6 класс");
                break;
            case R.id.id_textbooks_for_the_7th_grade:
                current_cat = "учебники за 7 класс";
                dbManager.getDataFromBd("учебники за 7 класс");
                break;
            case R.id.id_textbooks_for_the_8th_grade:
                current_cat = "учебники за 8 класс";
                dbManager.getDataFromBd("учебники за 8 класс");
                break;
            case R.id.id_textbooks_for_the_9th_grade:
                current_cat = "учебники за 9 класс";
                dbManager.getDataFromBd("учебники за 9 класс");
                break;
            case R.id.id_textbooks_for_the_10th_grade:
                current_cat = "учебники за 10 класс";
                dbManager.getDataFromBd("учебники за 10 класс");
                break;
            case R.id.id_textbooks_for_the_11th_grade:
                current_cat = "учебники за 11 класс";
                dbManager.getDataFromBd("учебники за 11 класс");
                break;


            case R.id.id_sign_up:
                signUpDialog(R.string.ac_sign_up, R.string.sign_up_action,R.string.google_sign_up, 0);

                break;

            case R.id.id_sign_in:
                signUpDialog(R.string.ac_sign_in, R.string.sign_in_action,R.string.google_sign_in,1);
                break;

            case R.id.id_sign_out:
                signOut();
                break;
        }
        return true;
    }

    private void signUpDialog(int title, int buttonTitle,int b2Title,final int index){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sign_up_layout, null);
        dialogBuilder.setView(dialogView);
        TextView titleTextView = dialogView.findViewById(R.id.tvAlertTitle);
        titleTextView.setText(title);
        Button b = dialogView.findViewById(R.id.buttonSignUp);
        EditText edEmail = dialogView.findViewById(R.id.edEmail);
        EditText edPassword = dialogView.findViewById(R.id.edPassword);
        b.setText(buttonTitle);
        b.setOnClickListener((v) ->{

                if(index == 0){
                    signUp(edEmail.getText().toString(),edPassword.getText().toString());
                } else{
                    signIn(edEmail.getText().toString(), edPassword.getText().toString());
                }
                dialog.dismiss();
        });
        dialog = dialogBuilder.create();
        dialog.show();



    }

    private void signUp(String email, String password) {

        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, (task) ->{

                            if (task.isSuccessful()) {
                                if(task.isSuccessful()){
                                    if(mAuth.getCurrentUser() != null){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        sendEmailVerification(user);
                                    }
                                    getUserData();


                            } else {
                                Log.w("MyLog", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show(); }
                            }

                    });
        } else{
            Toast.makeText(this, "Email or Password is empty.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void signIn(String email, String password) {
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getUserData();

                            } else {
                                Log.w("MyLog", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Email or Password is empty", Toast.LENGTH_LONG).show();
        }
    }

    private void signOut(){
        mAuth.signOut();
        getUserData();
    }

    private void addAds(){
        MobileAds.initialize(this);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    private void sendEmailVerification(FirebaseUser user){
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showDialog(R.string.alert, R.string.email_verification_sent);
                }
            }
        });
    }




}
