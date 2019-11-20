package com.example.planner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {

    protected static URL setUrl(String requestString) throws IOException {
        return new URL(requestString);
    }

    protected static void checkConnection(HttpURLConnection connection) throws IOException{
        //Check for valid request; 400 errors might occur
        int responseCode = connection.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        //HTTP_OK response code is 200
        if (responseCode == HttpURLConnection.HTTP_OK){
            //InputStreamReader is a bridge from byte streams to character streams; Can decode bytes into characters given specific charset
            //con.getInputStream gets the byte stream that is read in from the connection
            //BufferedReader buffers characters in reader for more efficient reads
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            //tracks every line of code that is read
            String inputLine;
            //Stringbuilder response of the given request
            StringBuilder response = new StringBuilder();
            //reads every individual line and appends it to the response
            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            //close the BufferedReader in
            in.close();
            //Gson JsonObject
            JsonObject root = new Gson().fromJson(response.toString(),JsonObject.class);
            JsonArray foodArray = root.getAsJsonArray("foods");
            JsonObject firstFood = foodArray.get(0).getAsJsonObject();
            int protein = firstFood.get("nf_protein").getAsInt();
            System.out.println("Desired protein: " + protein);
        }
    }

    protected static HttpURLConnection postRequest(String query) throws IOException{

        URL webpage = setUrl("https://trackapi.nutritionix.com/v2/search/instant");
        HttpURLConnection con = (HttpURLConnection) webpage.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("x-app-id", "ca04c904"); //application ID
        con.setRequestProperty("x-app-key","ad7a59251fee94655112ddd3414e7d91"); //developer application key
        con.setRequestProperty("x-remote-user-id","0"); //developer is set to 0
        con.setRequestProperty("Content-Type","application/json");

        con.setRequestProperty("Content-Length", Integer.toString((query.length())));
        con.setDoOutput(true);
        con.getOutputStream().write(query.getBytes("UTF8"));
        //Check for connection

        int responseCode = con.getResponseCode();
//        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode != HttpURLConnection.HTTP_OK){
            throw new IOException("Bad Server Connection");
        }

        return con;
    }

    protected static void getRequest() throws IOException{

        //The request used for the RESTAPI request
        URL webpage = setUrl("https://trackapi.nutritionix.com/v2/search/item?nix_item_id=513fc9e73fe3ffd40300109f");
        //Opens a connection to the webpage
        HttpURLConnection con = (HttpURLConnection) webpage.openConnection();

        //set request type
        con.setRequestMethod("GET");
        //set request headers
        con.setRequestProperty("x-app-id", "ca04c904"); //application ID
        con.setRequestProperty("x-app-key","ad7a59251fee94655112ddd3414e7d91"); //developer application key
        con.setRequestProperty("x-remote-user-id","0"); //developer is set to 0
        con.setRequestProperty("Content-Type","application/json");

        //Checks for valid connection
        checkConnection(con);
    }
}
