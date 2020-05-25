package com.su29029.server.bean;


public class SelectProblem extends Problem {
    private String options;

    public void setOptions(String option){
        this.options = option;
    }

    public String getOptions(){
        return this.options;
    }
}
