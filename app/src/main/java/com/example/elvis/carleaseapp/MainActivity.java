package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private static final int ITEMS_TO_LOAD = 2;
    private String filter = "postTime";
    private String order = "DESC";
    private static final String TAG = MainActivity.class.getSimpleName();

    private String location = ""; // the location where the user wants to lease a car

    private RecyclerView.OnScrollListener mOnScrollListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean scrollListenerEnabled = true;

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "in on stop, saving user to pref");
        Current.saveUserToPref(Current.getCurUser(), this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        initStatusBar();
        /*--------- setting up recycler view --------*/
        this.postList = new ArrayList<>();

        RecyclerView recyclerView;
        recyclerView = (RecyclerView) findViewById(R.id.postRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.postListAdapter = new PostListMainAdapter(postList, this);

        recyclerView.setAdapter(postListAdapter);

        new DisplayListTask(0, INITIAL_LIST_SIZE).execute();

        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /* test if user scrolled to the end of list */
                if (!recyclerView.canScrollVertically(SCROLL_DOWN) && scrollListenerEnabled) {
                    Log.v(TAG, "loading more items... post list size is " + postList.size());
                    new DisplayListTask(postList.size(), postList.size() + ITEMS_TO_LOAD).execute();
                }
            }
        };

        recyclerView.addOnScrollListener(mOnScrollListener);

        /*--------- setting up autocomplete fragment --------*/
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.search_autocomplete_fragment);

        // listener for selecting current location
        autocompleteFragment.setHint(getText(R.string.autocomplete_search));
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
                // Disable the onScrollListener to prevent it triggering
                // when "clear" is clicked
                // Re-enable it when new list populates the recycler view
                scrollListenerEnabled = false;

                autocompleteFragment.setText("");
                autocompleteFragment.setHint(getText(R.string.autocomplete_search));
                // clear the list
                location = "";
                postList = new ArrayList<>();
                postListAdapter.updateInnerList(postList);
                new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
            }
        });

        /*--------- setting up spinner --------*/
        Spinner characteristicSpinner = (Spinner) findViewById(R.id.filterSpinner);

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

        initRefreshLayout();

        /*--------------- check if the user has logged in before ------------*/
        //check if user has logged in before
        User savedUser = Current.retrieveUserFromPref(this);
        if(savedUser != null) {
            Log.v(TAG, "user has signed in before");
            Current.addCurUser(savedUser);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void initRefreshLayout() {
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        new ChangeListUponFilterTask(0, INITIAL_LIST_SIZE).execute();
                    }
                }
        );
    }

    private void initStatusBar() {
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
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
            Log.v(TAG, "location is: " + location);
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            List<Post> newList = BackEnd.filterPosts(start_num, end_num, filter, order, location);
            if(newList.size() > 0) {
                Log.v(TAG, "display list task triggered : new list size: " + newList.size());
                postList.addAll(newList);
                for(Post post: postList) {
                    Log.v(TAG, "post list item: " + post.getBrand());
                }
                return true;
            }
            else return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                postListAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), R.string.new_post_loaded, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), R.string.end_of_list, Toast.LENGTH_SHORT).show();
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
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(!scrollListenerEnabled)
                scrollListenerEnabled = true;
        }
    }


}
