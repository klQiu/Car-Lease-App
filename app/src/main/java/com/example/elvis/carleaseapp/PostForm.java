package com.example.elvis.carleaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PostForm extends AppCompatActivity {
    private  static final String TAG = PostForm.class.getSimpleName();
    String rentTime= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);
        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        String[] celebrities = {
                "",
                "10 days",
                "one month",
                "three months",
                "half year",
                "one year",
                "over one year",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, celebrities);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner.getSelectedItemPosition();
                        if (position != 0)
                            Toast.makeText(getApplicationContext(),"You have selected "+celebrities[+position],Toast.LENGTH_LONG).show();
                            rentTime = spinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }

                }
        );
        final Button button = (Button)findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText)findViewById(R.id.editTitle);
                String title = edit.getText().toString();
                Post post = new Post(0, title);
                edit = (EditText)findViewById(R.id.editYear);
                int year = Integer.parseInt(edit.getText().toString());
                post.setYear(year);
                edit = (EditText)findViewById(R.id.editBrand);
                String brand = edit.getText().toString();
                post.setBrand(brand);
                edit = (EditText)findViewById(R.id.editColour);
                String colour = edit.getText().toString();
                post.setColour(colour);
                edit = (EditText)findViewById(R.id.editMilage);
                int milage = Integer.parseInt(edit.getText().toString());
                post.setMilage(milage);
                edit = (EditText)findViewById(R.id.editPrice);
                int price = Integer.parseInt(edit.getText().toString());
                post.setPrice(price);
                edit = (EditText)findViewById(R.id.editEmail);
                String email = edit.getText().toString();
                post.setEmail(email);
                edit = (EditText)findViewById(R.id.editTelephone);
                String telephone = edit.getText().toString();
                post.setTelephone(telephone);
                post.setRentTime(rentTime);
                Log.v(TAG, "renttime");
                Log.v(TAG, rentTime);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        BackEnd.addPost(post);
                    }
                }).start();
                Context context = getApplicationContext();
                CharSequence text = "Posted!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                Intent myIntent = new Intent(PostForm.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
