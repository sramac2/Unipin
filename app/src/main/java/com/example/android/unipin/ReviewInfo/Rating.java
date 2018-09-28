package com.example.android.unipin.ReviewInfo;

public class Rating {
    private float overall;
    private float smell;
    private float noise;
    private float visual;
    private float crowd;
    private float light;
    private String placeId;

    public Rating(float overall, float smell, float noise, float visual, float crowd, float light
            , String placeId) {
        this.overall = overall;
        this.smell = smell;
        this.noise = noise;
        this.visual = visual;
        this.crowd = crowd;
        this.light = light;
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Rating(){}

    public float getOverall() {
        return overall;
    }

    public void setOverall(float overall) {
        this.overall = overall;
    }

    public float getSmell() {
        return smell;
    }

    public void setSmell(float smell) {
        this.smell = smell;
    }

    public float getNoise() {
        return noise;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }

    public float getVisual() {
        return visual;
    }

    public void setVisual(float visual) {
        this.visual = visual;
    }

    public float getCrowd() {
        return crowd;
    }

    public void setCrowd(float crowd) {
        this.crowd = crowd;
    }

    public float getLight() {
        return light;
    }

    public void setLight(float light) {
        this.light = light;
    }


    public boolean equals(Rating rating){
        return rating.getOverall()==overall&&rating.getCrowd()==crowd&&rating.getLight()==light
                &&rating.getNoise()==noise&&rating.getVisual()==visual&&rating.getSmell()
                ==smell;
    }
}
