package com.example.vital.myapplication555.UserProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital.myapplication555.CreateBox;
import com.example.vital.myapplication555.InBoxActivity;
import com.example.vital.myapplication555.R;
import com.example.vital.myapplication555.WorkWithDB.RequestToFriends;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class RequestToFriendsAct extends AppCompatActivity {
FirebaseAuth auth = FirebaseAuth.getInstance();
FirebaseFirestore db = FirebaseFirestore.getInstance();

TextView friendRequestTextView, yourFriendsTextVw;
ListView friendsRequestList, friendsList;

String isFromInBox, idOfBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);

        isFromInBox = getIntent().getStringExtra("Прийшло");
        idOfBox = getIntent().getStringExtra("IdOfBox");

        friendRequestTextView = findViewById(R.id.friendRequestTextView);
        friendsRequestList = findViewById(R.id.friendsRequestList);
        yourFriendsTextVw = findViewById(R.id.yourFriendsTextVw);
        friendsList = findViewById(R.id.friendsList);

        findFriendRequest();
        findFriendsId();
    }
// знайти та відобразити запити до друзів
    public void setFriendsRequestID(List<String> friendsRequestIdList) {
        if (friendsRequestIdList == null){
            friendRequestTextView.setText("Запити в друзі: \nЗапитів немає");
            return;
        }
        String  buf[] = new String[friendsRequestIdList.size()];
        for (int i = 0; i < friendsRequestIdList.size(); i++) {
            buf[i] = friendsRequestIdList.get(i);
        }
        setFriendsRequestID(buf);
        friendRequestNames = new String[buf.length];
    }
    public void setFriendsRequestID(String[] friendsRequestID) {
        this.friendsRequestID = friendsRequestID;
    }
    public void setFriendRequestNames(String data, int id){
        this.friendRequestNames[id] = data;
    }

    String[]friendsRequestID;
    String[]friendRequestNames;

    void findFriendRequest(){
        db.collection("GoogleUsers").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData().get("AskToFriends") != null){
                    makeIdList(documentSnapshot);
                }
                else if(documentSnapshot.getData().get("AskToFriends") == null
                        || Objects.requireNonNull(documentSnapshot.getData().get("AskToFriends")).toString().isEmpty()){
                    friendRequestTextView.setText("Запити в друзі: \nЗапитів немає");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    void makeIdList(DocumentSnapshot documentSnapshot){
        if(Objects.requireNonNull(documentSnapshot.get("AskToFriends")).toString().isEmpty() ||
            documentSnapshot.get("AskToFriends") == null){
            return;
        }
        setFriendsRequestID((List<String>) documentSnapshot.get("AskToFriends"));
        makeNameList();
    }
    void makeNameList(){
        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               fillNameArrById(document.getId(), document.getData().get("DisplayName").toString());
                            }
                            showRequestFriendsList();
                        } else {

                        }
                    }
                });
    }
    void fillNameArrById(String forCheck, String displayName){
        for (int i = 0; i < friendsRequestID.length ; i++) {
            if (forCheck.equals(friendsRequestID[i])){
                setFriendRequestNames(displayName, i);
            }
        }
    }
    //Відображення
    void showRequestFriendsList(){
        if (friendRequestNames == null){
            return;
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(RequestToFriendsAct.this, android.R.layout.simple_list_item_1, friendRequestNames);
        friendsRequestList.setAdapter(adapter);
        registerForContextMenu(friendsRequestList);
        friendsRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

// Знайти та відобразити друзів:
    String[] friendsNamesArr;
    String[] friendsIdArr;

    void setElementToFriendsIdArr(String data){
        if (friendsIdArr == null){
            friendsIdArr = new String[1];
            friendsIdArr[0] = data;
            return;
        }
        String []buf = friendsIdArr;
        friendsIdArr = new String[friendsIdArr.length + 1];
        friendsIdArr = buf;
        friendsIdArr[friendsIdArr.length] = data;
    }
    void setElementToFriendsNamesArr(String data, int id){
        this.friendsNamesArr[id] = data;
        System.out.println("Кладу");
    }

    void findFriendsId(){
        db.collection("GoogleUsers").document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("Friends") != null){
                    pushToIdArr(documentSnapshot);
                }
            }
        });
    }
    void pushToIdArr(DocumentSnapshot documentSnapshot){
        if(documentSnapshot.get("Friends") != null &&
                Objects.requireNonNull(documentSnapshot.get("Friends")).toString().isEmpty()){
            yourFriendsTextVw.setText("Ваші друзі: \nДрузів ще не додано");
            return;
        }
        List<String> friendsIdList = (List<String>) documentSnapshot.get("Friends");
        if (friendsIdList != null){
            for (int i = 0; i < friendsIdList.size(); i++) {
                setElementToFriendsIdArr(friendsIdList.get(i));
            }
        }
        findFriendNames();
    }
    void findFriendNames(){
        friendsNamesArr = new String[friendsIdArr.length];
        db.collection("GoogleUsers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               addToNamesArr(document.getId(), document.getData().get("DisplayName").toString());
                            }
                            showFriends();
                        } else {

                        }
                    }
                });
    }
    void addToNamesArr(String id, String name){
        for (int i = 0; i < friendsIdArr.length; i++) {
            if (friendsIdArr[i].equals(id)){
                setElementToFriendsNamesArr(name, i);
            }
        }
    }

    void showFriends(){
        if (friendsNamesArr == null){
            return;
        }
        ArrayAdapter adapter = new ArrayAdapter<String>(RequestToFriendsAct.this, android.R.layout.simple_list_item_1, friendsNamesArr);
        friendsList.setAdapter(adapter);
        registerForContextMenu(friendsList);
        if (isFromInBox != null && idOfBox != null && isFromInBox.equals("InBoxActivity")){
            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                   CreateBox createBox = new CreateBox();
                   createBox.makeHashMapForConnect(idOfBox, friendsIdArr[i]);

                   Intent intent = new Intent(RequestToFriendsAct.this, InBoxActivity.class);
                   startActivity(intent);
                    }
                });
            }
    }
