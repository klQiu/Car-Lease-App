package com.example.elvis.carleaseapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import static android.R.attr.data;

public class BackEnd {

    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String POST_TABLE = "PostInfo";
    private static final String ORDER_BY = " ORDER BY ";

    private static final String TAG = BackEnd.class.getSimpleName();

    public static List<Post> getPosts(int startnum, int endnum) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            stmt = myConn.createStatement();
            String start = Integer.toString(startnum);
            String end = Integer.toString(endnum);
            String query = SELECT_ALL_FROM +
                    POST_TABLE +
                    ORDER_BY +
                    "postTime DESC limit " + start + ", " + end;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //Retrieve by column name
                Post post = new Post(rs.getInt("userId"),  rs.getString("title"));
                post.setBrand(rs.getString("brand"));
                post.setColour(rs.getString("colour"));
                post.setYear(rs.getInt("year"));
                post.setMilage(rs.getInt("milage"));
                post.setPrice(rs.getInt("price"));
                post.setRentTime(rs.getString("rentTime"));
                post.setPostTime(rs.getDate("postTime").toString());
                list.add(post);
            }
            rs.close();
            myConn.close();
            stmt.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (myConn != null)
                    myConn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return list;
    }


    static public ArrayList<Post> getHisPost(User user) {
        return null;
    }

    static public void addPost(Post post) {
        Connection myConn = null;
        PreparedStatement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            st =  myConn.prepareStatement("insert into PostInfo values (?,NULL,?,?,?,?,?,?,?,?,?,?)");
            st.setInt(1,post.getUserId());
            st.setString(2, post.getTitle());
            st.setString(3, post.getBrand());
            st.setString(4, post.getColour());
            st.setInt(5,post.getYear());
            st.setInt(6,post.getMilage());
            st.setInt(7,post.getPrice());
            st.setString(8,post.getRentTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            st.setString(9,dateFormat.format(date));
            st.setString(10, post.getTelephone());
            st.setString(11, post.getEmail());
            Log.v(TAG, st.toString());
            st.execute();
            st.close();
            myConn.close();
        }
        catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally {
            //finally block used to close resources
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (myConn != null)
                    myConn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
    static public void addUser(User user) {

    }
    static public Boolean checkLogin(String email, String entPassword) {
        return false;
    }

}
