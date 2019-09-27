package com.example.calorietracker;

import java.math.BigDecimal;
import java.util.Date;

public class Report {
    private String repid;

    private Date reportdate;

    private Integer totalStepsTaken;

    private Integer calorieGoal;

    private BigDecimal totalCaloriesConsumed;

    private BigDecimal totalCaloriesBurned;

    private UserTable userid;

    public Report() {
    }

    public Report(String repid) {
        this.repid = repid;
    }

    public Report(String repid, Date reportdate, Integer totalStepsTaken,Integer calorieGoal,BigDecimal totalCaloriesConsumed,BigDecimal totalCaloriesBurned,UserTable userid) {
        this.repid = repid;
        this.reportdate = reportdate;
        this.totalStepsTaken = totalStepsTaken;
        this.calorieGoal = calorieGoal;
        this.totalCaloriesBurned = totalCaloriesBurned;
        this.totalCaloriesConsumed = totalCaloriesConsumed;
        this.userid = userid;
    }

    public String getRepid() {
        return repid;
    }

    public void setRepid(String repid) {
        this.repid = repid;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

    public Integer getTotalStepsTaken() {
        return totalStepsTaken;
    }

    public void setTotalStepsTaken(Integer totalStepsTaken) {
        this.totalStepsTaken = totalStepsTaken;
    }

    public Integer getCalorieGoal() {
        return calorieGoal;
    }

    public void setCalorieGoal(Integer calorieGoal) {
        this.calorieGoal = calorieGoal;
    }

    public BigDecimal getTotalCaloriesConsumed() {
        return totalCaloriesConsumed;
    }

    public void setTotalCaloriesConsumed(BigDecimal totalCaloriesConsumed) {
        this.totalCaloriesConsumed = totalCaloriesConsumed;
    }

    public BigDecimal getTotalCaloriesBurned() {
        return totalCaloriesBurned;
    }

    public void setTotalCaloriesBurned(BigDecimal totalCaloriesBurned) {
        this.totalCaloriesBurned = totalCaloriesBurned;
    }

    public UserTable getUserid() {
        return userid;
    }

    public void setUserid(UserTable userid) {
        this.userid = userid;
    }

}
