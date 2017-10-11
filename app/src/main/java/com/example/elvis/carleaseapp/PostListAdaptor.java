package com.example.elvis.carleaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elvis on 2017/10/10.
 */

public class PostListAdaptor  extends RecyclerView.Adapter<PostListAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {


    private static ArrayList<Post> postList;
    private Context associatedActivityContext;

    public PostListAdapter(ArrayList<Post> post_List, Context associatedActivityContext) {
        postList = post_List;
        this.associatedActivityContext = associatedActivityContext;
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    @Override
    public PostListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_list_item_title, null);

        return new ViewHolder(itemLayoutView);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.title.setText(postList.get(position).getTitle());

        //prevents out of memory error
    }

    /**
     * Replace the inner list with a new list
     *
     * @param newFoodList
     */
    public void updateInnerList(ArrayList<Post> newFoodList) {
        postList = newFoodList;
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemMove(RecyclerView.ViewHolder holder, int fromPosition, int toPosition) {
    }

    @Override
    public void onItemDismiss(final int position) {
        /*  1.delete the picture associated */
        String uriString = postList.get(position).getImageUriString();
        Utils.deleteImageOnDevice(Uri.parse(uriString), associatedActivityContext);

        /*  2.remove the ingredient and procedure files from internal
            storage
        */
        if (!Utils.deleteFoodDir(associatedActivityContext, foodList.get(position).getId())) {
            Toast.makeText(associatedActivityContext, R.string.delete_failed_error, Toast.LENGTH_LONG).show();
        }
        /*
            3.remove the swiped item from the adapter and its internal
            list
        */
        foodList.remove(position);
        notifyItemRemoved(position);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public ImageView foodImage;
        private final Context context;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title = (TextView) itemLayoutView.findViewById(R.id.title);
            context = itemLayoutView.getContext();
            itemLayoutView.setClickable(true);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, RecipePageActivity.class);

            intent.putExtra(Utils.EXTRA_CLICKING_POSITION, this.getAdapterPosition());
            intent.putExtra(Utils.EXTRA_FOOD_OBJECT, foodList.get(this.getAdapterPosition()));

            context.startActivity(intent);
        }

    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

}
