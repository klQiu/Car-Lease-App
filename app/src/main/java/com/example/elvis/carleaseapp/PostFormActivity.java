package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;


public class PostFormActivity extends AppCompatActivity {
    private  static final String TAG = PostFormActivity.class.getSimpleName();
    private String rentTime = "";
    private ImageView carImage;
    private byte[] imgBytes = null;
    private static final int IMG_SIZE_LIMIT = 1000;    // in bytes
    private Button submitBtn;
    private String placeSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);

        carImage = (ImageView) findViewById(R.id.car_image);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] timeFilter = {
                "Please select how long to rent",
                "10 days",
                "one month",
                "three months",
                "half year",
                "one year",
                "over one year",
        };

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.editTitle);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                placeSelected = place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO:Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timeFilter);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner.getSelectedItemPosition();
                        if (position != 0) {
                            Toast.makeText(getApplicationContext(), "You have selected " + timeFilter[+position], Toast.LENGTH_SHORT).show();
                            rentTime = spinner.getSelectedItem().toString();
                        }
                        else
                            rentTime = "";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        rentTime = "";
                    }

                }
        );





        submitBtn = (Button)findViewById(R.id.submit_button);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!EMAIL_ADDRESS_PATTERN.matcher(((EditText)findViewById(R.id.editEmail)).getText().toString()).matches()){
                    Toast.makeText(getApplicationContext(), "Please enter an email address.", Toast.LENGTH_LONG).show();
                }
                else if( ((EditText)findViewById(R.id.editYear)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editBrand)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editColour)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editMileage)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editPrice)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editEmail)).getText().toString().trim().length() == 0 ||
                        ((EditText)findViewById(R.id.editTelephone)).getText().toString().trim().length() == 0 ||
                        rentTime.equals("")  || imgBytes == null || placeSelected == ""){
                    Log.v(TAG, rentTime);
                    Toast.makeText(getApplicationContext(), "You should fill in all information.", Toast.LENGTH_LONG).show();
                }
                else{
                    submit();
                }
            }
        });
    }

    public void submit(){
//        EditText edit = (EditText)findViewById(R.id.editTitle);
//        String title = edit.getText().toString();
        int userId = Current.getCurUserID();
        Post post = new Post(userId, placeSelected);

                /* prepare a new post to add to database */
        EditText edit = (EditText)findViewById(R.id.editYear);
        int year = Integer.parseInt(edit.getText().toString());
        post.setYear(year);
        edit = (EditText)findViewById(R.id.editBrand);
        String brand = edit.getText().toString();
        post.setBrand(brand);
        edit = (EditText)findViewById(R.id.editColour);
        String colour = edit.getText().toString();
        post.setColour(colour);
        edit = (EditText)findViewById(R.id.editMileage);
        int mileage = Integer.parseInt(edit.getText().toString());
        post.setMileage(mileage);
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

    public void selectFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private void displayImage(byte[] imgBytes, Uri imageUri) {
//        if(imgBytes.length / IMG_SIZE_LIMIT > 500) {
//            Toast.makeText(this, "Image too large, change to another one", Toast.LENGTH_LONG).show();
//            submitBtn.setEnabled(false);
//        }
//        else {
            this.imgBytes = imgBytes;
            Glide.with(getApplicationContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(carImage);
//        }
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
                byte[] imgBuffer;
                imgBuffer = Utils.imgToByteArray(carBitmap);  // to be sent to database
                Log.v(TAG, "image bytes done");
                //carImage.setImageBitmap(carBitmap);
                Log.v(TAG, "image bytes done, img bytes: " + imgBuffer.length / 1000 + " KB");
                if(imgBuffer.length / IMG_SIZE_LIMIT > 500) {
                    Toast.makeText(this, "Image too large, change to another one", Toast.LENGTH_LONG).show();
                }
                else{
                    displayImage(imgBuffer, imageUri);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "error occurred during image selection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }



        }
    }
}