//
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);
        if (view.getId() == R.id.listViewFrFirst){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)contextMenuInfo;
            contextMenu.setHeaderTitle(friendRequestNames[info.position]);
            contextMenu.setHeaderTitle("Оберіть дію: ");
            contextMenu.add(0, view.getId(),0,"Додати до друзів");
            contextMenu.add(0, view.getId(), 0,"Відхилити заявку");
        }
        else if (view.getId() == R.id.friendsList){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)contextMenuInfo;
            contextMenu.setHeaderTitle(friendsNamesArr[info.position]);
            contextMenu.setHeaderTitle("Оберіть дію: ");
            contextMenu.add(0, view.getId(),0,"Видалити з друзів");
            contextMenu.add(0, view.getId(), 0,"Перейти до листування");
        }

    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle().equals("Додати до друзів")){
            makeText("Додаєм до друзів: " + friendRequestNames[info.position]);
            RequestToFriends requestToFriends = new RequestToFriends();
            requestToFriends.allowAddToFriends(auth.getCurrentUser().getUid(), friendsRequestID[info.position]);
            refresh();
        }
        else if(item.getTitle().equals("Відхилити заявку")){
            makeText("Видаляємо заявку: " + friendRequestNames[info.position]);
            RequestToFriends requestToFriends = new RequestToFriends();
            requestToFriends.deleteUserFromFriendsRequest(auth.getCurrentUser().getUid(), friendsRequestID[info.position]);
            refresh();
        }
        return super.onContextItemSelected(item);
    }
// Функції
    void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    void refresh(){
        finish();
        Intent intent = new Intent(RequestToFriendsAct.this, RequestToFriendsAct.class);
        startActivity(intent);
    }
}