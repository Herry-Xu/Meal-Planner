package com.example.planner;
import java.io.*;
import java.net.*;
import com.google.gson.*;

public class MealPlanner {

    private static void getRequest() throws IOException{

        //The request used for the RESTAPI request
        URL webpage = new URL("https://trackapi.nutritionix.com/v2/search/item?nix_item_id=513fc9e73fe3ffd40300109f");
        //Opens a connection to the webpage
        HttpURLConnection con = (HttpURLConnection) webpage.openConnection();

        //set request type
        con.setRequestMethod("GET");
        //set request headers
        con.setRequestProperty("x-app-id", "ca04c904"); //application ID
        con.setRequestProperty("x-app-key","ad7a59251fee94655112ddd3414e7d91"); //developer application key
        con.setRequestProperty("x-remote-user-id","0"); //developer is set to 0
        con.setRequestProperty("Content-Type","application/json");
        //Check for valid request; 400 errors might occur
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        //HTTP_OK response code is 200
        if (responseCode == HttpURLConnection.HTTP_OK){
            //InputStreamReader is a bridge from byte streams to character streams; Can decode bytes into characters given specific charset
            //con.getInputStream gets the byte stream that is read in from the connection
            //BufferedReader buffers characters in reader for more efficient reads
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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
            System.out.println("Desired protein: " + protein);        }
    }

    private static void postRequest() throws IOException{

        String query = "{" +
                "\"query\":\"2 eggs\", " +
                "\"timezone\": \"US/Eastern\"" +
                "}";

        //The request used for the RESTAPI request
        URL webpage = new URL("https://trackapi.nutritionix.com/v2/natural/nutrients");
        //Opens a connection to the webpage
        HttpURLConnection con = (HttpURLConnection) webpage.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("x-app-id", "ca04c904"); //application ID
        con.setRequestProperty("x-app-key","ad7a59251fee94655112ddd3414e7d91"); //developer application key
        con.setRequestProperty("x-remote-user-id","0"); //developer is set to 0
        con.setRequestProperty("Content-Type","application/json");

        con.setRequestProperty("Content-Length", Integer.toString((query.length())));
        con.setDoOutput(true);
        con.getOutputStream().write(query.getBytes("UTF8"));

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();
            JsonObject root = new Gson().fromJson(response.toString(),JsonObject.class);
            JsonArray foodArray = root.getAsJsonArray("foods");
            JsonObject firstFood = foodArray.get(0).getAsJsonObject();
            int protein = firstFood.get("nf_protein").getAsInt();
            System.out.println("Desired protein: " + protein);
        }

    }

    public static void main(String[] args) throws IOException {
        getRequest();
        postRequest();


//        BufferedReader BringItIn = new BufferedReader(new InputStreamReader(letsSee.getInputStream()));
//        String httpCode;
//
//        while ((httpCode = BringItIn.readLine()) != null){
//            System.out.println(httpCode);
//        }
//        BringItIn.close();
    }
}