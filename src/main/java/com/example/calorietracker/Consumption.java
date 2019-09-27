package com.example.calorietracker;

import java.util.Date;

public class Consumption {
    private String consumpid;
    private String quantityServingOfFood;
    private Food foodid;
    private UserTable userid;
    private Date date;

    public Consumption() {
    }

    public Consumption(String consumpid) {
        this.consumpid = consumpid;
    }
    public Consumption(String consumpid,UserTable userid, Food foodid,Date date, String quantityServingOfFood )
    {
        this.consumpid = consumpid;
        this.quantityServingOfFood = quantityServingOfFood;
        this.foodid = foodid;
        this.userid = userid;
        this.date = date;

    }

    public String getConsumpid() {
        return consumpid;
    }

    public void setConsumpid(String consumpid) {
        this.consumpid = consumpid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getQuantityServingOfFood() {
        return quantityServingOfFood;
    }

    public void setQuantityServingOfFood(String quantityServingOfFood) {
        this.quantityServingOfFood = quantityServingOfFood;
    }

    public Food getFoodid() {
        return foodid;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }

    public UserTable getUserid() {
        return userid;
    }

    public void setUserid(UserTable userid) {
        this.userid = userid;
    }
}
