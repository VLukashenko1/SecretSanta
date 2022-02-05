package com.example.vital.myapplication555;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vital.myapplication555.UserProfile.RequestToFriendsAct;
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

public class InBoxActivity extends AppCompatActivity {
    ImageButton back, addFriendToBoxButton;
    ListView listViewOnActBox;
    TextView nameOfBoxInBoxAct;

    String nameOfCurrentBox;
    String idOfCurrentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_box);

        nameOfCurrentBox = getIntent().getStringExtra("NameOfBox");
        idOfCurrentBox = getIntent().getStringExtra("IdOfBox");

        addFriendToBoxButton = findViewById(R.id.addFriendToBoxButton);
        addFriendToBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InBoxActivity.this, RequestToFriendsAct.class);
                intent.putExtra("Прийшло", "InBoxActivity");
                intent.putExtra("IdOfBox", idOfCurrentBox);
                intent.putExtra("NameOfBox", nameOfCurrentBox);
                startActivity(intent);
            }
        });
        back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
           Intent intent = new Intent(InBoxActivity.this, MainActivity.class);
           startActivity(intent);
        });
        listViewOnActBox = findViewById(R.id.listViewOnActBox);
        nameOfBoxInBoxAct = findViewById(R.id.nameOfBoxInBoxAct);

        setText();
        startFindUsersInBox(idOfCurrentBox);

    }
   void setText(){
        nameOfBoxInBoxAct.setText(nameOfCurrentBox);
    }
   public void makeList (HashMap<Integer,String> ids, String []userNames){
        ArrayAdapter adapter = new ArrayAdapter<>(InBoxActivity.this, android.R.layout.simple_list_item_1, userNames);
        listViewOnActBox.setAdapter(adapter);
        listViewOnActBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


//!!
FirebaseFirestore db = FirebaseFirestore.getInstance();
//
    HashMap<Integer, String> userIds = new HashMap<>();
    String[] userNames;
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
//Starter
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
                                makeList(userIds, userNames);
                            }
                        }
                        else {

                        }
                    }
                });
    }
}