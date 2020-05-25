package com.su29029.server.bean;

public class UserSelectedProblemInformation {
    private User user;
    private String language;
    private String difficulty;
    private int problemNumber;

    public UserSelectedProblemInformation(){}

    public UserSelectedProblemInformation(User user, String language, String difficulty, int problemNumber){
        this.user = user;
        this.language = language;
        this.difficulty = difficulty;
        this.problemNumber = problemNumber;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public String getLanguage() {
        return language;
    }

    public User getUser() {
        return user;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setProblemNumber(int problemNumber) {
        this.problemNumber = problemNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
