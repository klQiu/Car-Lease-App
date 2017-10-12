package com.example.elvis.carleaseapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insert() {
        try {
            System.out.println("start forname");
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("start connecting");
            //System.out.println("111");
            //Class.forName("com.mysql.jdbc.driver");
            //String url = "jdbc:mysql://192.168.1.70/user";
            Connection myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            System.out.println("succeed connecting");

            PreparedStatement st =  myConn.prepareStatement("insert into userinfo values (?,?,?,?)");
            st.setString(1, "rrr@163.com");
            st.setString(2, "55555");
            st.setString(3, "Cat");
            st.setInt(4,1);
            st.execute();
            st.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
