package com.example.biblioloft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.biblioloft.fragmentsAdmin.ProfileAdminFragment;
import com.example.biblioloft.fragmentsUser.SettingsFragment;
import com.example.biblioloft.R;
import com.example.biblioloft.fragmentsAdmin.HomeAdminFragment;
import com.example.biblioloft.fragmentsAdmin.RegistroLibrosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        BottomNavigationView bottomNav =(BottomNavigationView)findViewById(R.id.admin_bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeAdminFragment()).commit();
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeAdminFragment();
                        break;

                    case R.id.nav_challenge:
                        fragment = new RegistroLibrosFragment();
                        break;

                    case R.id.nav_profile:
                        fragment = new ProfileAdminFragment();
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