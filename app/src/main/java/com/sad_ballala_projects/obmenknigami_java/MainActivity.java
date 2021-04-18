package com.sad_ballala_projects.obmenknigami_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        getUserData();
        
    }

    public void onClickEdit(View View){
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);
    }

    private void getUserData(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            userEmail.setText(currentUser.getEmail());
        } else {
            userEmail.setText(R.string.not_reg);
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
        getDataDB();
        DbManager dbManager = new DbManager(dataSender);
        dbManager.getDataFromBd("научная литература");

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
                Toast.makeText(this, "Presed id_a_science_book", Toast.LENGTH_LONG).show();
                break;

            case R.id.id_a_humorous_story:
                Toast.makeText(this, "Presed id_a_humorous_story", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_romance:
                Toast.makeText(this, "Presed id_a_romance", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_detective_story:
                Toast.makeText(this, "Presed id_a_detective_story", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_an_adventure_novel:
                Toast.makeText(this, "Presed id_an_adventure_novel", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_fiction_book:
                Toast.makeText(this, "Presed id_a_fiction_book", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_science_fiction_book:
                Toast.makeText(this, "Presed id_a_science_fiction_book", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_drama:
                Toast.makeText(this, "Presed id_a_drama", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_psychologyt:
                Toast.makeText(this, "Presed id_a_psychologyt", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_horror_story:
                Toast.makeText(this, "Presed id_a_horror_story", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_fairy_tale:
                Toast.makeText(this, "Presed id_a_fairy_tale", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_war_novel:
                Toast.makeText(this, "Presed id_a_war_novel", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_historical_novel:
                Toast.makeText(this, "Presed id_a_historical_novel", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_an_autobiography:
                Toast.makeText(this, "Presed id_an_autobiography", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_biography:
                Toast.makeText(this, "Presed id_a_biography", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_sport:
                Toast.makeText(this, "Presed id_a_sport", Toast.LENGTH_LONG).show();
                break;
            case R.id.id_a_classic:
                Toast.makeText(this, "Presed id_a_classic", Toast.LENGTH_LONG).show();
                break;


            case R.id.id_textbooks_for_the_1st_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_1st_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_2nd_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_2nd_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_3rd_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_3rd_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_4th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_4th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_5th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_5th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_6th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_6th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_7th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_7th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_8th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_8th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_9th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_9th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_10th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_10th_grade", Toast.LENGTH_LONG)
                        .show();
                break;
            case R.id.id_textbooks_for_the_11th_grade:
                Toast.makeText(this, "Presed id_textbooks_for_the_11th_grade", Toast.LENGTH_LONG)
                        .show();
                break;


            case R.id.id_sign_up:
                signUpDialog(R.string.ac_sign_up, R.string.sign_up_action, 0);

                break;

            case R.id.id_sign_in:
                signUpDialog(R.string.ac_sign_in, R.string.sign_in_action, 1);
                break;

            case R.id.id_sign_out:
                signOut();
                break;
        }
        return true;
    }

    private void signUpDialog(int title, int buttonTitle, int index){
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

                                getUserData();

                            } else {
                                Log.w("MyLog", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

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




}
