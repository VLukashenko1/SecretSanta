package com.example.vital.myapplication555.Models;

import java.net.URL;
import java.util.HashMap;

public class UserModel {
    String id;
    String displayName, email, nickName;
    String askToFriends[];
    String friends[];
    URL PhotoUrl;
    public UserModel(HashMap<String, Object> user){

    }

    public UserModel(String id, String displayName, String email, String nickName,
                     String[] askToFriends, String[] friends, URL photoUrl) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.nickName = nickName;
        this.askToFriends = askToFriends;
        this.friends = friends;
        PhotoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String[] getAskToFriends() {
        return askToFriends;
    }

    public void setAskToFriends(String[] askToFriends) {
        this.askToFriends = askToFriends;
    }

    public String[] getFriends() {
        return friends;
    }

    public void setFriends(String[] friends) {
        this.friends = friends;
    }

    public URL getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(URL photoUrl) {
        PhotoUrl = photoUrl;
    }

}
