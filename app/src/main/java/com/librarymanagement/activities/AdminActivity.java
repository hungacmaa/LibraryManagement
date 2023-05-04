package com.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.librarymanagement.R;
import com.librarymanagement.fragments.admin.AdminAddBookFragment;
import com.librarymanagement.fragments.admin.AdminDashBoardFragment;
import com.librarymanagement.fragments.admin.AdminEditBookDetailFragment;
import com.librarymanagement.fragments.admin.AdminProfileFragment;

public class AdminActivity extends AppCompatActivity {

    private FrameLayout adminFrameLayout;
    private BottomNavigationView adminBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initView();

        setUpBottomNav();

    }

    private void setUpBottomNav() {

        Menu menu = adminBottomNavigation.getMenu();

        // Setting the default fragment as AddBookFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameLayout, new AdminAddBookFragment()).commit();

        // Handle event when select a menu item
        adminBottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //Assigning Fragment as Null
                Fragment selectedFragment = null;
                switch (item.getItemId()) {

                    //Shows the Appropriate Fragment by using id as address
                    case R.id.AdminAddBook:
                        selectedFragment = new AdminAddBookFragment();
                        break;

                    case R.id.AdminEditDetail:
                        selectedFragment = new AdminEditBookDetailFragment();
                        break;

                    case R.id.AdminDashBoard:
                        selectedFragment = new AdminDashBoardFragment();
                        break;

                    case R.id.AdminProfile:
                        selectedFragment = new AdminProfileFragment();
                        break;

                }
                //Sets the selected Fragment into the Framelayout
                getSupportFragmentManager().beginTransaction().replace(R.id.adminFrameLayout, selectedFragment).commit();
                return true;
            }
        });
    }

    private void initView() {

        adminFrameLayout = findViewById(R.id.adminFrameLayout);
        adminBottomNavigation = findViewById(R.id.adminBottomNavigation);

    }
}