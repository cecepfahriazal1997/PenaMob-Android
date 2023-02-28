package com.nita.penamob.model;

import org.json.JSONArray;

public class QuizModel {
    private String id, number, question, answer, type;
    private JSONArray option;
    private boolean editAnswer;

    public QuizModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getOption() {
        return option;
    }

    public void setOption(JSONArray option) {
        this.option = option;
    }

    public boolean isEditAnswer() {
        return editAnswer;
    }

    public void setEditAnswer(boolean editAnswer) {
        this.editAnswer = editAnswer;
    }
}
