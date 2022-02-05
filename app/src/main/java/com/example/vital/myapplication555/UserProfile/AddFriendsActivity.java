package com.example.vital.myapplication555.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital.myapplication555.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;

public class AddFriendsActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView mainTextFriends, resultTextView;
    TextInputEditText inputForFindByEmail;
    Button addToFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        mainTextFriends = findViewById(R.id.mainTextFriends);// Заголовок

        resultTextView = findViewById(R.id.resultTextView);
        resultTextView.setVisibility(View.INVISIBLE);

        inputForFindByEmail = findViewById(R.id.inputForFindByEmail);

        addToFriend = findViewById(R.id.addToFriendButton);
        addToFriend.setOnClickListener(view -> getEmailFromInput());

    }
//Пошук користувача за введеним e-mail
    void getEmailFromInput(){
    String inputEmail = inputForFindByEmail.getText().toString();
    if (!inputEmail.isEmpty() ){
        findUserByEmailInDB(auth.getCurrentUser().getUid(), inputEmail);
    }
    else {
        makeText("Заповніть поле для пошуку" + inputEmail);
    }
}
    void findUserByEmailInDB(String uId, String email){
    db.collection("GoogleUsers")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    findUser(task, uId, email);
                }
            });
    }
    void findUser(Task<QuerySnapshot> task, String uId, String email){
        if (task.isSuccessful()) {
            boolean tester = false;
            for (QueryDocumentSnapshot document : task.getResult()) {
                if( document.getData().get("Email").toString().equals(email)
                   || document.getData().get("NickName").toString().equals(email)) {
                    tester = true;
                    isFoundUserValid(uId, document.getId());
                }
            }
            if (tester == false){
                makeText("Користувача з таким e-mail/nick не знайдено");
            }
        }
        else {
            makeText("Не вдалось виконати пошук");
        }
    }
    void isFoundUserValid(String uId, String uIdFriend){
    if(uIdFriend == null){
        makeText("Користувача не знайдено");
        return;
    }
    else if(uId.equals(uIdFriend)){
        makeText("Ви ввели свій e-mail");
        return;
    }
        isUsersAlreadyFriend(uId, uIdFriend);
}
    //
    void isUsersAlreadyFriend(String uId, String uIdFriend){
        db.collection("GoogleUsers").document(uId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("Friends") != null && documentSnapshot.get("Friends").toString().contains(uIdFriend)){
                    makeText("Користувач вже у вас в друзях");
                    resultTextView.setText("Цей користувач вже у вас в друзях");
                    resultTextView.setVisibility(View.VISIBLE);
                    inputForFindByEmail.setText("");
                }else {
                    addToFriendByUid(uId, uIdFriend);
                }
            }
        });

    }
//Надіслати запит в друзі за id
    void addToFriendByUid(String uId, String uIdFriend){
    HashMap<String, Object> friends = new HashMap<>();
    String []uIDFriend = new String[1];
    uIDFriend[0] = uId;
    friends.put("AskToFriends", Arrays.asList(uIDFriend));
    db.collection("GoogleUsers").document(uIdFriend).set(friends,  SetOptions.merge())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    makeText("Ви надіслали заявку в друзі");
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            return;
        }
    });
}
//
    public void makeText(String text){
        if (text.equals("Користувача з таким e-mail не знайдено") ||
                text.equals("Користувача не знайдено") ||
                text.equals("Заповніть поле e-mail для пошуку"))  {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            resultTextView.setText("Користувача не знайдено");
            resultTextView.setVisibility(View.VISIBLE);
            return;
        }
        else if(text.equals("Ви ввели свій e-mail")){
            resultTextView.setText("Ви ввели свій e-mail/nick");
            inputForFindByEmail.setText("");
            resultTextView.setVisibility(View.VISIBLE);
        }
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}