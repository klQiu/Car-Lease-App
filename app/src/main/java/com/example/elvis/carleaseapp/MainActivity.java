package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private PostListAdapter postListAdapter;
    private List<Post> postList;
    private static final int SCROLL_DOWN = 1;
    private static final int SCROLL_UP = -1;
    private static final int INITIAL_LIST_SIZE = 5;
    private String filter = "postTime";
    private String order = "DESC";
    private static final String TAG = MainActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        this.postList = new ArrayList<>();

        /*--------- setting up recycler view --------*/
        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.postRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.postListAdapter = new PostListAdapter(postList);
        recyclerView.setAdapter(postListAdapter);
        new DisplayListTask(0, INITIAL_LIST_SIZE).execute();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /* test if user scrolled to the end of list */
                if (!recyclerView.canScrollVertically(SCROLL_DOWN)) {
                    new DisplayListTask(postList.size(), postList.size() + 2).execute();
                } else if (!recyclerView.canScrollVertically(SCROLL_UP)) {
                    new DisplayListTask(0, 0).execute();
                }
            }
        });

        Spinner characteristicSpinner = (Spinner) findViewById(R.id.filterSpinner);
        String[] characteristicFilter = {
                "Filter",
                "postTime",
                "price",
                "mileage",
                "year"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, characteristicFilter);
        characteristicSpinner.setAdapter(adapter);
        //characteristicSpinner.setSelection(1);
        characteristicSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        Log.v(TAG, "on item selected characteristic filter");
                        int position = characteristicSpinner.getSelectedItemPosition();
                        if(position != 0) {
                            Toast.makeText(getApplicationContext(), "You have selected " + characteristicFilter[+position], Toast.LENGTH_LONG).show();
                            filter = characteristicSpinner.getSelectedItem().toString();
                            new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        Log.v(TAG, "on nothing selected characteristic spinner");
                        filter = "postTime";
                    }

                }
        );

        Spinner orderSpinner = (Spinner) findViewById(R.id.orderSpinner);
        String[] orders = {
                "Order",
                "DESC",
                "ASC"
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, orders);
        orderSpinner.setAdapter(adapter2);
        //orderSpinner.setSelection(1);
        orderSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        Log.v(TAG, "on item selected orders filter");
                        int position = orderSpinner.getSelectedItemPosition();
                        if(position != 0) {
                            Toast.makeText(getApplicationContext(), "You have selected " + orders[+position], Toast.LENGTH_LONG).show();
                            order = orderSpinner.getSelectedItem().toString();
                            new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        order = "DESC";
                    }

                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showLogin(MenuItem mi) {
        if(Current.getCurUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    public void post(View view) {
        Intent myIntent = new Intent(MainActivity.this, PostFormActivity.class);
        startActivity(myIntent);
    }

    private class DisplayListTask extends AsyncTask<Void, Void, Boolean> {
        private final int start_num;
        private final int end_num;

        //todo refactor out context, postlistadapter and postlist
        DisplayListTask(int start_num, int end_num) {
            this.start_num = start_num;
            this.end_num = end_num;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            List<Post> newList = BackEnd.filterPosts(start_num, end_num, filter, order);
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

    private class ChangeListUponFilterTask extends AsyncTask<Void, Void, Boolean> {
        private final int start_num;
        private final int end_num;

        ChangeListUponFilterTask(int start_num, int end_num) {
            this.start_num = start_num;
            this.end_num = end_num;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            postList = BackEnd.filterPosts(start_num, end_num, filter, order);
            //todo return false for database error
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                postListAdapter.updateInnerList(postList);
            }
        }
    }
}
