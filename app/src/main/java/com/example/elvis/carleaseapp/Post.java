package com.example.elvis.carleaseapp;

import java.io.Serializable;

/**
 * Created by elvis on 2017/10/10.
 */

public class Post implements Serializable{

    private String title;
    private String brand;
    private String colour;
    private int year;
    private int mileage;
    private int photoIds[];
    private int price;
    private String rentTime;
    private final int userId;
    private int postId;
    private String postTime;
    private String email;
    private String telephone;
    private byte[] imgBytes = new byte[0];

    public Post(int userId, String title) {
        this.title = title;
        this.userId = userId;
    }

    public void setPostId(int postId) {this.postId = postId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRentTime() {
        return rentTime;
    }

    public void setRentTime(String rentTime) {
        this.rentTime = rentTime;
    }

    public int[] getPhotoIds() {
        return photoIds;
    }

    public void addPhotoId(int photoId) {
        int size = photoIds.length;
        photoIds[size] = photoId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostTime(String time) {
        this.postTime = time;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setEmail (String  email){
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public byte[] getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(byte[] imgBytes) {
        this.imgBytes = imgBytes;
    }
}
