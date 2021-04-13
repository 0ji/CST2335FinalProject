package com.cst2335.cst2335finalproject;

import android.widget.TextView;

public class Question {

    private String question;
    private String category;
    private String difficulty;
    private String correct_answer;
    private String incorrect_answers;
    private String type;
    public boolean isAnswered;
    private String state;

    private long id;

    public Question(String question, String category, String difficulty, String correct_answer, String incorrect_answers, String type, long id) {
        this.question = question;
        this.category = category;
        this.difficulty = difficulty;
        this.correct_answer = correct_answer;
        this.incorrect_answers = incorrect_answers;
        this.type = type;
        this.id = id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getIncorrect_answers() {
        return incorrect_answers;
    }

    public void setIncorrect_answers(String incorrect_answers) {
        this.incorrect_answers = incorrect_answers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
class ViewHolder {
    TextView text;

    public ViewHolder(TextView text) {
        this.text = text;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }
}
