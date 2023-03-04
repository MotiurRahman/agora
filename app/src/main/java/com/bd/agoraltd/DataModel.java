package com.bd.agoraltd;

public class DataModel {
    private String title;
    private String message;

    public DataModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
