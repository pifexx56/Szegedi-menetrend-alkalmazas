package com.cseradam.szkt;

import java.util.ArrayList;

public class Line
{
    private String number;
    private String terminus;
    private ArrayList<String> times;

    public Line(String number, String terminus, ArrayList<String> times) {
        this.number = number;
        this.terminus = terminus;
        this.times = times;
    }

    public String getNumber() {
        return number;
    }

    public String getTerminusz() {
        return "Végállomás: " + terminus;
    }

    public ArrayList<String> getTimes() {
        return times;
    }
}
