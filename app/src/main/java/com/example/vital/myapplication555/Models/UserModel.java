package com.example.vital.myapplication555.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.vital.myapplication555.WorkWithDB.DbHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class UserModel implements Parcelable {
    DbHelper dbHelper = new DbHelper();

    String id;
    String name, email, nickName;
    String askToFriends[];//dell
    String friends[];//dell++
    public HashMap<Integer, UserModel> hashMapWithFriends = new HashMap<>();
    public List<UserModel> friendsList = new ArrayList<UserModel>();
    public List<BoxModel> boxModels;
    URL PhotoUrl;

    public UserModel(DocumentSnapshot document) throws MalformedURLException {
        this.id = document.getId(); // почему єтот клас не юзаешь+
        this.name = document.getData().get(dbHelper.DISPLAY_NAME).toString();
        this.email = document.getData().get(dbHelper.EMAIL).toString();
        this.nickName = document.getData().get(dbHelper.NICKNAME).toString();
        this.PhotoUrl = new URL(document.getData().get(dbHelper.PHOTO_URL).toString());
        this.friendsList = getFriends(document);

        List<String> askFriendsList = (List<String>) document.get(dbHelper.ASK_TO_FRIENDS);
        this.askToFriends = askFriendsList.toArray(new String[0]);

        List<String> friendsIdList = (List<String>) document.get(dbHelper.FRIENDS);
        this.friends = friendsIdList.toArray(new String[0]);
    }
    public UserModel(DocumentSnapshot document, boolean isWithFriends ) throws MalformedURLException {
        this.id = document.getId();
        this.name = document.getData().get(dbHelper.DISPLAY_NAME).toString();
        this.email = document.getData().get(dbHelper.EMAIL).toString();
        this.nickName = document.getData().get(dbHelper.NICKNAME).toString();
        this.PhotoUrl = new URL(document.getData().get(dbHelper.PHOTO_URL).toString());

    }
    public UserModel(){};

    protected UserModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        email = in.readString();
        nickName = in.readString();
        askToFriends = in.createStringArray();
        friends = in.createStringArray();
        friendsList = in.createTypedArrayList(UserModel.CREATOR);
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    private List<UserModel> getFriends(DocumentSnapshot document) {
        List<UserModel> tempFriendsList = new ArrayList<UserModel>();
        List<String> friendsIdList = (List<String>) document.get(dbHelper.FRIENDS);

        for (String friendId:friendsIdList){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("GoogleUsers").document(friendId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    try {
                        UserModel tempModel = new UserModel(documentSnapshot,false);
                        tempFriendsList.add(tempModel);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return tempFriendsList;
    }



    @Override
    public String toString() {
        return "UserModel{" +
                "dbHelper=" + dbHelper +
                ", id='" + id + '\'' +
                ", displayName='" + name + '\'' +
                ", email='" + email + '\'' +
                ", nickName='" + nickName + '\'' +
                ", askToFriends=" + Arrays.toString(askToFriends) +
                ", friends=" + Arrays.toString(friends) +
                ", hashMapWithFriends=" + hashMapWithFriends +
                ", friendsList=" + friendsList +
                ", PhotoUrl=" + PhotoUrl +
                '}';
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(nickName);
        parcel.writeStringArray(askToFriends);
        parcel.writeStringArray(friends);
        parcel.writeTypedList(friendsList);
    }

    public String getName() {
        return name;
    }
}
