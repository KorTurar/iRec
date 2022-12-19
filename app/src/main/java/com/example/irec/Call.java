package com.example.irec;

import java.util.Date;

public class Call {
    private String date;
    private String number;
    private String name;
    private String duration;
    private String type;



    public Call(String date, String number, String name, String duration, String type) {
        this.date = date;
        this.number = number;
        this.name = name;
        this.duration = duration;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
