package com.example.elvis.carleaseapp;

import org.junit.Test;

import static com.example.elvis.carleaseapp.BackEnd.*;
import static org.junit.Assert.*;

/**
 * Created by apple on 2017/11/6.
 */

public class BackendTest {
    String testEmail = "car@gmail.com";
    String testPassword = "11111";
    User testUser = new User(testEmail, testPassword);

    @Test
    public void addUserTest() throws Exception {
        addUser(testUser);
        User getUser = checkLogin(testEmail, testPassword);
        assertEquals("Email should be the same", testEmail, getUser.getEmail());
        assertEquals("Password should be the same", testPassword, getUser.getPassword());
    }

}
