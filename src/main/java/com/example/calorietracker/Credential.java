package com.example.calorietracker;

import java.util.Date;

public class Credential {
    private int creid;

    private String username;

    private String password;

    private Date signUpDate;

    private UserTable userid;

    public Credential() {
    }

    public Credential(Integer creid) {
        this.creid = creid;
    }

    public Credential(Integer creid, String username, String password, Date signUpDate) {
        this.creid = creid;
        this.username = username;
        this.password = password;
        this.signUpDate = signUpDate;
    }

    public int getCreid() {
        return creid;
    }

    public void setCreid(int creid) {
        this.creid = creid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }

    public UserTable getUserid() {
        return userid;
    }

    public void setUserid(UserTable userid) {
        this.userid = userid;
    }

}
