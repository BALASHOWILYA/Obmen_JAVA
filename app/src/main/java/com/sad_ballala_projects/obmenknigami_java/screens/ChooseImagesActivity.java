package com.sad_ballala_projects.obmenknigami_java.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.images.ImageManager;
import com.sad_ballala_projects.obmenknigami_java.R;
import com.sad_ballala_projects.obmenknigami_java.utils.ImagesManager;
import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;
import com.sad_ballala_projects.obmenknigami_java.utils.OnBitMapLoaded;
import com.squareup.picasso.Picasso;

public class ChooseImagesActivity extends AppCompatActivity {
    private String[] uris = new String[3];
    private ImageView imMain, im2, im3;
    private ImageView imagesViews[] = new  ImageView[3];
    private ImagesManager imageManager;
    private OnBitMapLoaded onBitMapLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
        init();

    }

    private void init(){
        imMain = findViewById(R.id.mainImage);
        im2 = findViewById(R.id.image2);
        im3 = findViewById(R.id.image3);
        uris[0] = "empty";
        uris[1] = "empty";
        uris[2] = "empty";
        imagesViews[0] = imMain;
        imagesViews[1] = im2;
        imagesViews[2] = im3;
        getMyIntent();
        setOnBitMapLoaded();
        imageManager = new ImagesManager(this, onBitMapLoaded );


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null && data.getData() != null){
            switch (requestCode){
                case 1:
                    uris[0] = data.getData().toString();
                    imageManager.setImageIndex(0);
                    imageManager.resizeLargeImage(uris[0]);

                    break;
                case 2:
                    uris[1] = data.getData().toString();
                    imageManager.setImageIndex(1);
                    imageManager.resizeLargeImage(uris[1]);
                    break;
                case 3:
                    uris[2] = data.getData().toString();
                    imageManager.setImageIndex(2);
                    imageManager.resizeLargeImage(uris[2]);
                    break;
            }
        }
    }

    private void setOnBitMapLoaded(){
        onBitMapLoaded = new OnBitMapLoaded() {
            @Override
            public void onBitMapLoaded(Bitmap bitmap, int index) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imagesViews[index].setImageBitmap(bitmap);
                    }
                });
            }
        };
    }


    public  void onClickBack(View view){

        Intent i = new Intent();
        i.putExtra("uriMain", uris[0]);
        i.putExtra("uri2", uris[1]);
        i.putExtra("uri3", uris[2]);
        setResult(RESULT_OK, i);
        finish();
    }

    public  void onClickMainImage(View view){
        getImage(1);
    }
    public  void onClickImage2(View view){
        getImage(2);
    }
    public  void onClickImage3(View view){
        getImage(3);
    }

    //кагда добавляем картинку, для этого запускаем интэнт и ждем результата, чтобы он нам выдал ссылку на картинку которую мы выбрали
    private void getImage(int index){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, index);
    }

    private void getMyIntent(){
        Intent i = getIntent();
        if(i != null){


             uris[0] = i.getStringExtra(MyConstants.IMAGE_ID);
             uris[1] = i.getStringExtra(MyConstants.IMAGE_ID_2);
            uris[2] = i.getStringExtra(MyConstants.IMAGE_ID_3);
            setImages(uris);
        }
    }

    private void setImages(String[] uris){

        for(int i = 0; i < uris.length; i++ ){
            if(!uris[i].equals("empty"))  showImages(uris[i], i);
        }


    }

    private void showImages(String uri, int position){
        if(uri.startsWith("http")){
            Picasso.get().load(uri).into(imagesViews[position]);
        }
        else{
            imagesViews[position].setImageURI(Uri.parse(uri));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onClickDeleteMainImage(View view) {
        imMain.setImageResource(android.R.drawable.ic_menu_add);
        uris[0] = "empty";

    }

    public void onClickDeleteImage2(View view) {
        im2.setImageResource(android.R.drawable.ic_menu_add);
        uris[1] = "empty";
    }

    public void onClickDeleteImage3(View view) {
        im3.setImageResource(android.R.drawable.ic_menu_add);
        uris[2] = "empty";
    }
}