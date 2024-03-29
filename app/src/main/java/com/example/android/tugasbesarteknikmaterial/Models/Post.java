package com.example.android.tugasbesarteknikmaterial.Models;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String userID;
    private String userPhoto;
    private Object timestamp;

    public Post(String title, String description, String picture, String userID, String userPhoto) {
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.userID = userID;
        this.userPhoto = userPhoto;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Post() {

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
