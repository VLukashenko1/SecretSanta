package com.example.vital.myapplication555.model;

import java.net.URL;

class UserModel {
    String id, displayName, askToFriends, Friends, email, nickName;
    URL PhotoUrl;
    public UserModel(String id, String displayName, String askToFriends, String friends, String email, String nickName, URL photoUrl) {
        this.id = id;
        this.displayName = displayName;
        this.askToFriends = askToFriends;
        Friends = friends;
        this.email = email;
        this.nickName = nickName;
        PhotoUrl = photoUrl;
    }
}
