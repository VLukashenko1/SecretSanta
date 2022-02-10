package com.example.vital.myapplication555.WorkWithDB;

// database field names

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DbHelper {
    //collections
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public CollectionReference usersCollectionRef = db.collection("GoogleUsers");
    public CollectionReference boxesCollectionRef = db.collection("Boxes");

    // Current user info
        final DocumentReference currentUserRef = db.collection("GoogleUsers").document(auth.getCurrentUser().getUid());
        final DocumentReference newUserRef = db.collection("GoogleUsers").document();
    //?

    // Назви полів документу в базі даних колекції GoogleUsers
    public static final String ASK_TO_FRIENDS = "AskToFriends";
    public static final String DISPLAY_NAME = "DisplayName";
    public static final String EMAIL = "Email";
    public static final String FRIENDS = "Friends";
    public static final String NICKNAME = "NickName";
    public static final String PHOTO_URL = "PhotoUrl";
    public static final String USER_ID = "Uid";

    // Назви полів документу в базі даних колекції box
    public static final String ID_OF_CREATOR = "IdOfCreator";
    public static final String LIST_OF_USERS = "ListOfUsers";
    public static final String NAME_OF_BOX = "NameOfBox";
    public static final String NAME_OF_CREATOR = "NameOfCreator"; // ?

}
