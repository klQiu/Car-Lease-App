package com.example.elvis.carleaseapp;

import org.junit.Test;

import java.util.List;

import static com.example.elvis.carleaseapp.BackEnd.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by elvis on 2017/11/6.
 */

public class BackEndTest {
    String testEmail = "car@gmail.com";
    String testPassword = "11111";
    User testUser = new User(testEmail, testPassword);

    @Test
    public void addUserAndLoginTest() throws Exception {
        addUser(testUser);
        User getUser = checkLogin(testEmail, testPassword);
        assertNotEquals(getUser, null);
        assertEquals("Email should be the same", testEmail, getUser.getEmail());
        assertEquals("Password should be the same", testPassword, getUser.getPassword());
        deleteUser(testUser);
    }

    @Test
    public void filterTest() {
        List<Post> list = BackEnd.filterPosts(1, 2, "postTime", "DESC");
        assertEquals("List should contain two post", 2, list.size());
    }
    @Test
    public void postAndGetPost() {
        Post post = new Post(0, "test");
        post.setYear(666);
        post.setBrand("bmw");
        post.setColour("green");
        post.setMileage(100);
        post.setPrice(100);
        post.setEmail("@gmail.com");
        post.setTelephone("66666");
        post.setRentTime("one month");
        BackEnd.addPost(post);
        List<Post> list = BackEnd.filterPosts(1, 1, "postTime", "DESC");
        Post postGet = list.get(0);
        assertEquals("user id", 0, postGet.getUserId());
        assertEquals("title", "test", postGet.getTitle());
        assertEquals("year", 666, postGet.getYear());
        assertEquals("brand", "bmw", postGet.getBrand());
        assertEquals("colour", "green", postGet.getColour());
        assertEquals("mileage", 100, postGet.getMileage());
        assertEquals("price", 100, postGet.getPrice());
        assertEquals("email", "@gmail.com", postGet.getEmail());
        assertEquals("telephone", "66666", postGet.getTelephone());
        assertEquals("rent time", "one month", postGet.getRentTime());
    }

}
