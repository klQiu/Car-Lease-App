package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePsdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd);
    }

    public void subNewPsd(View view) {
        EditText oldPsdText = (EditText)findViewById(R.id.oldPsdText);
        TextView msg = (TextView)findViewById(R.id.changePsdMsg);
        String oldPsd = oldPsdText.getText().toString();
        String correctPst = Current.getCurUser().getPassword();
        if(oldPsd.equals(correctPst)) {
            EditText newPsd1Text = (EditText)findViewById(R.id.newPsdText);
            EditText newPsd2Test = (EditText)findViewById(R.id.conNewPsdText);
            String newPsd1 = newPsd1Text.getText().toString();
            String newPsd2 = newPsd2Test.getText().toString();
            if(newPsd1.equals(newPsd2)) {
                //change psd

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BackEnd.changePsd(Current.getCurUser().getEmail(), newPsd1);
                    }
                });
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Current.getCurUser().setPassword(newPsd1);
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            }
            else{
                //print error msg
                newPsd1Text.setText("");
                newPsd2Test.setText("");
                msg.setText("The passwords don't match. Please enter again");
            }
        }
        else{
            //print error msg
            oldPsdText.setText("");
            msg.setText("The old password is not correct. Please enter again");
        }
    }
}
