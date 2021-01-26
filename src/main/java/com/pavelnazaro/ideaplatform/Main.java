package com.pavelnazaro.ideaplatform;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Main {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yy HH:mm");
    private static final String FILE_NAME = "tickets.json";
    private static final String ORIGIN_NAME = "Владивосток";
    private static final String DESTINATION_NAME = "Тель-Авив";
    private static final double PERCENTILE = 90;

    public static void main(String[] args) {
        try(Reader reader = new FileReader(FILE_NAME)){
            Tickets tickets = new Gson().fromJson(reader, Tickets.class);

            long averageTime = 0;
            List<Double> times = new ArrayList<>();

            for (TicketsInfo info:tickets.getTickets()){
                if (info.getOrigin_name().equals(ORIGIN_NAME) && info.getDestination_name().equals(DESTINATION_NAME)) {
                    Date arrival_time = FORMAT.parse(info.getArrival_date() + " " + info.getArrival_time());
                    Date departure_time = FORMAT.parse(info.getDeparture_date() + " " + info.getDeparture_time());
                    long time = arrival_time.getTime() - departure_time.getTime();

                    averageTime += time;
                    times.add((double) time);
                }
            }

            System.out.println("Average time: " + getHoursAndMinutes(averageTime/tickets.getTickets().size()));
            System.out.println("90 percentile: " + getHoursAndMinutes((long) percentile(times,PERCENTILE/10)));
        } catch (FileNotFoundException e){
            System.out.println("File '" + FILE_NAME + "' not found!");
        } catch (ParseException | IOException e){
            e.printStackTrace();
        }
    }

    public static String getHoursAndMinutes(long averageTime){
        long hours = averageTime/1000/60/60;
        long minutes = averageTime/1000/60-hours*60;
        return "Hours: " + hours + " Minutes: " + minutes;
    }

    public static double percentile(List<Double> values, double percentile) {
        Collections.sort(values);
        int index = (int) Math.ceil((percentile / 100) * values.size());
        return values.get(index - 1);
    }
}
