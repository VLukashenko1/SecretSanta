package com.example.vital.myapplication555.Models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.vital.myapplication555.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class CurrentUserFiller {
    private static final String TAG = "CurrentUserFiller";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String fromWhere;


    UserModel2 um;
    public void fill(String id, String fromWhere) {
        this.fromWhere = fromWhere;
        db.collection("GoogleUsers").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    um = new UserModel2(documentSnapshot);
                    fillFriends(um);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    List<UserModel2> friends = new ArrayList<>();
    public void fillFriends(UserModel2 um){

        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    findUserFriends(um, document);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            um.friendsList = friends;
                            saveData(um);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    void findUserFriends(UserModel2 um, QueryDocumentSnapshot doc) throws MalformedURLException {
        String []tempFriendsArr = um.friends;
        for (int i = 0; i < tempFriendsArr.length; i++) {
            if (tempFriendsArr[i].equals(doc.getId())){
                UserModel2 um2 = new UserModel2(doc);
                friends.add(um2);
            }
        }
    }
    void findUserAskToFriends(UserModel2 um, QueryDocumentSnapshot doc){

    }// fill
    UserModel2 userModel2;
    void saveData(UserModel2 um){
        userModel2 = um;
        saveDataToFile();
        switch (fromWhere){
            case "MainActivity":
                MainActivity mainActivity = new MainActivity();
                mainActivity.tempUserSaver(um);
                System.out.println("Call from main activity");
                break;
            case "UserBoxes":
                System.out.println("Call from User Boxes");
                break;
        }

    }
    void saveDataToFile() {

    }



}
