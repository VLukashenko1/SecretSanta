package com.example.vital.myapplication555.Models;

import android.util.Log;

import com.example.vital.myapplication555.WorkWithDB.DbHelper;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;

public class UserUpdate {
    private static final String TAG = "UserUpdate class:";

    void updateUser(){
        DbHelper.db.collection("GoogleUsers").whereEqualTo("Uid", DbHelper.auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    return;
                }
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            Log.d(TAG, "New user: " + dc.getDocument().getData());
                            break;
                        case MODIFIED:
                            CurrentUserFiller cuf = new CurrentUserFiller();
                            cuf.fill(DbHelper.auth.getCurrentUser().getUid());

                            Log.d(TAG, "Modified user: " + dc.getDocument().getData());
                            break;
                        case REMOVED:
                            CurrentUserFiller currentUserFiller = new CurrentUserFiller();
                            currentUserFiller.fill(DbHelper.auth.getCurrentUser().getUid());

                            Log.d(TAG, "Removed user: " + dc.getDocument().getData());
                            break;
                    }
                }

            }
        });
    }
}
