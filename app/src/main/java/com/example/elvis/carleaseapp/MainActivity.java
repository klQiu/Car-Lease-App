package com.example.elvis.carleaseapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    public void post(View view){
        Intent myIntent = new Intent(MainActivity.this, PostForm.class);
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

    private void insertImage() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            PreparedStatement st =  myConn.prepareStatement("insert into TestImage values (NULL,?)");
            //todo
            //byte[] buffer = "some data".getBytes();
            byte[] buffer = imgToByteArray();
            Blob blob = myConn.createBlob();
            blob.setBytes(1, buffer);
            st.setBlob(1, blob);
            blob.free(); //??needed??

            st.execute();
            st.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    /**
     * uses a test image
     * todo change this
     * @return
     */
    private byte[] imgToByteArray() {
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.test_img);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap byteArrayToImage(byte[] imgBytes) {
        Bitmap b = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length); //Convert bytearray to bitmap
        return b;
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
                Bitmap b = byteArrayToImage(imgBytes);
                ImageView testImg = (ImageView) findViewById(R.id.testImg);
                testImg.setImageBitmap(b);
            }
        }
    }
    
}
