package com.bobcode.splitexpense.models;

/**
 * Created by vijayananjalees on 5/2/15.
 */
public class UserProfileModel {
    private String userName;
    private String password;
    private String answer;
    private String createdOn;

    public UserProfileModel(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public UserProfileModel(String userName, String password, String answer) {
        this.userName = userName;
        this.password = password;
        this.answer = answer;
    }

    public UserProfileModel(String userName, String password, String answer, String createdOn) {
        this.userName = userName;
        this.password = password;
        this.answer = answer;
        this.createdOn = createdOn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
