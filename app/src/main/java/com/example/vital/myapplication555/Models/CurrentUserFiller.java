package com.example.vital.myapplication555.Models;

import android.util.Log;

import com.example.vital.myapplication555.WorkWithDB.DbHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CurrentUserFiller {
    private static final String TAG = "CurrentUserFiller";

    public void fill(String id) {
        DbHelper.db.collection("GoogleUsers").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    UserModel2 um = new UserModel2(documentSnapshot);
                    findFriends(um);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    List<UserModel2> friends = new ArrayList<>();
    List<UserModel2> askToFriends = new ArrayList<>();

    void findFriends(UserModel2 um){
        DbHelper.db.collection("GoogleUsers")
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
                            um.askToFriendsList = askToFriends;
                            saveDataToHolder(um);
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
        String [] tempAskToFriendsArr = um.askToFriends;
        for (int i = 0; i < tempAskToFriendsArr.length; i++) {
            if (tempAskToFriendsArr[i].equals(doc.getId())){
                UserModel2 um3 = new UserModel2(doc);
                askToFriends.add(um3);
            }
        }
    }

    void saveDataToHolder(UserModel2 um){
        UserHolder dataHolder = UserHolder.getInstance();
        dataHolder.setUm(um);
        //fill LiveData user model
        dataHolder.getLiveUsr().setValue(um);
    }
}
