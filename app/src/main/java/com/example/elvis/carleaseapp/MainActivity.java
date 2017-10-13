package com.example.elvis.carleaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Post> postList = new ArrayList<>();
        Post p1 = new Post(0, 0, "Post1");
        p1.setColour("black");
        p1.setYear(1997);
        p1.setPrice(2000);

        Post p2 = new Post(1, 0, "Post2");
        p2.setColour("white");
        p2.setYear(2003);
        p2.setPrice(9000);

        Post p3 = new Post(2, 0, "Post3");
        p3.setColour("grey");
        p3.setYear(1987);
        p3.setPrice(5000);

        Post p4 = new Post(0, 0, "Post4");
        p4.setColour("idk");
        p4.setYear(1907);
        p4.setPrice(2900);
        postList.add(p1);
        postList.add(p2);
        postList.add(p3);
        postList.add(p4);

        /*--------- setting up recycler view --------*/
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.post_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PostListAdapter postListAdapter = new PostListAdapter(postList);
        recyclerView.setAdapter(postListAdapter);

    }
}
