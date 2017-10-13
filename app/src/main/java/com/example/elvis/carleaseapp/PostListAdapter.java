package com.example.elvis.carleaseapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LucyZhao on 2017/10/12.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder> {
    private List<Post> postList;

    public PostListAdapter(List<Post> postList) {
        this.postList = postList;
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
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        holder.title.setText(postList.get(position).getTitle());
        holder.carColor.setText(postList.get(position).getColour());
        holder.carYear.setText(Integer.toString(postList.get(position).getYear()));
        holder.carPrice.setText(Integer.toString(postList.get(position).getPrice()));
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
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView carColor;
        private TextView carYear;
        private TextView carPrice;

        private ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.title = (TextView) itemLayoutView.findViewById(R.id.car_title);
            this.carColor = (TextView) itemLayoutView.findViewById(R.id.car_color);
            this.carYear = (TextView) itemLayoutView.findViewById(R.id.car_year);
            this.carPrice = (TextView) itemLayoutView.findViewById(R.id.car_price);
        }

        @Override
        public void onClick(View view) {
            //
        }

    }
}
