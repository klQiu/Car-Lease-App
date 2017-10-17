package com.example.elvis.carleaseapp;

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
 * Created by elvis on 2017/10/12.
 */

public class BackEnd {

    static public List<Post> getPosts(int numPosts) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> list = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            myConn = DriverManager.getConnection("jdbc:mysql://23.229.238.67:3306/carLeaseUser", "betty", "cfy970213");
            stmt = myConn.createStatement();
            StringBuffer sql =new StringBuffer();;
            sql.append("SELECT * FROM PostInfo ORDER BY postTime DESC limit " + Integer.toString(numPosts));
            ResultSet rs = stmt.executeQuery(sql.toString());
            while(rs.next()){
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
            st =  myConn.prepareStatement("insert into PostInfo values (?,?,?,?,?,?,?,?,?,?,?)");
            st.setInt(1,post.getUserId());
            st.setString(3, post.getTitle());
            st.setString(4, post.getBrand());
            st.setString(5, post.getColour());
            st.setInt(6,post.getYear());
            st.setInt(7,post.getMilage());
            st.setInt(8,post.getPrice());
            st.setString(9,post.getRentTime());
            //Calendar calendar = Calendar.getInstance();
            //java.util.Date currentDate = calendar.getTime();
            //java.sql.Date date = new java.sql.Date(currentDate.getTime());
            //st.setDate(10,date);
            st.setInt(11, post.getTelephone());
            st.setString(12, post.getEmail());
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
