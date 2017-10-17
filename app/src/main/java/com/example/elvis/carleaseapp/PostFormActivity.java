package com.example.elvis.carleaseapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PostFormActivity extends AppCompatActivity {
    private  static final String TAG = PostFormActivity.class.getSimpleName();
    String rentTime= "";
    ImageView carImage;
    byte[] imgBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);
        carImage = (ImageView) findViewById(R.id.car_image);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
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
                //todo change userid to real owner's id after login is done
                Post post = new Post(0, title);

                /* prepare a new post to add to database */
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
                Log.v(TAG, rentTime);
                post.setImgBytes(imgBytes);

                //todo change this using async task
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        BackEnd.addPost(post);
                    }
                }).start();

                Toast.makeText(getApplicationContext(), "Posted!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(PostFormActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void selectFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            /* startActivityForResult passes in the uri of the image selected */
            Uri imageUri = data.getData();
            Bitmap carBitmap;
            try {
                carBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                imgBytes = Utils.imgToByteArray(carBitmap);  // to be sent to database
                Log.v(TAG, "image bytes done");
                //carImage.setImageBitmap(carBitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "error occurred during image selection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            Glide.with(getApplicationContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(carImage);    //use Glide library for efficient bitmap using
                                        //prevents out of memory error


        }
    }
}
