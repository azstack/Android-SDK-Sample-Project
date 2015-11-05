package com.azstack.sample.model;

/**
 * Created by luannguyen on 11/5/2015.
 */
public class User {

    private String userId;
    private String name;

    public User() {

    }

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
