package com.nita.penamob.model;

public class LessonsModel {
    private String id, lessons, type, format, expiredDate;

    public LessonsModel(String id, String lessons, String type, String format, String expiredDate) {
        this.id = id;
        this.lessons = lessons;
        this.type = type;
        this.format = format;
        this.expiredDate = expiredDate;
    }

    public String getId() {
        return id;
    }

    public String getLessons() {
        return lessons;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public String getExpiredDate() {
        return expiredDate;
    }
}
