package com.example.vital.myapplication555.Models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;

public class BoxModel {
    String boxName, boxId, idOfCreator;
    List<UserModel> userModelList;
    HashMap<String, String> uIdAndHisMessage;
    boolean isCreator;

    public BoxModel(String boxName, String boxId, String idOfCreator,
                    List<UserModel> userModelList, HashMap<String, String> uIdAndHisMessage, boolean isCreator) {
        this.boxName = boxName;
        this.boxId = boxId;
        this.idOfCreator = idOfCreator;
        this.userModelList = userModelList;
        this.uIdAndHisMessage = uIdAndHisMessage;
        this.isCreator = isCreator;
    }
    public BoxModel(DocumentSnapshot documentSnapshot){
        this.boxName = documentSnapshot.get("NameOfBox").toString();
        this.boxId = documentSnapshot.getId();
        this.idOfCreator = documentSnapshot.get("IdOfCreator").toString();
    }
}
