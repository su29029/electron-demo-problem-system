package com.su29029.server.bean;

public class Problem {
    private int id;
    private String problemContent;
    private int problemScore;
    private String problemType;
    private String problemAnswer;

    public int getId() {return this.id;}

    public int getProblemScore() {return this.problemScore;}

    public String getProblemContent() {return this.problemContent;}

    public String getProblemType() {return this.problemType;}

    public String getAnswer(){return this.problemAnswer;}

    public void setAnswer(String problemAnswer){this.problemAnswer = problemAnswer;}

}
