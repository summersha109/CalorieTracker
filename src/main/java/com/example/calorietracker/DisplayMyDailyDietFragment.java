package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DisplayMyDailyDietFragment extends Fragment{
    View vDisplayMyDaily;
    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<String> adapter2;
    Spinner spinner;
    EditText et_cate;
    TextView tv_food;
    Spinner sp_food;
    String foodChoice;
    String quantity;
    Button bt_Submit;
    EditText et_quantity;
    Button test;
    TextView tv_result;
    TextView tv_mesure;
    EditText editFood;
    ArrayList<String> foodname = new ArrayList<String>();
    ArrayList<String> foodInfor = new ArrayList<String>();
    TextView searchResult;
    Button bt_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        vDisplayMyDaily = inflater.inflate(R.layout.fragment_my_daily_diet, container,false);
        editFood = vDisplayMyDaily.findViewById(R.id.edit_food);
        Button btnSearch = vDisplayMyDaily.findViewById(R.id.btnSearch);
        et_cate = vDisplayMyDaily.findViewById(R.id.et_cate);
        spinner = vDisplayMyDaily.findViewById(R.id.cate_spinner);
        sp_food = vDisplayMyDaily.findViewById(R.id.sp_food);
        tv_result =vDisplayMyDaily.findViewById(R.id.tv_foodResult);
        tv_food = vDisplayMyDaily.findViewById(R.id.tv_food);
        bt_Submit = vDisplayMyDaily.findViewById(R.id.bt_submit);
        et_quantity = vDisplayMyDaily.findViewById(R.id.et_quantity);
        tv_mesure = vDisplayMyDaily.findViewById(R.id.tv_measure);
        searchResult = vDisplayMyDaily.findViewById(R.id.tv_search);
        bt_add = vDisplayMyDaily.findViewById(R.id.bt_add);

        adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.category, android.R.layout.simple_spinner_item);
        adapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        sp_food.setAdapter(adapter2);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectCategory = parent.getItemAtPosition(position).toString();
                et_cate.setText(selectCategory);
                foodname.clear();
                adapter2.clear();
                FoodSearchAsyncTask foodSearchAsyncTask = new FoodSearchAsyncTask();
                foodSearchAsyncTask.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sp_food.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               foodChoice = parent.getItemAtPosition(position).toString();
                SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                searchAsyncTask.execute(foodChoice);
                SearchFoodInforAsyncTask searchFoodInforAsyncTask = new SearchFoodInforAsyncTask();
                searchFoodInforAsyncTask.execute(foodChoice);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConsumptionPostAsyncTask consumptionPostAsyncTask = new ConsumptionPostAsyncTask();
                consumptionPostAsyncTask.execute();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editFood.getText().toString();
                SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                searchAsyncTask.execute(keyword);
                SearchFoodInforAsyncTask searchFoodInforAsyncTask = new SearchFoodInforAsyncTask();
                searchFoodInforAsyncTask.execute(keyword);
                searchResult.setText(foodInfor.get(0) + foodInfor.get(1) + foodInfor.get(2) +foodInfor.get(3));
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodPostAsyncTask foodPostAsyncTask = new FoodPostAsyncTask();
                foodPostAsyncTask.execute();
            }
        });


        return vDisplayMyDaily;
    }


    //Search service food.
    private  class  FoodSearchAsyncTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void...params){
            String cate = DisplayMyDailyDietFragment.this.et_cate.getText().toString();
            String result = RestClient.findByCategory(cate);
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                for(int i=0; i < jsonArray.length(); i++ ) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    foodname.add(jsonObject.getString("foodname")) ;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Food list";

        }
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
            adapter2.addAll(foodname);
        }
    }
     //Google search image.
    private class SearchAsyncTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String...params){
            String imageOb = SearchGoogleAPI.search(params[0], new String[]{"num"}, new String[]{"1"});
            String imageLink = SearchGoogleAPI.getImage(imageOb);
            Bitmap image = SearchGoogleAPI.getBitmapFromURL(imageLink);
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView tv = vDisplayMyDaily.findViewById(R.id.image);
            tv.setImageBitmap(result);
        }
    }

    //Food api search.
    private class SearchFoodInforAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...params){
             String result = SearchFoodApi.search(params[0],new String[]{"num"},new String[]{"1"});

            return result;
        }

        @Override
        protected void onPostExecute(String  result) {
            foodInfor.clear();
            JSONObject jsonObject = null;
            try { jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("hints");
                if (jsonArray != null && jsonArray.length() > 0) {
                    JSONObject foodObject = jsonArray.getJSONObject(0).getJSONObject("food");
                    String calorie = foodObject.getJSONObject("nutrients").getString("ENERC_KCAL");
                    String fat = foodObject.getJSONObject("nutrients").getString("FAT");
                    String serviceUint = jsonArray.getJSONObject(0).getJSONArray("measures").getJSONObject(0).getString("label");
                    String foodLabal = foodObject.getString("label");
                    String category = foodObject.getString("category");
                    tv_result.setText("Calorie:  " + calorie + "    Fat:  " + fat);
                    tv_mesure.setText("Unit:  " + serviceUint);
                    foodInfor.add(foodLabal);
                    foodInfor.add(calorie);
                    foodInfor.add(fat);
                    foodInfor.add(serviceUint);
                    foodInfor.add(category);



                }




            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



    private class ConsumptionPostAsyncTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {

            String foodResult = RestClient.findByFoodname(foodChoice);
            Intent intent = getActivity().getIntent();
            Bundle bundle = intent.getExtras();
            String username = bundle.getString("username");
            String userResult = RestClient.findByUsername(username);
            Food food = null;
            UserTable user = null;

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(foodResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                Gson gson = new Gson();
                food = gson.fromJson(jsonObject.toString(), Food.class);
                jsonArray = new JSONArray(userResult);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0).getJSONObject("userid");
                Gson gson1 = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").create();
                user = gson1.fromJson(jsonObject1.toString(), UserTable.class);


            } catch (JSONException e) {
                e.printStackTrace();
            }




            JSONArray consump = null;
            String allConsump = RestClient.findAllConsumption() ;

            try {
                consump = new JSONArray(allConsump);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String comsupid = "c" + String.valueOf(consump.length()+1);

            Consumption consumption = new Consumption();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date curDate = new Date(System.currentTimeMillis());

            consumption.setConsumpid(comsupid);
            consumption.setFoodid(food);
            consumption.setUserid(user);
            consumption.setQuantityServingOfFood(et_quantity.getText().toString());
            consumption.setDate(curDate);
            RestClient.createConsumption(consumption);
            return "Consumption submit";
        }
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
        }
    }

    private class FoodPostAsyncTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String foodId = null;
                String result = RestClient.findAllFood();
            try {
                JSONArray jsonArray = new JSONArray(result);
                foodId = "f" + (jsonArray.length() + 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Food food = new Food();
                food.setFoodid(foodId);
                food.setFoodname(foodInfor.get(0));
                food.setCategory(foodInfor.get(4));
                food.setCalorieAmount(BigDecimal.valueOf((Double.valueOf(foodInfor.get(1)))));
                food.setServingUnit(foodInfor.get(3));
                food.setServingAmount("1");
                food.setFat(BigDecimal.valueOf(Double.valueOf(foodInfor.get(2))));
                RestClient.createFood(food);



                return "Food is added";





        }

        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
        }
    }


}






