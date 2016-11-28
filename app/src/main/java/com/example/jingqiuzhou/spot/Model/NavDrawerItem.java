package com.example.jingqiuzhou.spot.Model;

public class NavDrawerItem {
    private String title;
    private int icon;
    private String count = "0";
    //boolean to set visibility of counter
    private boolean counterVisible = false;

    public NavDrawerItem() {}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public NavDrawerItem(String title, int icon, boolean visible, String count){
        this.title = title;
        this.icon = icon;
        this.counterVisible = visible;
        this.count = count;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getCount(){
        return this.count;
    }

    public boolean isCounterVisible(){
        return this.counterVisible;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public void setCount(String count){
        this.count = count;
    }

    public void setCounterVisibility(boolean isCounterVisible){
        this.counterVisible = isCounterVisible;
    }
}
