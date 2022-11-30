package com.nita.penamob.model;

public class RecommendationModel {
    private String id, title, passGrade, failedGrade, formula;

    public RecommendationModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassGrade() {
        return passGrade;
    }

    public void setPassGrade(String passGrade) {
        this.passGrade = passGrade;
    }

    public String getFailedGrade() {
        return failedGrade;
    }

    public void setFailedGrade(String failedGrade) {
        this.failedGrade = failedGrade;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }
}
