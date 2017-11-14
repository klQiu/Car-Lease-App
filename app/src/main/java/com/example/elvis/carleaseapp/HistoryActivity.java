package com.example.elvis.carleaseapp;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.postList = new ArrayList<>();
        postList.add(new Post(12, "hehe"));
        postList.add(new Post(13, "haha"));
        postList.add(new Post(15, "kkkk"));

        /*--------- setting up recycler view --------*/
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        gridLayoutManager = new GridLayoutManager(this, 2); //span count = 4;
        recyclerView.setLayoutManager(gridLayoutManager);

        this.postListAdapter = new PostHistoryAdapter(postList);
        recyclerView.setAdapter(postListAdapter);

    }
}
