package com.example.vital.myapplication555.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vital.myapplication555.InBoxActivity;
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

import java.util.HashMap;

public class MyProfileAct extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView usernameInMyProfile, userEmail, nickNameProfile;
    TextInputEditText inputNickName;
    ImageView userPhotoInMyProfile;
    Button setNickMyProfileButton;

    static String userId, userName, nickName, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
// TexView
        usernameInMyProfile = findViewById(R.id.usernameInMyProfile);
        userEmail = findViewById(R.id.userEmail);
        userEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(userEmail.getText() != null){
                    setClipboard(MyProfileAct.this, auth.getCurrentUser().getEmail());
                    userEmail.setText("Скопійовано");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            userEmail.setText("E-mail: " + auth.getCurrentUser().getEmail());
                        }
                    }, 1000);
                }
                return false;
            }
        });
        nickNameProfile = findViewById(R.id.nickNameProfile);
        nickNameProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (nickNameProfile.getText().toString() != null && !nickNameProfile.getText().equals("Nick: "+"відсутній")){

                }
                return false;
            }
        });
// InputText
        inputNickName = findViewById(R.id.inputNicknameMyProfile);
// ImageView
        userPhotoInMyProfile = findViewById(R.id.userPhotoOnMyProfile);
// Buttons
        setNickMyProfileButton = findViewById(R.id.setNickMyProfileButton);
        setNickMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTextFromNickNameInput();
            }
        });
//
        buildUserInfo();
        findUserNick();

    }
    void findUserNick(){
        db.collection("GoogleUsers").document(userId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getData().get("NickName") == null){
                            fillUserProfile();
                        }
                        else if(documentSnapshot.getData().get("NickName") != null){
                            setNickName(documentSnapshot.getData().get("NickName").toString());
                            fillUserProfile();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public static void setNickName(String nickName) {
        MyProfileAct.nickName = nickName;
    }

    void buildUserInfo(){
        userName = auth.getCurrentUser().getDisplayName();
        userId = auth.getCurrentUser().getUid();
        email = auth.getCurrentUser().getEmail();
        nickName = null;
    }
    void fillUserProfile(){
        //Встановлення фото
        Glide.with(MyProfileAct.this).load(auth.getCurrentUser().getPhotoUrl()).into(userPhotoInMyProfile);
        userPhotoInMyProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //
        String nick = nickName;
        if (nickName == null){
            nick = "відсутній";
        }else if (nickName != null){
            nick = nickName;
        }
        usernameInMyProfile.setText(userName);
        userEmail.setText("E-mail: " + email);
        nickNameProfile.setText("Nick: " + nick);
    }
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {

            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
            context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
            context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    void getTextFromNickNameInput(){
        if (inputNickName.getText() == null){
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("NickName", inputNickName.getText().toString());

        findNickNameRepeat(hashMap);
    }
    void findNickNameRepeat(HashMap hashMap){
        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("NickName") != null
                                    && document.getData().get("NickName").toString().equals(hashMap.get("NickName"))){
                                        makeText("NickName зайнято");
                                        inputNickName.setText(null);
                                        return;
                                    }
                            }
                            setUserNickName(hashMap);
                        } else {

                        }
                    }
                });
    }
    void setUserNickName(HashMap hashMap){
        db.collection("GoogleUsers").document(userId).set(hashMap, SetOptions.merge());
        Intent intent = new Intent(this, MyProfileAct.class);
        finish();
        startActivity(intent);
    }

    void makeText(String text){
        Toast.makeText(MyProfileAct.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}