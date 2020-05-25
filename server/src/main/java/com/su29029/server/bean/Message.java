package com.su29029.server.bean;

public class Message {
    private String msg;
    private int code;
    private User user;

    public Message(){}

    public Message(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }
    public Message(String msg, int code, User user) {
        this.msg = msg;
        this.code = code;
        this.user = user;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setUser(User user) { this.user = user; }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public User getUser() { return user; }


}
