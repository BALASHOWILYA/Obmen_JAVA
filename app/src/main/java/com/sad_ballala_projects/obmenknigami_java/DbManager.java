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
   /*
    }*/

    public DbManager(DataSender dataSender) {
        this.dataSender = dataSender;
        newPostList = new ArrayList<>();
    }

    public void getDataFromBd(String path){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef =db.getReference(path);
        mQuery = dbRef.orderByChild("ads/time");
        readDataUpdate();

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

}
