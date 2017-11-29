package com.example.elvis.carleaseapp;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LucyZhao on 2017/10/12.
 * Source:
 * https://github.com/hardworker93/carousels/blob/master/src/com/example/carousels/RowRecyclerAdapter.java
 */

public class PostListMainAdapter extends RecyclerView.Adapter<PostListMainAdapter.ViewHolder> {
    private List<Post> postList;
    private Context mContext;

    public PostListMainAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_post_item, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<Object> rowItems = new ArrayList<>();
        rowItems.add(postList.get(position).getImgBytes());
        rowItems.add(postList.get(position));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.mRecyclerViewRow.setLayoutManager(layoutManager);
        holder.mRecyclerViewRow.setHasFixedSize(true);
        PostRowAdapter postRowAdapter = new PostRowAdapter(rowItems);
        holder.mRecyclerViewRow.setAdapter(postRowAdapter);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    /**
     * Replace the inner list with a new list
     */
    public void updateInnerList(List<Post> newPostList) {
        this.postList = newPostList;
        this.notifyDataSetChanged();
    }

    // inner class to hold a reference to each item of RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerViewRow;

        private ViewHolder(View itemView) {
            super(itemView);
            mRecyclerViewRow = (RecyclerView) itemView.findViewById(R.id.row_recycler);
        }

    }
}
