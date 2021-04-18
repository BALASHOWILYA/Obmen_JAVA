package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sad_ballala_projects.obmenknigami_java.NewPost;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {

    private List<NewPost> arrayPost;
    private Context context;

    public PostAdapter(List<NewPost> arrayPost, Context context) {
        this.arrayPost = arrayPost;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderData extends  RecyclerView.ViewHolder{
        



        public ViewHolderData(@NonNull View itemView) {
            super(itemView);
        }
    }

}
