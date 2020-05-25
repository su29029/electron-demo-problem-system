package com.su29029.server.bean;

public class User {
    private String username;
    private String password;
    private float score;

    public User(){}

    public User(String username, String password, int score) {
        this.username = username;
        this.password = password;
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScore(float score) { this.score = score; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public float getScore() {
        return score;
    }

    public Message checkInformation(){
        if (this.username.length() < 3 || this.username.length() > 20) {
            return new Message("username invalid", -1);
        } else if (this.password.length() < 3 || this.password.length() > 20) {
            return new Message("password invalid", -1);
        } else {
            return new Message("pass", 1);
        }
    }

}
