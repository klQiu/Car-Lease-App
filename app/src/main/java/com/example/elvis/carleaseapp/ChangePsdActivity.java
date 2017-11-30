package com.example.elvis.carleaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ChangePsdActivity extends AppCompatActivity {
    PopupWindow popUpWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd);

        //add popup window for warning msg
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View warnLayout = inflater.inflate(R.layout.warn, null);
        popUpWindow = new PopupWindow(warnLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true);
    }

    public void subNewPsd(View view) {
        EditText oldPsdText = (EditText)findViewById(R.id.oldPsdText);
        String oldPsd = oldPsdText.getText().toString();
        String correctPst = Current.getCurUser().getPassword();
        EditText newPsd1Text = (EditText)findViewById(R.id.newPsdText);
        EditText newPsd2Test = (EditText)findViewById(R.id.conNewPsdText);
        String newPsd1 = newPsd1Text.getText().toString();
        String newPsd2 = newPsd2Test.getText().toString();

        if(oldPsd.equals(correctPst)) {
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
                //set warning msg
                TextView warning = (TextView)popUpWindow.getContentView().findViewById(R.id.warnText);
                warning.setText("The passwords don't match. Please enter again");
                popUpWindow.showAtLocation(findViewById(R.id.changePsdLayout), Gravity.TOP, 0, 0);
                popUpWindow.update(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }
        else{
            //print error msg
            oldPsdText.setText("");
            newPsd1Text.setText("");
            newPsd2Test.setText("");
            TextView warning = (TextView)popUpWindow.getContentView().findViewById(R.id.warnText);
            warning.setText("The old password is not correct. Please enter again");
            popUpWindow.showAtLocation(findViewById(R.id.changePsdLayout), Gravity.TOP, 0, 0);
            popUpWindow.update(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void dismissWarn(View view) {
        popUpWindow.dismiss();
    }
}
