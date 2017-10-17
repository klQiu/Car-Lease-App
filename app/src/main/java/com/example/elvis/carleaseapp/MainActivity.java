package com.example.elvis.carleaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    PostListAdapter postListAdapter;
    List<Post> postList;
    private static final int SCROLL_DOWN = 1;
    private static final int INITIAL_LIST_SIZE = 5;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.postList = new ArrayList<>();

        /*--------- setting up recycler view --------*/
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.post_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.postListAdapter = new PostListAdapter(postList);
        recyclerView.setAdapter(postListAdapter);
        new BackEndTask(this, 0, INITIAL_LIST_SIZE).execute();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /* test if user scrolled to the end of list */
                if(!recyclerView.canScrollVertically(SCROLL_DOWN)) {
                    //Log.v(TAG, "entering async task; postList size: " + postList.size());
                    new BackEndTask(MainActivity.this, postList.size(), postList.size() + 2).execute();
                }
            }
        });
    }
}
