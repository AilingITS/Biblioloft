package com.example.scopeofreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.scopeofreading.Prevalent.Prevalent;
import com.example.scopeofreading.firebase.Users;
import com.example.scopeofreading.fragments.ChallengeFragment;
import com.example.scopeofreading.fragments.HomeFragment;
import com.example.scopeofreading.fragments.ProfileFragment;
import com.example.scopeofreading.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView bottomNav =(BottomNavigationView)findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_challenge:
                        fragment = new ChallengeFragment();
                        break;

                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;

                    case R.id.nav_settings:
                        fragment = new SettingsFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();

                return true;
            }
        });
    }
}