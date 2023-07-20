package com.internet.speedtest.speedcheck.nvboost.model;

public class NextScreen {


    String Title1,Title2, Description;
    int NextImg;


    public String getTitle1() {
        return Title1;
    }

    public void setTitle1(String title1) {
        Title1 = title1;
    }
    public String getTitle2() {
        return Title2;
    }

    public void setTitle2(String title2) {
        Title1 = title2;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenImg() {
        return NextImg;
    }

    public void setScreenImg(int screenImg) {
        NextImg = screenImg;
    }



    public NextScreen(String title1, String title2, String description, int screenImg){
        Title1 =title1;
        Title2 =title2;
        Description = description;
        NextImg = screenImg;
//        NextSlideImg = nextSlideImg;
    }

}
