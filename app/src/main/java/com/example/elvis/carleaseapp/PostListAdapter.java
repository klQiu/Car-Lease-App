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
        holder.carBrand.setText(postList.get(position).getBrand());
        String postYear = "";
        String postPrice = "";
        if(postList.get(position).getYear() != 0) {
            postYear = Integer.toString(postList.get(position).getYear());
        }
        holder.carYear.setText(postYear);
        if(postList.get(position).getPrice() != 0) {
            postPrice = Integer.toString(postList.get(position).getPrice());
        }
        holder.carPrice.setText(postPrice);
        holder.postTime.setText(postList.get(position).getPostTime());
        holder.email.setText(postList.get(position).getEmail());

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
        private TextView carBrand;
        //private TextView carColor;
        private TextView carYear;
        //private TextView carMileage;
        private TextView carPrice;
        //private TextView rentTime;
        private TextView postTime;
        //private TextView telephone;
        private TextView email;
        private ImageView carImg;


        private ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.title = (TextView) itemLayoutView.findViewById(R.id.car_title);
            this.carBrand = (TextView) itemLayoutView.findViewById(R.id.car_brand);
            //this.carColor = (TextView) itemLayoutView.findViewById(R.id.car_color);
            this.carYear = (TextView) itemLayoutView.findViewById(R.id.car_year);
            //this.carMileage = (TextView) itemLayoutView.findViewById(R.id.car_mileage);
            this.carPrice = (TextView) itemLayoutView.findViewById(R.id.car_price);
            //this.rentTime = (TextView) itemLayoutView.findViewById(R.id.car_rentTime);
            this.postTime = (TextView) itemLayoutView.findViewById(R.id.car_postTime);
            //this.telephone = (TextView) itemLayoutView.findViewById(R.id.car_telephone);
            this.email = (TextView) itemLayoutView.findViewById(R.id.car_email);
            this.carImg = (ImageView) itemLayoutView.findViewById(R.id.car_img);
        }

        @Override
        public void onClick(View view) {
            //todo
        }

    }
}
