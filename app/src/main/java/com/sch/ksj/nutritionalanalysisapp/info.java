package com.sch.ksj.nutritionalanalysisapp;

import android.app.Application;

public class info extends Application {
    private String name;
    private int age, weight;
    // true면 남성 false면 여성
    private boolean gender;

    @Override
    public void onCreate(){
        name = "";
        age = 0;
        weight = 0;
        gender = true;
        super.onCreate();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAge (int age){
        this.age = age;
    }

    public void setWeight (int weight){
        this.weight = weight;
    }

    public void setGender (boolean gender){
        this.gender = gender;
    }

    public String getName(){
        return name;
    }

    public int getWeight(){
        return weight;
    }
    public int getAge(){
        return age;
    }
    public boolean getGender(){
        return gender;
    }

    private static info instance = null;

    public static synchronized info getInstance(){
        if(instance == null)
            instance = new info();
        return instance;
    }

}
