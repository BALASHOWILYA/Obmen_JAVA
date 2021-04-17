package com.sad_ballala_projects.obmenknigami_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView nav_view;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
    }

    private void init()
    {
        nav_view = findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

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
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == 0){
                    signUp(edEmail.getText().toString(),edPassword.getText().toString());
                }
            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();


    }

    private void signUp(String email, String password) {

        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, (task) ->{

                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user != null) Log.d("MyLog", "createUserWithEmail:success" + user.getEmail());

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



}
