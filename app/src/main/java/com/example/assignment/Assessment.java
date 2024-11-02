package com.example.assignment;

public class Assessment {
    private String weight;
    private String height;
    private String bp;
    private long date;

    public Assessment(String weight, String height, String bp, long date) {
        this.weight = weight;
        this.height = height;
        this.bp = bp;
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getBp() {
        return bp;
    }

    public long getDate() {
        return date;
    }
}
