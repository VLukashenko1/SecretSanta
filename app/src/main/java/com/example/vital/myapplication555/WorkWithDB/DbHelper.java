package com.example.vital.myapplication555.WorkWithDB;

// database field names

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DbHelper {
    //collections

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference usersCollectionRef = db.collection("GoogleUsers");
    public CollectionReference boxesCollectionRef = db.collection("Boxes");

    // Current user info
        final DocumentReference currentUserRef = db.collection("GoogleUsers").document(uid);
        final DocumentReference newUserRef = db.collection("GoogleUsers").document();
    //?

    // GoogleUsers
    public static final String asktofriends = "AskToFriends";
    public static final String displayname = "DisplayName";
    public static final String email = "Email";
    public static final String friends = "Friends";
    public static final String nickname = "NickName";
    public static final String photourl = "PhotoUrl";
    public static final String uid = "Uid";

    //box
    public static final String idofcreator = "IdOfCreator";
    public static final String listofusers = "ListOfUsers";
    public static final String nameofbox = "NameOfBox";
    public static final String nameofcreator = "NameOfCreator"; // ?

}
