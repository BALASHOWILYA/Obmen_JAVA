package com.sad_ballala_projects.obmenknigami_java.accountHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sad_ballala_projects.obmenknigami_java.MainActivity;
import com.sad_ballala_projects.obmenknigami_java.R;

public class AccountHelper {
    //Google Sign In
    private GoogleSignInClient mSignInClient;
    public static final int GOOGLE_SIGN_IN_CODE = 10;
    public static final int GOOGLE_SIGN_IN_LINK_CODE = 15;
    private FirebaseAuth mAuth;
    private MainActivity activity;
    private String tempEmail, tempPassword;


    public AccountHelper(FirebaseAuth mAuth, MainActivity activity) {
        this.mAuth = mAuth;
        this.activity = activity;
        googleAccountManager();
    }
    //Sign Up by Email
    public void signUp(String email, String password) {

        if (!email.equals("") && !password.equals("")) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, (task) ->{

                        if (task.isSuccessful()) {
                                if(mAuth.getCurrentUser() != null){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    sendEmailVerification(user);
                                }
                                activity.getUserData();


                            } else {
                                FirebaseAuthUserCollisionException exception = (FirebaseAuthUserCollisionException)
                                        task.getException();
                                if(exception == null) return;
                                if(exception.getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")){
                                    linkEmailAndPassword(email, password);
                                }
                                }


                    });
        } else{
            Toast.makeText(activity, "Email or Password is empty.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void signIn(String email, String password) {
        if (!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                activity.getUserData();

                            } else {
                                Log.w("MyLog", "signInWithEmail:failure", task.getException());
                                Toast.makeText(activity, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(activity, "Email or Password is empty", Toast.LENGTH_LONG).show();
        }
    }

    public void signOut(){
        mAuth.signOut();
        mSignInClient.signOut();
        activity.getUserData();
        Intent i = new Intent(activity, SignInAct.class);
        activity.startActivity(i);


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

    // Sign In by google account

    private void googleAccountManager(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build();
        mSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    public void signInGoogle(int code){
        Intent signInIntent = mSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, code);
    }

    public void signInFirebaseGoogle(String idToken, int index ){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(activity, "Log In done", Toast.LENGTH_SHORT).show();
                //if(index == 1) linkEmailAndPassword(tempEmail, tempPassword);
                activity.getUserData();
            } else{

            }
        });
    }

    // dialog

    public void showDialogSignInWithLink(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signInGoogle(GOOGLE_SIGN_IN_CODE);
            }
        });
        builder.create();
        builder.show();
    }

    public void showDialog(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

    public void showDialogNotVerified(int title, int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
            }
        });
        builder.setNegativeButton(R.string.send_email_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(mAuth.getCurrentUser() != null) {
                    sendEmailVerification(mAuth.getCurrentUser());
                }
            }
        });
        builder.create();
        builder.show();
    }

    private void linkEmailAndPassword(String email, String password){
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        if(mAuth.getCurrentUser() != null){
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Вы успешно связали аккаунты", Toast.LENGTH_LONG).show();
                            Log.d("MyLog", "linkWithCredential:success");

                            if(task.getResult() == null) return;
                            FirebaseUser user = task.getResult().getUser();
                            sendEmailVerification(user);
                            activity.getUserData();

                        } else {
                            Toast.makeText(activity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    } else{
            Log.d("MyLog", "Please Sign In your google account" );
            tempEmail = email;
            tempPassword = password;
            showDialogSignInWithLink(R.string.alert,R.string.sign_link_message);
        }
    }


}
