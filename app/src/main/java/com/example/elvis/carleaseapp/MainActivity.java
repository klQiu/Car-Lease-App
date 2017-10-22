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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    PostListAdapter postListAdapter;
    List<Post> postList;
    private static final int SCROLL_DOWN = 1;
    private static final int SCROLL_UP = -1;
    private static final int INITIAL_LIST_SIZE = 5;
    String filter= "postTime";
    String Order= "DESC";
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
                else if(!recyclerView.canScrollVertically(SCROLL_UP)) {
                    new BackEndTask(MainActivity.this, 0, 0).execute();
                }
            }
        });

        Spinner spinner1 = (Spinner) findViewById(R.id.filterSpinner);
        List<String> list1 = new ArrayList<String>();
        String[] celebrities = {
                "",
                "Price",
                "Milage",
                "postTime",
                "Year"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, celebrities);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner1.getSelectedItemPosition();
                        if (position != 0)
                            Toast.makeText(getApplicationContext(),"You have selected "+celebrities[+position],Toast.LENGTH_LONG).show();
                        filter = spinner1.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }

                }
        );

        Spinner spinner2 = (Spinner) findViewById(R.id.orderSpinner);
        List<String> list2 = new ArrayList<String>();
        String[] orders = {
                "",
                "ASC",
                "DESC",
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, orders);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner2.getSelectedItemPosition();
                        if (position != 0)
                            Toast.makeText(getApplicationContext(),"You have selected "+orders[+position],Toast.LENGTH_LONG).show();
                        Order = spinner2.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }

                }
        );



    }



    public void showLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void post(View view){
        Intent myIntent = new Intent(MainActivity.this, PostFormActivity.class);
        startActivity(myIntent);
    }

}
