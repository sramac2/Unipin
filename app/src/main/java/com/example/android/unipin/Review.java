package com.example.android.unipin;

import com.example.android.unipin.ReviewInfo.Rating;

import java.util.Date;

public class Review {
    private String placeID;
    private String placeName;
    private float noise;
    private float visualInput;
    private String identification;
    private String username;
    private Date date;
    private float crowd;
    private float smell;
    private String description;
    private float light;
    private float overall;
    private String occasion;
    private String uId;
    private Rating rating;

    public Review(String placeID, String placeName, String identification,
                  String username, float noise, float visualInput,
                  float crowd, float smell, float light, String description, String uId, String occasion){
        this.placeID = placeID;
        this.placeName = placeName;
        this.identification = identification;
        this.occasion = occasion;
        this.username = username;
        date = new Date();
        this.uId = uId;
        this.light = light;
        this.visualInput = visualInput;
        this.description = description;
        this.smell = smell;
        this.crowd = crowd;
        this.noise = noise;
    }

    public Review(String placeID, String placeName, String identification,
                  String username, float noise, float visualInput,
                  float crowd, float smell, float light, String description, String uId, String occasion,
                  Rating rating) {
        this.placeID = placeID;
        this.placeName = placeName;
        this.noise = noise;
        this.visualInput = visualInput;
        this.identification = identification;
        this.username = username;
        date = new Date();
        this.crowd = crowd;
        this.smell = smell;
        this.description = description;
        this.light = light;
        this.overall = overall;
        this.occasion = occasion;
        this.uId = uId;
        this.rating = rating;
    }

    public Review(){

    }

    public float getLight() {
        return light;
    }

    public String getIdentification() {
        return identification;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }


    public String getuId() {
        return uId;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setIdentification(String identification){
        this.identification = identification;
    }

    public Date getDate() {
        return date;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceID() {
        return placeID;
    }

    public float getCrowd() {
        return crowd;
    }

    public float getSmell() {
        return smell;
    }

    public float getVisualInput() {
        return visualInput;
    }

    public float getNoise() {
        return noise;
    }

    public void setCrowd(int crowd) {
        this.crowd = crowd;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLight(float light) {
        this.light = light;
    }

    public void setSmell(int smell) {
        this.smell = smell;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNoise(int noise) {
        this.noise = noise;
    }

    public void setVisualInput(int visualInput) {
        this.visualInput = visualInput;
    }

    public float getOverall(){
        return (smell+noise+light+crowd+visualInput)/5;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
