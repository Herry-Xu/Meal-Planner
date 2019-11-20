package com.example.planner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HttpQueries {
    protected static double getFoods(String food) throws IOException {

        double calories = -1.0;
        String query = setQuery(RequestType.SEARCH, food);
        //Send the post request that will return the JsonObject
        HttpURLConnection con = HttpRequests.postRequest(query);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }
        in.close();
        JsonObject root = new Gson().fromJson(response.toString(),JsonObject.class);
        JsonArray foodArray = root.getAsJsonArray("common");
        JsonObject firstFood = foodArray.get(0).getAsJsonObject();
        JsonArray foodNutrients = firstFood.getAsJsonArray("full_nutrients");

        for (int i = 0; i < foodNutrients.size(); i++){
            JsonObject nutrient = foodNutrients.get(i).getAsJsonObject();

            if (nutrient.get("attr_id").getAsInt() == 208){
                calories = nutrient.get("value").getAsDouble();
            }
        }
        return calories;
    }

    protected static String setQuery(RequestType type, String name) {
        String query = "";
        switch (type) {
            case SEARCH: {
                query =
                        "{" +
                                "\"query\":\"" + name + "\", " +
                                "\"branded\": false," +
                                "\"full_nutrients\": {" +
                                "\"208\": {" +
                                "\"lte\": 500 " +
                                "}" +
                                "}," +
                                "\"detailed\": true" +
                                "}";
                break;
            }
        }
        return query;
    }
}
