package com.example.elvis.carleaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import java.sql.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(toolbar);

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

    public void post(View view){
        Intent myIntent = new Intent(MainActivity.this, PostForm.class);
        startActivity(myIntent);
    }

    public void onClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                insert();
            }
        }).start();
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
            st.setString(1, "sss");
            st.setString(2, "sss");
            st.setString(3, "sss");
            st.setInt(4,10);
            st.execute();
            st.close();
            Statement stmt = myConn.createStatement();
            StringBuffer sql =new StringBuffer();;
            sql.append("SELECT * FROM userinfo");
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
                //Retrieve by column name
                String email  = rs.getString("email");
                String password = rs.getString("password");
                String name = rs.getString("name");
                int id = rs.getInt("id");

                //Display values
                System.out.print("email: " + email);
                System.out.print(", password: " + password);
                System.out.print(", name: " + name);
                System.out.println(", id: " + id);
            }
            rs.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
