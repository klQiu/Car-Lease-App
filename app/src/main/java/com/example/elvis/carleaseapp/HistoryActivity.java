package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private PostHistoryAdapter postListAdapter;
    private List<Post> postList;
    private GridLayoutManager gridLayoutManager;
    private static final String TAG = HistoryActivity.class.getSimpleName();
    private int mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.postList = new ArrayList<>();
        Intent mIntent = getIntent();
        mode = mIntent.getIntExtra("intVariableName", 0);
        /*--------- setting up recycler view --------*/
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        gridLayoutManager = new GridLayoutManager(this, 2); //span count = 4;
        recyclerView.setLayoutManager(gridLayoutManager);

        this.postListAdapter = new PostHistoryAdapter(postList);
        recyclerView.setAdapter(postListAdapter);
        new DisplayListTask().execute();

    }

    private class DisplayListTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            //User user = new User("hehe", "111", 0);
            User user = Current.getCurUser();
            List<Post> newList;
            if(mode == 0){
                newList = BackEnd.getHisPost(user);
                postListAdapter.setMode(mode);
            }
            else{
                newList = BackEnd.getStarPost(user);
                postListAdapter.setMode(mode);
            }
            postList.addAll(newList);
            //todo return false for database error
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                postListAdapter.notifyDataSetChanged();
            }
        }
    }

}
