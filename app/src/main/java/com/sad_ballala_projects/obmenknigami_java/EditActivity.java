package com.sad_ballala_projects.obmenknigami_java;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class EditActivity extends AppCompatActivity {
    // Create a Cloud Storage reference from the app
    private Spinner spinner;
    private StorageReference mStorageRef;
    private ImageView imItem;
    private Uri uploadUri;
    private DatabaseReference dRef;
    private FirebaseAuth mAuth;
    private EditText edTitle, edTel, edDisc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        init();
    }


    private void init(){

        edTitle = findViewById(R.id.edTel);
        edTel = findViewById(R.id.edTel);
        edDisc = findViewById(R.id.edDisc);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.getSelectedItem();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        imItem = findViewById(R.id.imItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && data != null && data.getData() != null){
            if(resultCode == RESULT_OK){

                imItem.setImageURI(data.getData());
                uploadImage();
            }
        }
    }

    private void uploadImage(){
        Bitmap bitMap = ((BitmapDrawable)imItem.getDrawable()).getBitmap();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] byteArray = out.toByteArray();
        final StorageReference mRef = mStorageRef.child(System.currentTimeMillis() + "image");
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri = task.getResult();
                assert uploadUri != null;
                Toast.makeText(EditActivity.this, "Upload done :" + uploadUri.toString(), Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void onClickSavePost(View View){
        savePost();
    }


    public void OnClickImage(View View){
        getImage();
    }

    private void getImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 10);
    }

    private  void savePost(){
        // сначала создается центральная основная папка, берем путь этой папки из spinner
        dRef = FirebaseDatabase.getInstance().getReference(spinner.getSelectedItem().toString());
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getUid() != null){
            //генерируем спецальный key под которым мы будем сохранять объявления
            // для тогого чтобы редоктировать особенное объявление
            String key = dRef.push().getKey();
            NewPost post = new NewPost();

            post.setImageId(uploadUri.toString());
            post.setTitle(edTitle.getText().toString());
            post.setTel(edTel.getText().toString());
            post.setDisc(edDisc.getText().toString());
            post.setKey(key);
            // создаем новую подпапку, название папки зависит от Uid, далее создается объявление с помощью key, а затем помещаем туда значение
            if(key != null)dRef.child(mAuth.getUid()).child(key).setValue(post);

        }
    }

}
