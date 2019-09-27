package com.example.calorietracker;

import java.math.BigDecimal;

public class Food {
    private String foodid;

    private String foodname;

    private String category;

    private BigDecimal calorieAmount;

    private String servingUnit;

    private String servingAmount;

    private BigDecimal fat;


    public Food() {
    }

    public Food(String foodid) {
        this.foodid = foodid;
    }

    public Food(String foodid, String foodname, String category,BigDecimal calorieAmount, String servingUnit,String servingAmount, BigDecimal fat) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.category = category;
        this.calorieAmount = calorieAmount;
        this.servingUnit = servingUnit;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(BigDecimal calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public String getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(String servingAmount) {
        this.servingAmount = servingAmount;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

}
