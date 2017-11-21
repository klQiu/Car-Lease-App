package com.example.elvis.carleaseapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by LucyZhao on 2017/11/20.
 */

public class PostRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = PostRowAdapter.class.getSimpleName();
    // The items to display in your RecyclerView
    private List<Object> rowItems;

    private static final int POST = 0, IMAGE = 1;

    public PostRowAdapter(List<Object> items) {
        this.rowItems = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POST:
                View v1 = inflater.inflate(R.layout.single_post_item_txt, parent, false);
                viewHolder = new PostViewHolder(v1);
                break;
            case IMAGE:
                View v2 = inflater.inflate(R.layout.single_post_item_img, parent, false);
                viewHolder = new ImgViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(R.layout.single_post_item_txt, parent, false);
                viewHolder = new PostViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case POST:
                PostViewHolder vh1 = (PostViewHolder) holder;
                configurePostViewHolder(vh1, position);
                break;
            case IMAGE:
                ImgViewHolder vh2 = (ImgViewHolder) holder;
                configureImgViewHolder(vh2, position);
                break;
            default:
                PostViewHolder vh = (PostViewHolder) holder;
                configurePostViewHolder(vh, position);
                break;
        }
    }

    private void configurePostViewHolder(PostViewHolder holder, int position) {
        Log.v(TAG, "configuring post id: " + ((Post)rowItems.get(position)).getPostId());
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        holder.title.setText(((Post)rowItems.get(position)).getTitle());
        holder.carBrand.setText(((Post)rowItems.get(position)).getBrand());
        String postYear = "";
        String postPrice = "";
        if(((Post)rowItems.get(position)).getYear() != 0) {
            postYear = Integer.toString(((Post)rowItems.get(position)).getYear());
        }
        holder.carYear.setText(postYear);
        if(((Post)rowItems.get(position)).getPrice() != 0) {
            postPrice = Integer.toString(((Post)rowItems.get(position)).getPrice());
        }
        holder.carPrice.setText(postPrice);
        holder.postTime.setText(((Post)rowItems.get(position)).getPostTime());
        holder.email.setText(((Post)rowItems.get(position)).getEmail());

    }

    private void configureImgViewHolder(ImgViewHolder holder, int position) {
        Log.v(TAG, "configuring image");
        Glide.with(holder.img.getContext())
                .load((byte[])rowItems.get(position))
                .centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return this.rowItems.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (rowItems.get(position) instanceof Post) {
            return POST;
        } else if (rowItems.get(position) instanceof byte[]) {
            return IMAGE;
        }
        return -1;
    }


//    static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private ViewHolder(View itemView) {
//            super(itemView);
//        }
//    }

    static class ImgViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        private ImgViewHolder(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.car_img);
        }
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

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


        private PostViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.title = (TextView) itemLayoutView.findViewById(R.id.car_location);
            this.carBrand = (TextView) itemLayoutView.findViewById(R.id.car_brand);
            //this.carColor = (TextView) itemLayoutView.findViewById(R.id.car_color);
            this.carYear = (TextView) itemLayoutView.findViewById(R.id.car_year);
            //this.carMileage = (TextView) itemLayoutView.findViewById(R.id.car_mileage);
            this.carPrice = (TextView) itemLayoutView.findViewById(R.id.car_price);
            //this.rentTime = (TextView) itemLayoutView.findViewById(R.id.car_rentTime);
            this.postTime = (TextView) itemLayoutView.findViewById(R.id.car_postTime);
            //this.telephone = (TextView) itemLayoutView.findViewById(R.id.car_telephone);
            this.email = (TextView) itemLayoutView.findViewById(R.id.car_email);
        }


    }
}
