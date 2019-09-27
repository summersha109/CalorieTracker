package com.example.calorietracker;

    public class Food2 {
        private String foodid;
        private String foodname;

        public Food2() {

        }
        public Food2(String foodid, String foodname){
            this.foodid = foodid;
            this.foodname = foodname;
        }

        public void setFoodid(String foodid) {
            this.foodid = foodid;
        }

        public String getFoodid() {
            return foodid;
        }

        public void setFoodname(String foodname) {
            this.foodname = foodname;
        }

        public String getFoodname() {
            return foodname;
        }
    }


