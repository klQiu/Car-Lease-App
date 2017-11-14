package com.example.elvis.carleaseapp;

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


public class EditPostActivity extends AppCompatActivity {
    private  static final String TAG = PostFormActivity.class.getSimpleName();
    private String rentTime = "";
    private ImageView carImage;
    private byte[] imgBytes = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        Post post = (Post)bundle.getSerializable("post_to_edit");
        Log.v(TAG, Integer.toString(post.getPostId()));
        carImage = (ImageView) findViewById(R.id.Updatecar_image);

        EditText editTitle = (EditText)findViewById(R.id.UpdateTitle);
        editTitle.setText(post.getTitle(), TextView.BufferType.EDITABLE);
        EditText editYear = (EditText)findViewById(R.id.UpdateYear);
        editYear.setText(Integer.toString(post.getYear()), TextView.BufferType.EDITABLE);
        EditText editBrand = (EditText)findViewById(R.id.UpdateBrand);
        editBrand.setText(post.getBrand(), TextView.BufferType.EDITABLE);
        EditText editColour = (EditText)findViewById(R.id.UpdateColour);
        editColour.setText(post.getColour(), TextView.BufferType.EDITABLE);
        EditText editMileage = (EditText)findViewById(R.id.UpdateMileage);
        editMileage.setText(Integer.toString(post.getMileage()), TextView.BufferType.EDITABLE);
        EditText editPrice = (EditText)findViewById(R.id.UpdatePrice);
        editPrice.setText(Integer.toString(post.getPrice()), TextView.BufferType.EDITABLE);
        EditText editTelephone = (EditText)findViewById(R.id.UpdateTelephone);
        editTelephone.setText(post.getTelephone(), TextView.BufferType.EDITABLE);
        EditText editEmail = (EditText)findViewById(R.id.UpdateEmail);
        editEmail.setText(post.getEmail(), TextView.BufferType.EDITABLE);

        Spinner spinner = (Spinner) findViewById(R.id.Updatespinner);
        String[] timeFilter = {
                "Please select how long to rent",
                "10 days",
                "one month",
                "three months",
                "half year",
                "one year",
                "over one year",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timeFilter);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = spinner.getSelectedItemPosition();
                        if (position != 0)
                            Toast.makeText(getApplicationContext(),"You have selected "+timeFilter[+position],Toast.LENGTH_LONG).show();
                        rentTime = spinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        rentTime = "";
                    }

                }
        );


        final Button deleteButton = (Button)findViewById(R.id.Delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        BackEnd.deletePost(post);;
                    }
                }).start();
                Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(EditPostActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });

        final Button button = (Button)findViewById(R.id.Update_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText edit = (EditText)findViewById(R.id.UpdateTitle);
                String title = edit.getText().toString();
                int userId = Current.getCurUserID();
                Post post1 = new Post(userId, title);

                /* prepare a new post to add to database */
                edit = (EditText)findViewById(R.id.UpdateYear);
                int year = Integer.parseInt(edit.getText().toString());
                post1.setYear(year);
                edit = (EditText)findViewById(R.id.UpdateBrand);
                String brand = edit.getText().toString();
                post1.setBrand(brand);
                edit = (EditText)findViewById(R.id.UpdateColour);
                String colour = edit.getText().toString();
                post1.setColour(colour);
                edit = (EditText)findViewById(R.id.UpdateMileage);
                int mileage = Integer.parseInt(edit.getText().toString());
                post1.setMileage(mileage);
                edit = (EditText)findViewById(R.id.UpdatePrice);
                int price = Integer.parseInt(edit.getText().toString());
                post1.setPrice(price);
                edit = (EditText)findViewById(R.id.UpdateEmail);
                String email = edit.getText().toString();
                post1.setEmail(email);
                edit = (EditText)findViewById(R.id.UpdateTelephone);
                String telephone = edit.getText().toString();
                post1.setTelephone(telephone);
                if(rentTime != ""  &&  rentTime !="Please select how long to rent"){
                    post1.setRentTime(rentTime);
                }
                else{
                    post1.setRentTime(post.getRentTime());
                }
                post1.setPostId(post.getPostId());
                post1.setPostTime(post.getPostTime());
                Log.v(TAG, rentTime);
                if(imgBytes != null){
                    post1.setImgBytes(imgBytes);
                }
                else{
                    imgBytes = post.getImgBytes();
                    post1.setImgBytes(imgBytes);
                }


                //todo change this using async task
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        BackEnd.updatePost(post1);
                    }
                }).start();

                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(EditPostActivity.this, MainActivity.class);
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
