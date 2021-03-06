package com.example.vital.myapplication555.WorkWithDB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.vital.myapplication555.Exceptions.InitializeMainActivityException;
import com.example.vital.myapplication555.MainActivity;
import com.example.vital.myapplication555.Models.UserHolder;
import com.example.vital.myapplication555.Models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.net.MalformedURLException;

import androidx.annotation.NonNull;

public class UserFiller {

    private static final String TAG = "PutUser";
    DbHelper dbHelp = new DbHelper();

    public void setDocument(DocumentSnapshot document) {
        this.document = document;
    }
    DocumentSnapshot document;

    public void setDefaultData(){
        dbHelp.currentUserRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    UserModel userModel = new UserModel(documentSnapshot);

                    boolean result = tryToInitializeMainActivity(userModel);

                    if (!result)
                        throw new InitializeMainActivityException();

                    result = tryToInitializeFriendsList(userModel);
                    if (!result)
                        return;


                } catch (MalformedURLException | InitializeMainActivityException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    void startActivity(Intent intent){
            startActivity(intent);
    }
    private boolean tryToInitializeFriendsList(UserModel um) {
        try {
                //toast..


            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private boolean tryToInitializeMainActivity(UserModel um) {
        try {

                //toast..

            return true;
        }
        catch (Exception e){
            return false;
        }
    }



}
