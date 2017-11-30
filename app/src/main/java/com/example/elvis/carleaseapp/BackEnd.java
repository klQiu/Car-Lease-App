package com.example.elvis.carleaseapp;

import android.util.Log;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.*;
import java.util.Date;
import java.util.List;

public class BackEnd {
    private static final String SELECT_ALL_FROM = "SELECT * FROM ";
    private static final String USER_TABLE = "userinfo";
    private static final String POST_TABLE = "PostInfo";
    private static final String ORDER_BY = " ORDER BY ";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    private static final String SERVER = "jdbc:mysql://23.229.238.67:3306/carLeaseUser";
    private static final String USER_NAME = "betty";
    private static final String PASSWORD = "cfy970213";

    private static final String TAG = BackEnd.class.getSimpleName();

   static public boolean addUser(User user) {
       Statement stmt = null;
        try {
            Class.forName(DRIVER_NAME);
            Connection myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();
            String sql = ("SELECT * FROM userinfo WHERE email='" + user.getEmail() + "'");
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()){
                count++;
            }
            if(count>0)
                return false;
            PreparedStatement st =  myConn.prepareStatement("insert into userinfo values (?,?,NULL)");

            st.setString(1, user.getEmail());
            st.setString(2, user.getPassword());
            st.execute();
            st.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
       return true;
    }

    static public void deleteUser(User user) {
        try {
            Class.forName(DRIVER_NAME);
            Connection myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);

            Statement stmt = myConn.createStatement();
            String userEmail = user.getEmail();
            String query = "DELETE FROM " + USER_TABLE + " WHERE email='" + userEmail + "'";
            stmt.executeUpdate(query);
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
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
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

    public static void changePsd(String email, String newPsd) {
        Connection myConn = null;
        Statement stmt = null;
        try{
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();
            String sql = "UPDATE " + USER_TABLE + " SET password=" + newPsd + " WHERE email=" + email;
            stmt.executeUpdate(sql);
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static List<Post> filterPosts(int startnum, int endnum, String filter, String order, String location) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> list = new ArrayList<>();
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();
            String start = Integer.toString(startnum);
            String end = Integer.toString(endnum);

            String query = SELECT_ALL_FROM + POST_TABLE;
            if(!"".equals(location)) {
                query += " WHERE title = '" + location + "'";
            }
            query += ORDER_BY + filter + " " + order + " limit " + start + ", " + end;

            Log.v(TAG, query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Post post = getPostFromRs(rs);
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

    static public List<Post> getHisPost(User user) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> list = new ArrayList<>();
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();

            String query = SELECT_ALL_FROM +
                    POST_TABLE +
                    " WHERE userId = " + user.getID() +
                    ORDER_BY +
                    "postTime DESC" ;

            Log.v(TAG, query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Post post = getPostFromRs(rs);
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


    static public void addPost(Post post) {
        Connection myConn = null;
        PreparedStatement st = null;
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            st =  myConn.prepareStatement("insert into PostInfo values (?,NULL,?,?,?,?,?,?,?,?,?,?,?)");
            st.setInt(1,post.getUserId());
            st.setString(2, post.getTitle());
            st.setString(3, post.getBrand());
            st.setString(4, post.getColour());
            st.setInt(5,post.getYear());
            st.setInt(6,post.getMileage());
            st.setInt(7,post.getPrice());
            st.setString(8,post.getRentTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            st.setString(9,dateFormat.format(date));
            st.setString(10, post.getTelephone());
            st.setString(11, post.getEmail());
            /* prepare image blob */
            Blob blob = myConn.createBlob();
            blob.setBytes(1, post.getImgBytes());
            st.setBlob(12, blob);
            blob.free();

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

    static public void deletePost(Post post) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> list = new ArrayList<>();
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();

            String query = "DELETE FROM " + POST_TABLE +
                    " WHERE postId = " + post.getPostId();

            Log.v(TAG, query);
            stmt.executeUpdate(query);
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
    }

    static public void updatePost(Post post) {
        Connection myConn = null;
        PreparedStatement st = null;
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            st =  myConn.prepareStatement
                    ("update PostInfo set title = ?, brand = ?,colour = ?,year = ?,mileage = ?,price = ?,rentTime = ?,postTime = ?,telephone = ?,email = ?,imgBytes = ? where postID = ?");
            st.setString(1, post.getTitle());
            st.setString(2, post.getBrand());
            st.setString(3, post.getColour());
            st.setInt(4,post.getYear());
            st.setInt(5,post.getMileage());
            st.setInt(6,post.getPrice());
            st.setString(7,post.getRentTime());
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            st.setString(8,dateFormat.format(date));
            st.setString(9, post.getTelephone());
            st.setString(10, post.getEmail());
            /* prepare image blob */
            Blob blob = myConn.createBlob();
            blob.setBytes(1, post.getImgBytes());
            st.setBlob(11, blob);
            blob.free();
            st.setInt(12, post.getPostId());

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

    static private Post getPostFromRs(ResultSet rs) throws SQLException{
        //Retrieve by column name
        Post post = new Post(rs.getInt("userId"),  rs.getString("title"));
        post.setBrand(rs.getString("brand"));
        post.setColour(rs.getString("colour"));
        post.setYear(rs.getInt("year"));
        post.setMileage(rs.getInt("mileage"));
        post.setPrice(rs.getInt("price"));
        post.setRentTime(rs.getString("rentTime"));
        post.setPostTime(rs.getDate("postTime").toString());
        post.setTelephone(rs.getString("telephone"));
        post.setEmail(rs.getString("email"));
        post.setPostId(rs.getInt("postId"));

        /* get image blob */
        Blob blob = rs.getBlob("imgBytes");
        if(blob != null) {
            byte[] imgBytes = blob.getBytes(1, (int)blob.length());
            post.setImgBytes(imgBytes);
        }
        return post;
    }

    static public void star(User user, Post post) {
        try {
            Class.forName(DRIVER_NAME);
            Connection myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            PreparedStatement st =  myConn.prepareStatement("insert into starRelation values (?,?,NULL)");

            st.setInt(1, user.getID());
            st.setInt(2, post.getPostId());
            st.execute();
            st.close();
            myConn.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    static public void unStar(User user, Post post) {
        Connection myConn = null;
        Statement stmt = null;
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();
            String query = "delete from starRelation where user_id = " +  user.getID() + " AND post_id = " + post.getPostId();
            stmt.executeUpdate(query);
            myConn.close();
            stmt.close();
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    static public List<Post> getStarPost(User user) {
        Connection myConn = null;
        Statement stmt = null;
        List<Post> postList = new ArrayList<>();
        try {
            Class.forName(DRIVER_NAME);
            myConn = DriverManager.getConnection(SERVER, USER_NAME, PASSWORD);
            stmt = myConn.createStatement();

            String query = SELECT_ALL_FROM +
                    POST_TABLE +
                    " WHERE postId IN (SELECT DISTINCT post_id From starRelation WHERE user_id = " + user.getID() + ")";

            Log.v(TAG, query);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Post post = getPostFromRs(rs);
                postList.add(post);
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
        return postList;
    }
}


