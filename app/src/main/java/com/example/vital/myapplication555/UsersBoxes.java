package com.example.vital.myapplication555;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.vital.myapplication555.databinding.FragmentFirstBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;

public class UsersBoxes extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState

    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        findUserBOxes();
    }
    void findUserBOxes(){
        showUserInfo();//show  Name & Photo
        showBoxCreatedByCurrentUser(); // make list where user included
    }
    void showUserInfo(){
        String userName = auth.getCurrentUser().getDisplayName().toString();
        if (userName != null) {
            binding.textviewFirst.setText(userName);
        }
        ImageView imageView = binding.imageViewFirst;
        Glide.with(UsersBoxes.this).load(auth.getCurrentUser().getPhotoUrl()).into(imageView);

    }

    static HashMap<Integer, String> boxName = new HashMap<>();
    static HashMap<Integer, String> boxId = new HashMap<>();


    void showBoxCreatedByCurrentUser(){
        db.collection("Boxes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int a = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("IdOfCreator").equals(auth.getCurrentUser().getUid())){
                                    boxName.put(a, document.getData().get("NameOfBox").toString());
                                    boxId.put(a, document.getId());
                                    a++;
                                }
                                if(document.getData().get("ListOfUsers") != null){
                                    if (showBoxWhereUserIncluded(document)){
                                        boxName.put(a, document.getData().get("NameOfBox").toString());
                                        boxId.put(a, document.getId());
                                        a++;
                                    }
                                }
                            }
                            hashMapToStringArray(boxName, boxId);
                        }
                        else {
                            binding.msgForList.setText("Ви не підключені до жодної коробки");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast(e.getMessage().toString());
            }
        });
    }
    boolean showBoxWhereUserIncluded(QueryDocumentSnapshot documentSnapshot){
        String uId = auth.getCurrentUser().getUid();
                if(Arrays.asList(documentSnapshot.getData().get("ListOfUsers")).toString().contains(uId)){
                    System.out.println("TEST: " + Arrays.asList(documentSnapshot.getData().get("ListOfUsers")).toString().isEmpty());
                    return true;
                }
        return false;
    }
    void hashMapToStringArray(HashMap<Integer, String> boxName, HashMap<Integer, String> boxId){
        if (boxId.size() != 0){
            String boxes[] = new String[boxId.size()];
            for (int i = 0; i < boxId.size(); i++) {
                boxes[i] = boxName.get(i);
            }
            makeListView(boxName, boxId, boxes);
        }else {
            binding.msgForList.setText("Ви не підключені до жодної коробки");
        }

    }
    void makeListView(HashMap<Integer, String> boxName, HashMap<Integer, String> boxId, String[] names){
        Context context = getContext();
        if (names == null){
            makeToast("Оновіть сторінку, коробки не встигають за вами");
            return;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, names);
        binding.listViewFrFirst.setAdapter(adapter);
        binding.listViewFrFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), InBoxActivity.class);
                intent.putExtra("NameOfBox", boxName.get(i));
                intent.putExtra("IdOfBox", boxId.get(i));
                startActivity(intent);
            }
        });

    }
    void makeToast(String text){
        if (text != null){
            Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(),"Помилка",Toast.LENGTH_SHORT).show();
        }

    }
}
