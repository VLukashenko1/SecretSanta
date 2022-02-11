package com.example.vital.myapplication555.Models;

import androidx.lifecycle.MutableLiveData;

public class UserHolder{

    UserModel2 um;
    public UserModel2 getUm() {
        return um;
    }
    public void setUm(UserModel2 um) {
        this.um = um;
    }

    String data;
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    private static final UserHolder holder = new UserHolder();
    public static UserHolder getInstance(){
        return holder;
    }

    //LiveData
    private MutableLiveData<UserModel2> userLiveData = new MutableLiveData<>();
    public MutableLiveData<UserModel2> getLiveUsr() {
        return userLiveData;
    }
}