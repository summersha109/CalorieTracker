package com.example.calorietracker;

import java.math.BigDecimal;
import java.util.Date;

public class UserTable {
    private Integer userid;
    private String name;
    private String surname;
    private String email;
    private Date dob;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal height;
    private BigDecimal weight;
    private String gender;
    private String address;
    private String postcode;
    private short levelOfActivity;
    private short stepPerMile;

    public UserTable() {
    }

    public UserTable(Integer userid) {
        this.userid = userid;
    }

    public UserTable(Integer userid, String name, String surname, String email, Date dob, BigDecimal height, BigDecimal weight, String gender, String address,String postcode, short levelOfActivity, short stepPerMile) {
        this.userid = userid;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepPerMile = stepPerMile;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public short getLevelOfActivity() {
        return levelOfActivity;
    }

    public void setLevelOfActivity(short levelOfActivity) {
        this.levelOfActivity = levelOfActivity;
    }

    public short getStepPerMile() {
        return stepPerMile;
    }

    public void setStepPerMile(short stepPerMile) {
        this.stepPerMile = stepPerMile;
    }
}
