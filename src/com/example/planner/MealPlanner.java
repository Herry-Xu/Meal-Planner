package com.example.planner;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum RequestType {
    SEARCH
}

public class MealPlanner {

    private static void launchApp(){
        List<Double> calories = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        double curCal;
        while (true) {
            System.out.println("Log a food item:");
            String food = input.next();
            if (food.equals("done")){
                break;
            }
            try {
                curCal = HttpQueries.getFoods(food);
                calories.add(curCal);
                System.out.printf("%s is %f calories\n\n", food, curCal);
            } catch (IOException e){
                System.out.printf("Found error: %s", e);
            }
        }
        plotChart(calories);
    }

    private static void plotChart(List<Double> calories){
        double[] dateData = new double[] {11.20, 11.21, 11.22};
        double[] calData = calories.stream().mapToDouble(i->i).toArray();

        XYChart chart = QuickChart.getChart("Day vs. Calorie", "Date", "Calories", "Daily Trend", dateData, calData);

        new SwingWrapper(chart).displayChart();
        try{
            BitmapEncoder.saveBitmap(chart, "./DailyTrend", BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e){
            System.out.printf("Found error: %s", e);
        }
    }

    public static void main(String[] args) throws IOException {
        //Open WebApp
        launchApp();

    }
}
