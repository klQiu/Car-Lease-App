package com.example.elvis.carleaseapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by LucyZhao on 2017/10/12.
 */

public class PostHistoryAdapter extends RecyclerView.Adapter<PostHistoryAdapter.ViewHolder> {
    private List<Post> postList;

    public PostHistoryAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_history_post_item, parent, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        holder.title.setText(postList.get(position).getTitle());
        holder.carBrand.setText(postList.get(position).getBrand());

        //Bitmap b = Utils.byteArrayToImage(postList.get(position).getImgBytes());
        Glide.with(holder.carImg.getContext())
                    .load(postList.get(position).getImgBytes())
                    .centerCrop()
                    .into(holder.carImg);    //use Glide library for efficient bitmap using
                                             //prevents out of memory error

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    // inner class to hold a reference to each item of RecyclerView
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private TextView carBrand;
        private ImageView carImg;


        private ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.title = (TextView) itemLayoutView.findViewById(R.id.txtCarTitle);
            this.carBrand = (TextView) itemLayoutView.findViewById(R.id.txtCarBrand);
            this.carImg = (ImageView) itemLayoutView.findViewById(R.id.imgCar);
        }

        @Override
        public void onClick(View view) {
            //todo
        }

    }
}
