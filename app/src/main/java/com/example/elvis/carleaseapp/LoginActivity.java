package com.example.elvis.carleaseapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.example.elvis.carleaseapp.BackEnd;

import java.util.ArrayList;
import java.util.List;

import static com.example.elvis.carleaseapp.PostFormActivity.EMAIL_ADDRESS_PATTERN;
import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {
    PopupWindow popUpWindow;
    private static final String TAG = LoginActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set title font
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "bgothm.ttf");
        TextView title = (TextView)findViewById(R.id.loginTitle);
        title.setTypeface(myTypeface);

        //adjust hint font for password
        EditText password = (EditText) findViewById(R.id.passwordText);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        //add popup window for warning msg
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View warnLayout = inflater.inflate(R.layout.warn, null);
        popUpWindow = new PopupWindow(warnLayout,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true);

        //check if user has logged in before
        User savedUser = Current.retrieveUserFromPref(this);
        if(savedUser != null) {
            Log.v(TAG, "user has signed in before");
            Current.addCurUser(savedUser);
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
    }

    public void login(View view) {
        EditText emailText = (EditText) findViewById(R.id.emailText);
        EditText passText  = (EditText) findViewById(R.id.passwordText);
        String email = emailText.getText().toString();
        String password = passText.getText().toString();
        if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            emailText.setText("");
            passText.setText("");
            //set warning msg
            TextView warning = (TextView)popUpWindow.getContentView().findViewById(R.id.warnText);
            warning.setText("Email format is not correct. Please try again.");
            popUpWindow.showAtLocation(findViewById(R.id.loginLayout), Gravity.TOP, 0, 0);
            popUpWindow.update(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            return;
        }
        User newUser = new User();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                User rUser = BackEnd.checkLogin(email, password);

                if(rUser != null) {
                    System.out.println("start");
                    newUser.setID(rUser.getID());
                    newUser.setEmail(rUser.getEmail());
                    newUser.setPassword(rUser.getPassword());
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(newUser.getEmail().equals("")) {
            emailText.setText("");
            passText.setText("");
            //set warning msg
            TextView warning = (TextView)popUpWindow.getContentView().findViewById(R.id.warnText);
            warning.setText("Email and Password don't match. Please try again.");
            popUpWindow.showAtLocation(findViewById(R.id.loginLayout), Gravity.TOP, 0, 0);
            popUpWindow.update(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        }
        else{
            /* Login success, safe to use the returned user object */

            //query from database the posts starred by the logged in user
            Thread getStarredPostThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Post> starredPosts = BackEnd.getStarPost(newUser);
                    Log.v(TAG, "got starred posts for user " + newUser.getEmail());
                    List<String> starredPostIds = new ArrayList<>();
                    for(Post post : starredPosts) {
                        starredPostIds.add(Integer.toString(post.getPostId()));
                    }
                    newUser.setStarredPostId(starredPostIds);
                }
            });

            getStarredPostThread.start();
            try {
                getStarredPostThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //remember user for this session
            Current.addCurUser(newUser, getApplicationContext());
            this.finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void showReg(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void dismissWarn(View view) {
            popUpWindow.dismiss();
    }
}
