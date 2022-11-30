package com.nita.penamob.model;

public class CoursesModel {
    private String id, category, title, image, participant, teacher;

    public CoursesModel() {
    }

    public CoursesModel(String id, String category, String title, String image, String participant, String teacher) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.image = image;
        this.participant = participant;
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
