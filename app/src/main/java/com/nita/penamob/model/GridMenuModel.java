package com.nita.penamob.model;

public class GridMenuModel {
    private String title, total;

    public GridMenuModel() {
    }

    public GridMenuModel(String title, String total) {
        this.title = title;
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
