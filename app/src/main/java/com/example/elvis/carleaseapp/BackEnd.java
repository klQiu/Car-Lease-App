package com.example.elvis.carleaseapp;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.Calendar;
import java.util.List;


import static android.R.attr.data;

public class BackEnd {
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String POST_TABLE = "PostInfo";
    private static final String ORDER_BY = " ORDER BY ";

    private static final String TAG = BackEnd.class.getSimpleName();
    static public void addUser(User user) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            PreparedStatement st =  myConn.prepareStatement("insert into userinfo values (?,?)");
            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            st.execute();
            st.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    /**
     @param email - email user entered
     @param entPassword - password user entered
     @return if email matches the password, return User, else return null
     */
    static public User checkLogin(String email, String entPassword) {
        Connection myConn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            stmt = myConn.createStatement();
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT * FROM userinfo WHERE email='" + email + "'AND password='" + entPassword+"'");
            ResultSet rs = stmt.executeQuery(sql.toString());

            int count = 0;
            while (rs.next()){
                count++;
            }

            if(count == 1) {
                System.out.println("find");
                rs.first();
                User user = new User(rs.getString("email"), rs.getString("password"), rs.getInt("userId"));
                stmt.close();
                myConn.close();
                return user;
            }
            else {
                stmt.close();
                myConn.close();
                return null;
            }
        }
        catch (Exception exc) {
            exc.printStackTrace();

            if(stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            if(myConn != null)
                try {
                    myConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }


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
            Log.v(TAG, query);
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
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentDate = calendar.getTime();
            java.sql.Date date = new java.sql.Date(currentDate.getTime());
            st.setDate(9,date);
            st.setInt(10, post.getTelephone());
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


}
