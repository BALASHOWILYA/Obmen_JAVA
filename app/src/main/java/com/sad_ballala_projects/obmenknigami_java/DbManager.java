package com.sad_ballala_projects.obmenknigami_java;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sad_ballala_projects.obmenknigami_java.adapter.DataSender;

import java.util.ArrayList;
import java.util.List;

public class DbManager {
    private  Query mQuery;
    private List<NewPost> newPostList;
    private DataSender dataSender;
    private FirebaseDatabase db;
    private int cat_ads_counter = 0;
    private String[] category_ads1 = {"мои объявления","учебники за 1 класс","учебники за 2 класс","учебники за 3 класс","учебники за 4 класс",
            "учебники за 5 класс", "учебники за 6 класс", "учебники за 7 класс","учебники за 8 класс",
            "учебники за 9 класс", "учебники за 10 класс","учебники за 11 класс","научная литература","комедия","романтика",
            "детектив","приключения","фантастика","научная фантастика",
            "драма","психология","ужасы","детская литература","боевик","история","автобиография",
            "биография", "спорт", "классикаклассика" };




    public void deleteItem(NewPost newPost){
        DatabaseReference dbRef = db.getReference(newPost.getCat());
        dbRef.child(newPost.getKey()).removeValue();
    }

    public DbManager(DataSender dataSender) {
        this.dataSender = dataSender;
        newPostList = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
    }

    public void getDataFromBd(String path){


        DatabaseReference dbRef =db.getReference(path);
        mQuery = dbRef.orderByChild("ads/time");
        readDataUpdate();

    }

    public void getMyAdsDataFromBd(String uid){
        if(newPostList.size() > 0) newPostList.clear();
        DatabaseReference dbRef =db.getReference(category_ads1[0]);
        mQuery = dbRef.orderByChild("ads/uid").equalTo(uid);
        readMyAdsDataUpdate(uid);
        cat_ads_counter++;

    }

    public void readDataUpdate(){
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(newPostList.size() > 0) newPostList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    NewPost newPost = ds.child("ads").getValue(NewPost.class);
                    newPostList.add(newPost);
                    //Log.d("MyLog", "Title : " + newPost.getTitle());
            }
                dataSender.onDataReceived(newPostList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void readMyAdsDataUpdate(final String uid){
        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    NewPost newPost = ds.child("ads").getValue(NewPost.class);
                    newPostList.add(newPost);
                    //Log.d("MyLog", "Title : " + newPost.getTitle());
                }
                if(cat_ads_counter > 28) {
                    dataSender.onDataReceived(newPostList);
                    newPostList.clear();
                    cat_ads_counter = 0;
                } else{
                    DatabaseReference dbRef =db.getReference(category_ads1[cat_ads_counter]);
                    mQuery = dbRef.orderByChild("ads/uid").equalTo(uid);
                    readMyAdsDataUpdate(uid);
                    cat_ads_counter++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
