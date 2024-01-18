package com.nita.penamob.model;

public class PickerModel {
    private String id, title;
    private boolean selected;

    public PickerModel(String id, String title, boolean selected) {
        this.id = id;
        this.title = title;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return selected;
    }
}
