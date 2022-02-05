package com.example.vital.myapplication555;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class CreateBox extends AppCompatActivity {
FirebaseFirestore db = FirebaseFirestore.getInstance();
FirebaseAuth auth = FirebaseAuth.getInstance();

Button createBoxButton, connectToBoxButton;
public TextInputEditText nameOfBoxInput, connectIdInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_box);

        //TextInput
        connectIdInput = findViewById(R.id.connectToBox);
        nameOfBoxInput = findViewById(R.id.createBoxName);
        //Buttons
        createBoxButton = findViewById(R.id.createNewBoxButton);
        createBoxButton.setOnClickListener(view -> createboxStarter());

        connectToBoxButton = findViewById(R.id.connectToBoxButton);
        connectToBoxButton.setOnClickListener(view -> connectToBoxStarter());
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    // Create new box
    void createboxStarter(){
        String input = Objects.requireNonNull(nameOfBoxInput.getText()).toString();
        if (input.isEmpty()){
            makeToast("Заповніть поле з назвою для коробки");
            return;
        }
        else {
            createBox(nameOfBoxInput.getText().toString());
        }
    }
    void createBox(String nameOfBox){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("NameOfCreator", auth.getCurrentUser().getDisplayName().toString());
        hashMap.put("IdOfCreator", auth.getCurrentUser().getUid());
        hashMap.put("NameOfBox", nameOfBox);
        hashMap.put("ListOfUsers", Arrays.asList());

        db.collection("Boxes").add(hashMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                makeToast("Operation success");
                changeActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

//Connect to Box
    void connectToBoxStarter(){ //Starter
        getInputForConnectToBox();
    }
    void getInputForConnectToBox(){
        String input = connectIdInput.getText().toString();
        if (input.isEmpty() || input.equals(" ") || input == null){
            makeToast("Заповніть поле з назвою для коробки");
            return;
        }
        else {
            isDocExist(input);
        }
    }
    void isDocExist(String idOfBox){
        String userId = auth.getCurrentUser().getUid();
        HashMap<String, Object> forPush = new HashMap<>();
        forPush.put("ListOfUsers",Arrays.asList(userId));
        db.collection("Boxes").document(idOfBox).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getData().get("IdOfCreator").equals(userId)){
                    makeToast("Коробка створена вами, вас вже підключено");
                    return;
                }
                else if (documentSnapshot.getData() != null && !documentSnapshot.getData().get("IdOfCreator").equals(userId)){
                    addUserToBox(idOfBox, forPush);
                }
                else{
                    makeToast("Коробку не знайдено");
                    return;
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Коробку не знайдено");
            }
        });

    }
    public void makeHashMapForConnect(String idOfBox, String userId){
        HashMap<String, Object> forPush = new HashMap<>();
        forPush.put("ListOfUsers",Arrays.asList(userId));
        addUserToBox(idOfBox, forPush);
    }
    void addUserToBox(String idOfBox, HashMap<String, Object> forPush){
        db.collection("Boxes").document(idOfBox).set(forPush,  SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        makeToast("You connected to box");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Сталась помилка під час запису");
            }
        });
    }
//

   public void makeToast(String text){
        Toast.makeText(CreateBox.this, text, Toast.LENGTH_SHORT).show();
    }
   public void changeActivity(){
        Intent intent = new Intent(CreateBox.this, MainActivity.class);
        startActivity(intent);
    }

}