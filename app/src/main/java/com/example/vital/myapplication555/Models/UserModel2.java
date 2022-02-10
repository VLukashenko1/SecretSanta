package com.example.vital.myapplication555.Models;

import com.example.vital.myapplication555.WorkWithDB.DbHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class UserModel2 implements Serializable{
    DbHelper dbHelper = new DbHelper();

    String id;
    String displayName, email, nickName;
    String askToFriends[];
    String friends[];
    List<UserModel2> askToFriendsList;
    List<UserModel2> friendsList;
    URL PhotoUrl;


    public UserModel2(DocumentSnapshot document) throws MalformedURLException {
        this.id = document.getId();
        this.displayName = document.getData().get(dbHelper.DISPLAY_NAME).toString();
        this.email = document.getData().get(dbHelper.EMAIL).toString();
        this.nickName = document.getData().get(dbHelper.NICKNAME).toString();

        this.PhotoUrl = new URL(document.getData().get(dbHelper.PHOTO_URL).toString());

        List<String> askFriendsList = (List<String>) document.get(dbHelper.ASK_TO_FRIENDS);
        this.askToFriends = askFriendsList.toArray(new String[0]);

        List<String> friendsIdList = (List<String>) document.get(dbHelper.FRIENDS);
        this.friends = friendsIdList.toArray(new String[0]);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "UserModel2{" +
                ", id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", askToFriends=" + Arrays.toString(askToFriends) +
                ", friends=" + Arrays.toString(friends) +
                ", PhotoUrl=" + PhotoUrl +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String[] getAskToFriends() {
        return askToFriends;
    }

    public String[] getFriends() {
        return friends;
    }

    public List<UserModel2> getAskToFriendsList() {
        return askToFriendsList;
    }

    public List<UserModel2> getFriendsList() {
        return friendsList;
    }

    public URL getPhotoUrl() {
        return PhotoUrl;
    }

}
