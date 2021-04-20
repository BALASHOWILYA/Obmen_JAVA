package com.sad_ballala_projects.obmenknigami_java.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sad_ballala_projects.obmenknigami_java.DbManager;
import com.sad_ballala_projects.obmenknigami_java.EditActivity;
import com.sad_ballala_projects.obmenknigami_java.MainActivity;
import com.sad_ballala_projects.obmenknigami_java.NewPost;
import com.sad_ballala_projects.obmenknigami_java.R;
import com.sad_ballala_projects.obmenknigami_java.ShowLayoutActivity;
import com.sad_ballala_projects.obmenknigami_java.utils.MyConstants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {

    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;
    private DbManager dbManager;



    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom) {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
        this.dbManager = dbManager;

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

        private TextView tvTitle, tvTel, tvDisc, tvChange,tvTotalViews;
        private ImageView imAds;
        private LinearLayout edit_layout;
        private ImageButton deleteButton, editButton;
        private OnItemClickCustom onItemClickCustom;



        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom ) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTotalViews = itemView.findViewById(R.id.tvTotalViews);
            tvChange = itemView.findViewById(R.id.tvChange);
            tvTel = itemView.findViewById(R.id.tvTel);
            tvDisc = itemView.findViewById(R.id.tvDisc);
            imAds = itemView.findViewById(R.id.imAds);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            deleteButton = itemView.findViewById(R.id.imDeleteItem);
            editButton = itemView.findViewById(R.id.imEditItem);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }

        public void setData(NewPost newPost){

            if(newPost.getUid().equals(MainActivity.MAUTH))
            {
                edit_layout.setVisibility(View.VISIBLE);
            } else {
                edit_layout.setVisibility(View.GONE);
            }
            Picasso.get().load(newPost.getImageId()).into(imAds);
            tvTitle.setText(newPost.getTitle());
            String Change = "Обмен на: " + newPost.getChange();
            tvChange.setText(Change);
            String Phone = "Номер телефона: " + newPost.getTel();
            tvTel.setText(Phone);
            String textDisc;
            if(newPost.getDisc().length() > 50) {textDisc = newPost.getDisc().substring(0,50) + "...";}
            else{
                textDisc = newPost.getDisc();
            }
            tvDisc.setText(textDisc);
            tvTotalViews.setText(newPost.getTotal_views());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteDialog(newPost, getAdapterPosition());
                }
            });
            editButton.setOnClickListener( (v) -> {

                    Intent i = new Intent(context, EditActivity.class);
                    i.putExtra(MyConstants.IMAGE_ID, newPost.getImageId());
                    i.putExtra(MyConstants.TITLE, newPost.getTitle());
                    i.putExtra(MyConstants.CHANGE, newPost.getChange());
                    i.putExtra(MyConstants.TEL, newPost.getTel());
                    i.putExtra(MyConstants.DISC, newPost.getDisc());
                    i.putExtra(MyConstants.KEY, newPost.getKey());
                    i.putExtra(MyConstants.UID, newPost.getUid());
                    i.putExtra(MyConstants.TIME, newPost.getTime());
                    i.putExtra(MyConstants.CAT, newPost.getCat());
                    i.putExtra(MyConstants.EDIT_STATE, true);
                    i.putExtra(MyConstants.TOTAL_VIEWS, newPost.getTotal_views());
                    context.startActivity(i);

            });



        }

        @Override
        public void onClick(View v) {
            NewPost newPost = arrayPost.get(getAdapterPosition());
            dbManager.updateTotalViews(newPost);
            Intent i = new Intent(context, ShowLayoutActivity.class);
            i.putExtra(MyConstants.IMAGE_ID, newPost.getImageId());
            i.putExtra(MyConstants.TITLE, newPost.getTitle());
            i.putExtra(MyConstants.CHANGE, newPost.getChange());
            i.putExtra(MyConstants.TEL, newPost.getTel());
            i.putExtra(MyConstants.DISC, newPost.getDisc());
            context.startActivity(i);
            onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }

    private void deleteDialog(final NewPost newPost, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.delete_message);
        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteItem(newPost);
                    arrayPost.remove(position);
                    notifyItemRemoved(position);
            }
        });
        builder.show();


    }


    public  interface OnItemClickCustom{
        void onItemSelected(int position);
    }

    public void updateAdapter(List<NewPost> listData){
        arrayPost.clear();
        arrayPost.addAll(listData);
        notifyDataSetChanged();
    }

    public void  setDbManager(DbManager dbManager)
    {
        this.dbManager = dbManager;
    }



}
