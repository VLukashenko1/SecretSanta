package com.example.vital.myapplication555.WorkWithDB;

import com.example.vital.myapplication555.Models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;


import java.net.MalformedURLException;

public class PutCurrentUserToLocalModel {
    UserModel um;
    DbHelper dbHelp = new DbHelper();

    public UserModel work(){
        dbHelp.currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    um = new UserModel(documentSnapshot);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        return um;
    }

}
