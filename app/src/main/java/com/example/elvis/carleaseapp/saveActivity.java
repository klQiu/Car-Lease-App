package com.example.elvis.carleaseapp;

import android.content.Intent;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import static com.example.elvis.carleaseapp.PostFormActivity.EMAIL_ADDRESS_PATTERN;

public class saveActivity extends AppCompatActivity {
    private  static final String TAG = PostFormActivity.class.getSimpleName();
    private ImageView carImage;
    private String updatePlaceSelected = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Post post = (Post)bundle.getSerializable("saved_post");
        Log.v(TAG, Integer.toString(post.getPostId()));
        carImage = (ImageView) findViewById(R.id.Savecar_image);
        updatePlaceSelected = post.getTitle();
        TextView location = (TextView)findViewById(R.id.SaveTitle);
        location.setText(post.getTitle());
        TextView editYear = (TextView)findViewById(R.id.SaveYear);
        editYear.setText(Integer.toString(post.getYear()));
        TextView editBrand = (TextView)findViewById(R.id.SaveBrand);
        editBrand.setText(post.getBrand());
        TextView editColour = (TextView)findViewById(R.id.SaveColour);
        editColour.setText(post.getColour());
        TextView editMileage = (TextView)findViewById(R.id.SaveMileage);
        editMileage.setText(Integer.toString(post.getMileage()));
        TextView editPrice = (TextView)findViewById(R.id.SavePrice);
        editPrice.setText(Integer.toString(post.getPrice()));
        TextView editTelephone = (TextView)findViewById(R.id.SaveTelephone);
        editTelephone.setText(post.getTelephone());
        TextView editEmail = (TextView)findViewById(R.id.SaveEmail);
        editEmail.setText(post.getEmail());
        Glide.with(this)
                .load(post.getImgBytes())
                .centerCrop()
                .into(carImage);

        TextView spinner = (TextView) findViewById(R.id.Savespinner);
        spinner.setText(post.getRentTime());


    }
}
