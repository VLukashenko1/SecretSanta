package com.example.vital.myapplication555.WorkWithDB;

import android.content.Intent;

import com.example.vital.myapplication555.InBoxActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

//      Useless class

public class UsersInBoxUSELESS {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public HashMap<Integer, String> userIds = new HashMap<>();
    public String[] userNames;
    int iterator = 0;

    void pushToHashMap(String userId){
        if (userId == null){
            return;
        }
        userIds.put(iterator, userId);
        iterator++;
    }
    void pushToHashMap(DocumentSnapshot documentSnapshot){
        List<String> group = (List<String>) documentSnapshot.get("ListOfUsers");
        if (group == null){
            return;
        }
        for (int i = 0; i < group.size() ; i++) {
            userIds.put(iterator, group.get(i));
            iterator++;
        }
    }

    public void startFindUsersInBox(String idOfBox){
    db.collection("Boxes").document(idOfBox).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            if (documentSnapshot.getData().get("IdOfCreator") != null){
                pushToHashMap(documentSnapshot.getData().get("IdOfCreator").toString());
            }
            if (documentSnapshot.getData().get("ListOfUsers") != null){
                pushToHashMap(documentSnapshot);
            }
            makeListWithNames();
            userNames = new String[userIds.size()];
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    });

}
    void findNames(DocumentSnapshot documentSnapshot){
            for (int i = 0; i < userIds.size() ; i++) {
             if (userIds.get(i).equals(documentSnapshot.getId())){
                  userNames[i] = documentSnapshot.getData().get("DisplayName").toString();
                }
            }
        }
    void makeListWithNames(){
        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                findNames(document);
                            }
                            if (userNames != null && userIds != null){
                                InBoxActivity inBoxActivity = new InBoxActivity();
                                inBoxActivity.makeList(userIds,userNames);
                            }
                        }
                        else {

                        }
                    }
                });
    }

}
