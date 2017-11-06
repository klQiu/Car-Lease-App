package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void regAccout(View view){
        EditText emailEt = (EditText) findViewById(R.id.RegEmailEt);
        EditText pswEt = (EditText) findViewById(R.id.RegPswEt);
        EditText rePswEt = (EditText) findViewById(R.id.RegRePswEt);
        TextView warning  = (TextView) findViewById(R.id.RegWarningTv);
        String email = emailEt.getText().toString();
        String psw = pswEt.getText().toString();
        String rePsw = rePswEt.getText().toString();

        if(!psw.equals(rePsw)){
            warning.setText("The passwords don't match. Please enter again");
            pswEt.setText("");
            rePswEt.setText("");
        }
        else{
            User newUser = new User(email, psw);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BackEnd.addUser(newUser);
                }
            }).start();
            this.finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
