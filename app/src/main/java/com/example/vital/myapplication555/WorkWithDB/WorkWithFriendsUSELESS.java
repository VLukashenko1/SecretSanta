package com.example.vital.myapplication555.WorkWithDB;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class WorkWithFriendsUSELESS {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void addToFriendsByEmail(String uId, String email){
        findUserByEmail(uId, email);
    }
    void findUserByEmail(String uId, String email){
        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               if( document.getData().get("Email").equals(email) ){
                                   addToFriendByUid(uId, document.getId());
                               }
                            }
                        }
                        else {

                        }
                    }
                });
    }
    void addToFriendByUid(String uId, String uIdFriend){
        if (!uId.isEmpty() || !uIdFriend.isEmpty()){
            return;
        }
        HashMap<String, Object> friends= new HashMap<>();
        friends.put("AskToFriends", Arrays.asList(uIdFriend));
        db.collection("GoogleUsers").document(uId).set(friends,  SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //FriendsActivity friendsActivity = new FriendsActivity();
                        //friendsActivity.makeText("Ви надіслали заявку в друзі");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                return;
            }
        });
    }
}
