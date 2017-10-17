package com.example.elvis.carleaseapp;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.sql.*;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

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
                    new BackEndTask(MainActivity.this, postList.size(), postList.size() + 2).execute();
                }
            }
        });
    }


    public void showLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void post(View view){
        Intent myIntent = new Intent(MainActivity.this, PostFormActivity.class);
        startActivity(myIntent);
    }



    /*************** code for testing displaying an image ***************/

    /**
     * button to test getting image from database before gallery
     * code is ready
     */
    public void testImage(View view) {
        /* test inserting image to database */

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                insertImage();
//            }
//        }).start();


        /* test getting image from database */
        new DisplayImageTask().execute();
    }


    /**
     * Gets a blob from database and display the image on UI
     */
    private class DisplayImageTask extends AsyncTask<Void, Void, Boolean> {
        byte[] imgBytes;

        @Override
        protected Boolean doInBackground(Void... params) {
            List<byte[]> imgList = BackEnd.getImg();
            imgBytes = imgList.get(0);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Bitmap b = Utils.byteArrayToImage(imgBytes);
                ImageView testImg = (ImageView) findViewById(R.id.testImg);
                testImg.setImageBitmap(b);
            }
        }
    }
    

}
