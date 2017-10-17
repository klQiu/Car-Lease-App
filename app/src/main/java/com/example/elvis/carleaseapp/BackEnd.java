package com.example.elvis.carleaseapp;

import java.util.ArrayList;
import java.sql.*;

/**
 * Created by apple on 2017/10/12.
 */

public class BackEnd {

    static public ArrayList<Post> get20Post() {
        return null;
    }
    static public ArrayList<Post> getHisPost(User user) {
        return null;
    }
    static public void addPost(Post post) {

    }
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

}
