package com.example.planner;
import java.io.*;
import java.util.Scanner;

enum RequestType {
    SEARCH
}

public class MealPlanner {

    private static void launchApp(){
        double calories = -1;
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Log a food item:");
            String food = input.next();
            try {
                calories = HttpQueries.getFoods(food);
            } catch (IOException e){
                System.out.printf("Found error: %s", e);
            }
            System.out.printf("%s is %f calories\n\n", food, calories);
        }
    }

    public static void main(String[] args) throws IOException {
        //Open WebApp
        launchApp();

    }
}
