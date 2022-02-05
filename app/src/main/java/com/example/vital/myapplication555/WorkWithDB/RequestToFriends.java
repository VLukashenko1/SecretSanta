package com.example.vital.myapplication555.WorkWithDB;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;

public class RequestToFriends {
FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void allowAddToFriends(String uId, String frId){
        moveToFriends(uId, frId);
        delUserFromFriendsRequest(uId, frId);
    }
    void moveToFriends(String uId, String frId){
        String [] currentUser = new String[1];
        currentUser[0] = uId;
        String [] friendUser = new String[1];
        friendUser[0] = frId;
        HashMap<String,Object> currentUserHashMap = new HashMap<>();
        HashMap<String,Object> friendUserHashMap = new HashMap<>();
        currentUserHashMap.put("Friends", Arrays.asList(friendUser));
        friendUserHashMap.put("Friends", Arrays.asList(currentUser));
        db.collection("GoogleUsers").document(uId).set(currentUserHashMap,  SetOptions.merge());
        db.collection("GoogleUsers").document(frId).set(friendUserHashMap,  SetOptions.merge());
    }
    public void delUserFromFriendsRequest(String uId, String frId){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("AskToFriends", FieldValue.arrayRemove(frId) );
        db.collection("GoogleUsers").document(uId).update(hashMap);

        //Видалити заявку в друга, якщо є
        HashMap<String,Object> frHashMap = new HashMap<>();
        frHashMap.put("AskToFriends", FieldValue.arrayRemove(uId) );
        db.collection("GoogleUsers").document(frId).update(frHashMap);
    }
}

