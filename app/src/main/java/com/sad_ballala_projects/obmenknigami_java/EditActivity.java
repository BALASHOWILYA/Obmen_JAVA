package com.sad_ballala_projects.obmenknigami_java;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

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
import com.sad_ballala_projects.obmenknigami_java.adapter.ImageAdapter;
import com.sad_ballala_projects.obmenknigami_java.screens.ChooseImagesActivity;
import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class EditActivity extends AppCompatActivity {
    // Create a Cloud Storage reference from the app
    private Spinner spinner;
    private StorageReference mStorageRef;
    private ImageView imItem;
    private String[] uploadUri = new String[3];
    private DatabaseReference dRef;
    private FirebaseAuth mAuth;
    private EditText edTitle, edTel, edDisc, edChange;
    private boolean edit_state = false;
    private String temp_cat = "";
    private String temp_uid = "";
    private String temp_time = "";
    private String temp_key = "";
    private String temp_total_views = "";
    private String temp_image_url = "";
    private boolean is_image_update = false;
    private ProgressDialog pd;

    private int load_image_counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);
        init();
    }


    private void init(){

        //ViewPager vp = findViewById(R.id.view_pager);
        //ImageAdapter imageAdapter = new ImageAdapter(this);
        //vp.setAdapter(imageAdapter);
        uploadUri[0] = "empty";
        uploadUri[1] = "empty";
        uploadUri[2] = "empty";

        pd = new ProgressDialog(this);
        pd.setMessage("Идет загрузка...");
        edTitle = findViewById(R.id.edTitle);
        edTel = findViewById(R.id.edTel);
        edDisc = findViewById(R.id.edDisc);
        edChange = findViewById(R.id.edChange);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.getSelectedItem();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        imItem = findViewById(R.id.imItem);
        getMyIntent();
    }

    private void getMyIntent(){
        if(getIntent() != null){
            Intent i = getIntent();
            edit_state = i.getBooleanExtra(MyConstants.EDIT_STATE,false);
            if(edit_state) setDataAds(i);
        }
    }

    private void setDataAds(Intent i){
        Picasso.get().load(i.getStringExtra(MyConstants.IMAGE_ID)).into(imItem);
        edTel.setText(i.getStringExtra(MyConstants.TEL));
        edTitle.setText(i.getStringExtra(MyConstants.TITLE));
        edChange.setText(i.getStringExtra(MyConstants.CHANGE));
        edDisc.setText(i.getStringExtra(MyConstants.DISC));
        temp_cat = i.getStringExtra(MyConstants.CAT);
        temp_uid = i.getStringExtra(MyConstants.UID);
        temp_time = i.getStringExtra(MyConstants.TIME);
        temp_key = i.getStringExtra(MyConstants.KEY);
        temp_image_url = i.getStringExtra(MyConstants.IMAGE_ID);
        temp_total_views = i.getStringExtra(MyConstants.TOTAL_VIEWS);


    }





    private void uploadImage(){

        if(load_image_counter < uploadUri.length){
        if(!uploadUri[load_image_counter].equals("empty")) {
            Bitmap bitMap = null;
            try {
                bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uploadUri[load_image_counter]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            assert bitMap != null;
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
                    if(task.getResult() == null) return;
                    uploadUri[load_image_counter] = task.getResult().toString();
                    assert uploadUri != null;
                    load_image_counter++;
                    if (load_image_counter < uploadUri.length) {
                        uploadImage();
                    } else {
                        savePost();
                        Toast.makeText(EditActivity.this, "Upload done !", Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            load_image_counter++;
            uploadImage();
        }} else{
            savePost();
            finish();
        }

    }

    private void uploadUpdateImage(){
        Bitmap bitMap = null;
        try {
            bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.parse(uploadUri[load_image_counter]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assert bitMap != null;
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] byteArray = out.toByteArray();
        final StorageReference mRef = FirebaseStorage.getInstance().getReferenceFromUrl(temp_image_url);
        UploadTask up = mRef.putBytes(byteArray);
        Task<Uri> task = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return mRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uploadUri[load_image_counter] = task.getResult().toString();
                assert uploadUri != null;
                temp_image_url = uploadUri.toString();
                updatePost();
                savePost();
                Toast.makeText(EditActivity.this, "Upload done !" , Toast.LENGTH_LONG).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void onClickSavePost(View View){

        pd.show();

        if(!edit_state) {
            uploadImage();
        } else{
            if(is_image_update){
                uploadUpdateImage();
            }
            else {
                updatePost();
            }
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 15 && data != null){
            if(resultCode == RESULT_OK){

                Log.d("MyLog", "Uri main" + data.getStringExtra("uriMain"));
                Log.d("MyLog", "Uri 2" + data.getStringExtra("uri2"));
                Log.d("MyLog", "Uri 3" + data.getStringExtra("uri3"));
                uploadUri[0] = data.getStringExtra("uriMain");
                uploadUri[1] = data.getStringExtra("uri2");
                uploadUri[2] = data.getStringExtra("uri3");

            }
        }
    }


    public void OnClickImage(View View){
        Intent i = new Intent(EditActivity.this, ChooseImagesActivity.class);
        startActivityForResult(i, 15);

    }


    private  void updatePost(){
        dRef = FirebaseDatabase.getInstance().getReference(temp_cat);


            NewPost post = new NewPost();
            post.setImageId(temp_image_url);
            post.setTitle(edTitle.getText().toString());
            post.setChange(edChange.getText().toString());
            post.setTel(edTel.getText().toString());
            post.setDisc(edDisc.getText().toString());
            post.setKey(temp_key);
            post.setCat(temp_cat);
            post.setTime(temp_time);
            post.setUid(temp_uid);
            post.setTotal_views(temp_total_views);

            // создаем новую подпапку, название папки зависит от Uid, далее создается объявление с помощью key, а затем помещаем туда значение
            dRef.child(temp_key).child("ads").setValue(post);

            finish();

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


            post.setImageId(uploadUri[0]);
            post.setImageId2(uploadUri[1]);
            post.setImageId3(uploadUri[2]);
            post.setTitle(edTitle.getText().toString());
            post.setChange(edChange.getText().toString());
            post.setTel(edTel.getText().toString());
            post.setDisc(edDisc.getText().toString());
            post.setKey(key);
            post.setCat(spinner.getSelectedItem().toString());
            post.setTime(String.valueOf(System.currentTimeMillis()));
            post.setUid(mAuth.getUid());
            post.setTotal_views("0");
            // создаем новую подпапку, название папки зависит от Uid, далее создается объявление с помощью key, а затем помещаем туда значение
            if(key != null)dRef.child(key).child("ads").setValue(post);
            Intent i = new Intent();
            i.putExtra("cat", spinner.getSelectedItem().toString());
            setResult(RESULT_OK, i);


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pd.dismiss();
    }
}
