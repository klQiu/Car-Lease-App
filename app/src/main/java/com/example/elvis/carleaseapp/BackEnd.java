package com.example.elvis.carleaseapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by apple on 2017/10/12.
 */

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
            StringBuffer sql = new StringBuffer();
            String start = Integer.toString(startnum);
            String end = Integer.toString(endnum);
            String query = SELECT_ALL_FROM +
                    POST_TABLE +
                    ORDER_BY +
                    "postTime DESC limit " + start + ", " + end;
            sql.append("SELECT * FROM PostInfo ORDER BY postTime DESC limit 0, 2");
            Log.v(TAG, query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                //Retrieve by column name
                Post post = new Post(rs.getInt("userId"), rs.getInt("postId"), rs.getString("title"));
                post.setBrand(rs.getString("brand"));
                post.setColour(rs.getString("colour"));
                post.setYear(rs.getInt("year"));
                post.setMilage(rs.getInt("milage"));
                post.setPrice(rs.getInt("price"));
                post.setRentTime(rs.getString("rentTime"));
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

    public ArrayList<Post> getHisPost(User user) {
        return null;
    }

    public void addPost(Post post) {

    }

    public void addUser(User user) {

    }

    public Boolean checkLogin(String email, String entPassword) {
        return false;
    }

}
