package com.example.android.tugasbesarteknikmaterial.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.tugasbesarteknikmaterial.Models.Post;
import com.example.android.tugasbesarteknikmaterial.R;

import java.util.Comparator;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvtitle.setText(mData.get(position).getTitle());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgpostprofile);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvtitle;
        ImageView imgPost;
        ImageView imgpostprofile;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvtitle = itemView.findViewById(R.id.row_post_title);
            imgPost = itemView.findViewById(R.id.row_post_image);
            imgpostprofile = itemView.findViewById(R.id.row_post_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postdetail = new Intent(mContext, com.example.android.tugasbesarteknikmaterial.Activities.postdetail.class);
                    int position = getAdapterPosition();

                    postdetail.putExtra("title",mData.get(position).getTitle());
                    postdetail.putExtra("postImage",mData.get(position).getPicture());
                    postdetail.putExtra("description",mData.get(position).getDescription());
                    postdetail.putExtra("postKey",mData.get(position).getPostKey());
                    postdetail.putExtra("userPhoto",mData.get(position).getUserPhoto());

                    long timestamp = (long) mData.get(position).getTimestamp();
                    postdetail.putExtra("postDate",timestamp);
                    mContext.startActivity(postdetail);
                }
            });
        }
    }
}
