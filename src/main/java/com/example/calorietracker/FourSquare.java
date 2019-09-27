package com.example.calorietracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FourSquare {

    private static final String client_id = "2BHVTFGAI4LTW0EJXJP5A2OIYS3PJDMXIYOOGSGNHB1Z5LDI";
    private static final String client_secret= "RR4AVELQ2QD0DRDZWR21QIN1CK3WDO4PZYO14WIEGDUUOILY\n";

    public static String search(String[] params, String[] values) {
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
            url = new URL("https://api.foursquare.com/v2/venues/search?v=20190501&client_id=" +
                    client_id+ "&client_secret=" + client_secret + query_parameter);
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

    public static List<String> getLocation(String result) {
        List<String> location = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(result);
            JSONArray venues = response.getJSONObject("response").getJSONArray("venues");
            if (venues != null && venues.length() > 0) {
                for (int i = 0; i < venues.length(); i++) {
                    location.add(
                            venues.getJSONObject(i).getJSONObject("location").getString("lat") + "," +
                                    venues.getJSONObject(i).getJSONObject("location").getString("lng")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            location = null;
        }
        return location;

    }

}


