package com.librarymanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.librarymanagement.activities.AdminActivity;
import com.librarymanagement.activities.SplashScreenActivity;
import com.librarymanagement.constants.Constant;
import com.librarymanagement.fragments.user.UserDashBoardFragment;
import com.librarymanagement.fragments.user.UserHomeFragment;
import com.librarymanagement.fragments.user.UserNotificationFragment;
import com.librarymanagement.fragments.user.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout userFrameLayout;
    private BottomNavigationView userBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        
        setUpBottomNav();

    }

    private void setUpBottomNav() {

        Menu menu = userBottomNavigation.getMenu();

        // Setting the default fragment as UserHomeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.userFrameLayout, new UserHomeFragment()).commit();

        // Handle event when select a menu item
        userBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;

                switch (item.getItemId()) {

                    //Shows the Appropriate Fragment by using id as address
                    case R.id.UserHome:
                        selectedFragment = new UserHomeFragment();
                        break;

                    case R.id.UserDashboard:
                        selectedFragment = new UserDashBoardFragment();
                        break;

                    case R.id.UserNotification:
                        selectedFragment = new UserNotificationFragment();
                        break;

                    case R.id.UserProfile:
                        selectedFragment = new UserProfileFragment();
                        break;

                }

                // Sets the selected Fragment into the FrameLayout
                getSupportFragmentManager().beginTransaction().replace(R.id.userFrameLayout, selectedFragment).commit();
                return true;

            }
        });
    }

    private void initView() {

        // find views
        userFrameLayout = findViewById(R.id.userFrameLayout);
        userBottomNavigation = findViewById(R.id.userBottomNavigation);

    }

    // handle get user when start app
    @Override
    protected void onStart() {
        super.onStart();

        // checking user already logged or not
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser == null) {

            Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            startActivity(intent);
            finish();

        } else {

            //Checks for user Role and starts the appropriate activity
            String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AllUsers").child(id).child("role");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue() != null) {

                        int data = Integer.parseInt(snapshot.getValue().toString());

                        if (data == Constant.ADMIN) {

                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            //do nothing
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
}