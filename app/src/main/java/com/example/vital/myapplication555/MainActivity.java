package com.example.vital.myapplication555;

import android.content.Intent;
import android.os.Bundle;

import com.example.vital.myapplication555.Models.UserModel;
import com.example.vital.myapplication555.UserProfile.AddFriendsActivity;
import com.example.vital.myapplication555.UserProfile.FriendsAct;
import com.example.vital.myapplication555.UserProfile.MyProfileAct;
import com.example.vital.myapplication555.WorkWithDB.PushUserToFirebase;
import com.example.vital.myapplication555.WorkWithDB.PutCurrentUserToLocalModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vital.myapplication555.databinding.ActivityMainBinding;
import com.google.firebase.firestore.DocumentSnapshot;

import android.view.Menu;
import android.view.MenuItem;

import java.net.MalformedURLException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Створити нову коробку, чи приэднатись до готової", Snackbar.LENGTH_LONG)
                        .setAction("Go", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(MainActivity.this, CreateBox.class));
                            }
                        }).show();
            }
        });

        PushUserToFirebase pushUserToFirebase = new PushUserToFirebase();
        pushUserToFirebase.findUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        PutCurrentUserToLocalModel put = new PutCurrentUserToLocalModel();
        if (put.work() == null){
            System.out.println("MISTAKES");
        }
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AddFriendsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.myFriends){
            Intent intent = new Intent(this, FriendsAct.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.myProfile){
            Intent intent = new Intent(this, MyProfileAct.class);
            Intent intentForBack = new Intent(this, MainActivity.class);
            intent.putExtra("whereWasStarted", intentForBack);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}