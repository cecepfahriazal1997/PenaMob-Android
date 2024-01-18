package com.nita.penamob.model;

import java.util.ArrayList;
import java.util.List;

public class LearningPathModel {
    private String id, topic;
    private boolean haveLessons, locked, opened, withScore;
    private List<LessonsModel> lessons = new ArrayList<LessonsModel>();

    public LearningPathModel(String id, String topic, boolean haveLessons, List<LessonsModel> lessons, boolean locked, boolean opened) {
        this.id = id;
        this.topic = topic;
        this.haveLessons = haveLessons;
        this.lessons = lessons;
        this.locked = locked;
        this.withScore = false;
    }

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public boolean isHaveLessons() {
        return haveLessons;
    }

    public List<LessonsModel> getLessons() {
        return lessons;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isWithScore() {
        return withScore;
    }

    public void setWithScore(boolean withScore) {
        this.withScore = withScore;
    }
}
