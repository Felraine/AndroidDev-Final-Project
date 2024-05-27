package com.example.finalproject;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Sched implements Comparable<Sched> {

    private int ID;
    private String task;
    private LocalTime from;
    private LocalTime to;
    private String color;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public Sched() {
        Calendar calendar = Calendar.getInstance();
        from = LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
        to = LocalTime.of(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getSched() {
        return task;
    }

    public void setSched(String task) {
        this.task = task;
    }

    public String getFromToString() {
        return from.format(formatter);
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = LocalTime.parse(from, formatter);
    }

    public String getToToString() {
        return to.format(formatter);
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = LocalTime.parse(to, formatter);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getColorID(Context context) {
        int c;
        switch (color) {
            case "Blue":
                c = ResourcesCompat.getColor(context.getResources(), R.color.steel_blue, null);
                break;
            case "Pink":
                c = ResourcesCompat.getColor(context.getResources(), R.color.orchid, null);
                break;
            case "Cyan":
                c = ResourcesCompat.getColor(context.getResources(), R.color.turquoise, null);
                break;
            case "Yellow":
                c = ResourcesCompat.getColor(context.getResources(), R.color.goldenrod, null);
                break;
            case "Orange":
                c = ResourcesCompat.getColor(context.getResources(), R.color.coral, null);
                break;
            case "Purple":
                c = ResourcesCompat.getColor(context.getResources(), R.color.slate_blue, null);
                break;
            case "Grey":
                c = ResourcesCompat.getColor(context.getResources(), R.color.grey, null);
                break;
            case "Red":
                c = ResourcesCompat.getColor(context.getResources(), R.color.red, null);
                break;
            default:
                c = ResourcesCompat.getColor(context.getResources(), R.color.rose, null);
        }
        return c;
    }


    @Override
    public int compareTo(Sched sched) {
        return from.compareTo(sched.getFrom());
    }
}
