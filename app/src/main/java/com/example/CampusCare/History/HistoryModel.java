package com.example.CampusCare.History;
//MAIN Coder: Pundavela
public class HistoryModel{
    private String type;
    private String date;
    private String time;

    public HistoryModel(String type, String date, String time) {
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
