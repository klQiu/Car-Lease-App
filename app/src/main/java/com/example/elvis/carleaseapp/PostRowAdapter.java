package com.example.elvis.carleaseapp;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by LucyZhao on 2017/11/20.
 * Source:
 * https://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView
 */

public class PostRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = PostRowAdapter.class.getSimpleName();
    // The items to display in your RecyclerView
    private List<Object> rowItems;

    private static final int POST = 0, IMAGE = 1; //view type encodings
    private static final int POST_OBJ_POS = 1, IMAGE_OBJ_POS = 0;

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
        Log.v(TAG, "configuring post id: " + ((Post) rowItems.get(position)).getPostId());

        holder.location.setText(((Post) rowItems.get(position)).getTitle());

        String postYear = "";
        if (((Post) rowItems.get(position)).getYear() != 0) {
            postYear = Integer.toString(((Post) rowItems.get(position)).getYear());
        }
        holder.carYear.setText(postYear);

        String postMileage = "";
        if (((Post) rowItems.get(position)).getMileage() != 0) {
            postMileage = Integer.toString(((Post) rowItems.get(position)).getMileage());
        }
        holder.carMileage.setText(postMileage);
        holder.postTime.setText(((Post) rowItems.get(position)).getPostTime());
        holder.email.setText(((Post) rowItems.get(position)).getEmail());
        holder.telephone.setText(((Post) rowItems.get(position)).getTelephone());
    }

    private void configureImgViewHolder(ImgViewHolder holder, int position) {
        Log.v(TAG, "configuring image");
        Glide.with(holder.img.getContext())
                .load((byte[]) rowItems.get(position))
                .centerCrop()
                .into(holder.img);

        /* We know that item 1 in rowItems is the post object,
           item 0 is image bytes
         */
        Post post = (Post) rowItems.get(POST_OBJ_POS);
        holder.carBrand.setText(post.getBrand());
        holder.rentTime.setText(post.getRentTime());

        String postPrice = "";
        if (post.getPrice() != 0) {
            postPrice = Integer.toString(post.getPrice());
        }
        holder.carPrice.setText(postPrice);

        User user = Current.getCurUser();
        if(user != null) {
            if(user.getStarredPostIds().contains(Integer.toString(post.getPostId()))) {
                holder.star.setVisibility(View.VISIBLE);
            }
            else {
                holder.star.setVisibility(View.INVISIBLE);
            }
        }
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


    private class ImgViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView carBrand;
        private TextView rentTime;
        private TextView carPrice;
        private ImageView star;


        private ImgViewHolder(View itemView) {
            super(itemView);
            this.img = (ImageView) itemView.findViewById(R.id.car_img);
            this.carBrand = (TextView) itemView.findViewById(R.id.car_brand);
            this.carPrice = (TextView) itemView.findViewById(R.id.car_price);
            this.rentTime = (TextView) itemView.findViewById(R.id.car_rentTime);

            /*------------ setting up stars --------------*/
            this.star = (ImageView) itemView.findViewById(R.id.star_img);
            RelativeLayout starLayout = (RelativeLayout) itemView.findViewById(R.id.star_layout);
            starLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(star.getVisibility() == View.VISIBLE) {
                        removeStarredPost();
                    }
                    else {
                        starAPost();
                    }
                }
            });

            /* Check if user is logged in to determine whether to show star */
            User user = Current.getCurUser();
            if(user != null) {
                starLayout.setVisibility(View.VISIBLE);
            }
        }

        private void removeStarredPost() {
            Post post = (Post) rowItems.get(POST_OBJ_POS);
            if(Current.getCurUser() != null)
                new RemoveStarTask(Current.getCurUser(), post).execute();
        }

        private void starAPost() {
            Post post = (Post) rowItems.get(POST_OBJ_POS);
            if(Current.getCurUser() != null)
                new StarringTask(Current.getCurUser(), post).execute();
        }

        private class StarringTask extends AsyncTask<Void, Void, Boolean> {
            User user;
            Post post;
            StarringTask(User user, Post post) {
                this.user = user;
                this.post = post;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                BackEnd.star(user, post);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result) {
                    star.setVisibility(View.VISIBLE);
                    Current.getCurUser().getStarredPostIds().add(Integer.toString(post.getPostId()));
                }
            }
        }

        private class RemoveStarTask extends AsyncTask<Void, Void, Boolean> {
            User user;
            Post post;
            RemoveStarTask(User user, Post post) {
                this.user = user;
                this.post = post;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                BackEnd.unStar(user, post);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if(result) {
                    star.setVisibility(View.INVISIBLE);
                    Current.getCurUser().getStarredPostIds().remove(Integer.toString(post.getPostId()));
                }
            }
        }

    }

    private class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView location;
        //private TextView carColor;
        private TextView carYear;
        private TextView carMileage;
        private TextView postTime;
        private TextView telephone;
        private TextView email;


        private PostViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.location = (TextView) itemLayoutView.findViewById(R.id.car_location);
            //this.carColor = (TextView) itemLayoutView.findViewById(R.id.car_color);
            this.carYear = (TextView) itemLayoutView.findViewById(R.id.car_year);
            this.carMileage = (TextView) itemLayoutView.findViewById(R.id.car_mileage);
            this.postTime = (TextView) itemLayoutView.findViewById(R.id.car_postTime);
            this.telephone = (TextView) itemLayoutView.findViewById(R.id.car_telephone);
            this.email = (TextView) itemLayoutView.findViewById(R.id.car_email);
        }

    }
}
