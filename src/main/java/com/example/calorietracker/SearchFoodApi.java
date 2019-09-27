package com.example.calorietracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchFoodApi {


        public static String search(String keyword, String[] params, String[] values) {
            keyword = keyword.replace(" ", "%20");
            URL url = null;
            HttpURLConnection connection = null;
            String textResult = "";
            String query_parameter = "";

            if (params != null && values != null) {
                for (int i = 0; i < params.length; i++) {
                    query_parameter += "&";
                    query_parameter += params[i];
                    query_parameter += "=";
                    query_parameter += values[i];
                }
            }

            try {
                url = new URL("https://api.edamam.com/api/food-database/parser?app_id=d000058d&app_key=ab09323f42a8ed898746e6c856af878b&ingr=" +
                        keyword + query_parameter);

                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    textResult += scanner.nextLine();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
            return textResult;
        }


    }


