package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sad_ballala_projects.obmenknigami_java.NewPost;
import com.sad_ballala_projects.obmenknigami_java.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {

    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;

    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom) {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads, parent, false);
        return new ViewHolderData(view, onItemClickCustom);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(arrayPost.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayPost.size();
    }

    public class ViewHolderData extends  RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle, tvTel, tvDisc, tvChange;

        private ImageView imAds;
        private OnItemClickCustom onItemClickCustom;


        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom ) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChange = itemView.findViewById(R.id.tvChange);
            tvTel = itemView.findViewById(R.id.tvTel);
            tvDisc = itemView.findViewById(R.id.tvDisc);
            imAds = itemView.findViewById(R.id.imAds);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }

        public void setData(NewPost newPost){
            tvTitle.setText(newPost.getTitle());
            tvChange.setText(newPost.getTitle());
            tvTel.setText(newPost.getTitle());
            tvDisc.setText(newPost.getTitle());



        }

        @Override
        public void onClick(View v) {
            onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }

    public  interface OnItemClickCustom{
        public void onItemSelected(int position);
    }

}
