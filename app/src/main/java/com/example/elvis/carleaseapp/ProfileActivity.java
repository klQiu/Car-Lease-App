package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void changePsd(View view) {
        Intent intent = new Intent(this, ChangePsdActivity.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        Current.deleteCurUser(getApplicationContext());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void viewHistory(View view) {
        Intent myIntent = new Intent(this, HistoryActivity.class);
        myIntent.putExtra("intVariableName", 0);
        startActivity(myIntent);
    }

    public void viewSaved(View view) {
        Intent myIntent = new Intent(this, HistoryActivity.class);
        myIntent.putExtra("intVariableName", 1);
        startActivity(myIntent);
    }
}
