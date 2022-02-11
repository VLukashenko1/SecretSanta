package com.example.vital.myapplication555.WorkWithDB;

import com.example.vital.myapplication555.Models.CurrentUserFiller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class PushUserToFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


   public void findUser(){
        db.collection("GoogleUsers").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() == null){
                    createUserMap();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    static HashMap<String, Object> user = new HashMap<>();
    void createUserMap(){
        String[]a = new String[0];
        user.put("DisplayName", auth.getCurrentUser().getDisplayName());
        user.put("PhotoUrl", auth.getCurrentUser().getPhotoUrl().toString());
        user.put("Uid", auth.getCurrentUser().getUid());
        user.put("Email", auth.getCurrentUser().getEmail());
        user.put("NickName", "");
        user.put("AskToFriends", Arrays.asList());
        user.put("Friends", Arrays.asList());
        sendToFirebase(user);
    }
    void sendToFirebase(HashMap<String,Object> hashMap){
        db.collection("GoogleUsers").document(hashMap.get("Uid").toString()).set(hashMap);
    }
}