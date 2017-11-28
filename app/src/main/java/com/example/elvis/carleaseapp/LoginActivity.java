package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elvis.carleaseapp.BackEnd;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //set title font
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Sofia-Regular.otf");
        TextView title = (TextView)findViewById(R.id.loginTitle);
        title.setTypeface(myTypeface);

        EditText password = (EditText) findViewById(R.id.passwordText);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());

        User savedUser = Current.retrieveUserFromPref(this);
        if(savedUser != null) {
            Log.v(TAG, "user has signed in before");
            Current.addCurUser(savedUser);
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }

    public void login(View view) {
        EditText emailText = (EditText) findViewById(R.id.emailText);
        EditText passText  = (EditText) findViewById(R.id.passwordText);
        String email = emailText.getText().toString();
        String password = passText.getText().toString();
        System.out.println(email);
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

        System.out.println("emaial is " + newUser.getEmail());
        if(newUser.getEmail().equals("")) {
            emailText.setText("");
            passText.setText("");
            TextView warning = (TextView) findViewById(R.id.loginWarning);
            warning.setText("Failed: Email and Password don't match");
        }
        else{
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

}
