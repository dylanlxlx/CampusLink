package com.dylanlxlx.campuslink.data.model;

public class Record {
    private int id;
    private String title;
    private String content;
    private String time;
    private String name;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public int getId(){return id;}
}
