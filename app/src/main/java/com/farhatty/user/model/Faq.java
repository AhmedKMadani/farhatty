package com.farhatty.user.model;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class Faq {
    private String question;
    private String answer;
    private int viewStatus;

    public Faq(String question, String answer, int viewStatus) {
        this.question = question;
        this.answer = answer;
        this.viewStatus = viewStatus;
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

    public int getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(int viewStatus) {
        this.viewStatus = viewStatus;
    }
}
