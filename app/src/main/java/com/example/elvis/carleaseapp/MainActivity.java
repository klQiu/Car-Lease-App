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

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private PostListMainAdapter postListAdapter;
    private List<Post> postList;
    private static final int SCROLL_DOWN = 1;
    private static final int INITIAL_LIST_SIZE = 5;
    private String filter = "postTime";
    private String order = "DESC";
    private static final String TAG = MainActivity.class.getSimpleName();

    private String location = ""; // the location where teh user wants to lease a car
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        /*--------- setting up autocomplete fragment --------*/
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.search_autocomplete_fragment);

        // listener for selecting current location
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                location = place.getName().toString();
                new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
            }

            @Override
            public void onError(Status status) {
                location = "";
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // listener for deselecting current location
        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompleteFragment.setText("");
                autocompleteFragment.setHint(getText(R.string.autocomplete_search));

                // clear the list
                location = "";
                new DisplayListTask(0, INITIAL_LIST_SIZE).execute();
            }
        });


        /*--------- setting up recycler view --------*/
        this.postList = new ArrayList<>();

        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.postRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.postListAdapter = new PostListMainAdapter(postList, this);

        recyclerView.setAdapter(postListAdapter);

        new DisplayListTask(0, INITIAL_LIST_SIZE).execute();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /* test if user scrolled to the end of list */
                if (!recyclerView.canScrollVertically(SCROLL_DOWN)) {
                    new DisplayListTask(postList.size(), postList.size() + 2).execute();
                }
            }
        });

        Spinner characteristicSpinner = (Spinner) findViewById(R.id.filterSpinner);
//        String[] characteristicFilter = {
//                "Filter",
//                "postTime",
//                "price",
//                "mileage",
//                "year"
//        };

        String[] characteristicFilter = {
                "Sort by",
                "newest posts",
                "price: high to low",
                "price: low to high",
                "mileage: low to high",
                "year of manufacture: most to least recent"
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
                            String filterSelected = characteristicSpinner.getSelectedItem().toString();
                            if(filterSelected.equals("newest posts"))   {
                                filter = "postTime";
                                order = "DESC";
                            }
                            else if(filterSelected.equals("price: high to low"))   {
                                filter = "price";
                                order = "DESC";
                            }
                            else if(filterSelected.equals("price: low to high"))   {
                                filter = "price";
                                order = "ASC";
                            }
                            else if(filterSelected.equals("mileage: low to high"))   {
                                filter = "mileage";
                                order = "ASC";
                            }
                            else{
                                filter = "year";
                                order = "DESC";
                            }
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

//        Spinner orderSpinner = (Spinner) findViewById(R.id.orderSpinner);
//        String[] orders = {
//                "Order",
//                "DESC",
//                "ASC"
//        };
//
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
//                this, android.R.layout.simple_spinner_item, orders);
//        orderSpinner.setAdapter(adapter2);
//        //orderSpinner.setSelection(1);
//        orderSpinner.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                               int arg2, long arg3) {
//                        Log.v(TAG, "on item selected orders filter");
//                        int position = orderSpinner.getSelectedItemPosition();
//                        if(position != 0) {
//                            Toast.makeText(getApplicationContext(), "You have selected " + orders[+position], Toast.LENGTH_LONG).show();
//                            order = orderSpinner.getSelectedItem().toString();
//                            new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                        order = "DESC";
//                    }
//
//                }
//        );
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

        DisplayListTask(int start_num, int end_num) {
            this.start_num = start_num;
            this.end_num = end_num;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            List<Post> newList = BackEnd.filterPosts(start_num, end_num, filter, order, location);
            if(newList.size() > 0) {
                postList.addAll(newList);
                return true;
            }
            else return false;
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
            postList = BackEnd.filterPosts(start_num, end_num, filter, order, location);
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
