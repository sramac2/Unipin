package com.example.android.unipin;

public class User {

    private String name;
    private String identification;
    private String uId;
    private String email;
    private String levelName;
    private int points;
    private int reviewCount;
    private int levelNumber;
    private String trophy;

    public User(){

    }
    public User(String name, String uId, String email) {
        this.name = name;
        this.uId = uId;
        this.email = email;
    }

    public User(String name, String uId, String email, String identification, int points,
                int reviewCount, int levelNumber, String levelName, String trophy) {
        this.name = name;
        this.uId = uId;
        this.identification = identification;
        this.email = email;
        this.points = points;
        this.reviewCount = reviewCount;
        this.levelName = levelName;
        this.trophy = trophy;
        this.levelNumber = levelNumber;
    }

    public String getTrophy() {
        return trophy;
    }

    public void setTrophy(String trophy) {
        this.trophy = trophy;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getEmail() {
        return email;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
