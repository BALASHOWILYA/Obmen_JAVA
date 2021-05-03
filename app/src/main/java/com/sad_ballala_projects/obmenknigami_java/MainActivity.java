package com.sad_ballala_projects.obmenknigami_java;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sad_ballala_projects.obmenknigami_java.accountHelper.AccountHelper;
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
    private AccountHelper accountHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyLog", "On Create" );
        setContentView(R.layout.activity_main);
        addAds();
        init();
        accountHelper.signInGoogle(AccountHelper.GOOGLE_SIGN_IN_CODE);


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

        switch (requestCode){
            case EDIT_RES:
                if( resultCode == RESULT_OK && data != null){
                    current_cat = data.getStringExtra("cat");
                }
                break;
            case AccountHelper.GOOGLE_SIGN_IN_CODE:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    if(account != null)
                   accountHelper.signInFirebaseGoogle(account.getIdToken(), 0);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;
            case AccountHelper.GOOGLE_SIGN_IN_LINK_CODE:
                Task<GoogleSignInAccount> task2 = GoogleSignIn.getSignedInAccountFromIntent(data);

                try {
                    GoogleSignInAccount account = task2.getResult(ApiException.class);
                    if(account != null)
                        accountHelper.signInFirebaseGoogle(account.getIdToken(), 1);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;

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
            if(mAuth.getCurrentUser().isEmailVerified()) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(i, EDIT_RES);
            } else{
                accountHelper.showDialogNotVerified(R.string.alert, R.string.email_not_verified);
            }
        }
    }

    public void getUserData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            userEmail.setText(currentUser.getEmail());
            MAUTH = mAuth.getUid();
        } else {
            userEmail.setText(R.string.not_reg);
            MAUTH = "";
        }
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
        accountHelper = new AccountHelper(mAuth, this);

       // test
        getDataDB();
        dbManager = new DbManager(dataSender, this);
        postAdapter.setDbManager(dbManager);




        Menu menu = nav_view.getMenu();
        MenuItem categoryAccountItem = menu.findItem(R.id.school_book_id);
        SpannableString sp = new SpannableString(categoryAccountItem.getTitle());
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_red)), 0,sp.length(), 0 );
        categoryAccountItem.setTitle(sp);
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



            case R.id.id_sign_out:
                accountHelper.signOut();
                break;
        }
        return true;
    }


    private void addAds(){
        MobileAds.initialize(this);
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }





}
