package com.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.constants.Constant;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final int SIGNIN_CODE = 54;

    private Button userLogin, adminLogin;

    private FirebaseAuth auth;

    private FirebaseDatabase database;

    private GoogleSignInClient googleSignInClient;

    private ProgressDialog progressDialog;

    private int myRole = Constant.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // find views
        userLogin = findViewById(R.id.userLogin);
        adminLogin = findViewById(R.id.adminLogin);

        // set click listener for buttons
        userLogin.setOnClickListener(this);
        adminLogin.setOnClickListener(this);

        // build progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng nhập bằng Google");
        progressDialog.setMessage("Đang đăng nhập...");

        // get instance for auth service and database service
        auth =  FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // for google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onClick(View view) {
        if (userLogin.equals(view)) {
            myRole = Constant.USER;
        }
        if (adminLogin.equals(view)) {
            myRole = Constant.ADMIN;
        }

        login();
    }

    private void login() {
        //login
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, SIGNIN_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGNIN_CODE){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            // show waiting dialog
            progressDialog.show();

            try{

                // getting account
                GoogleSignInAccount myAccount = task.getResult(ApiException.class);

                // user firebase auth service for above account
                firebaseAuth(myAccount);

            }
            catch (Exception e ){
                System.out.println(e);
            }
        }
    }

    private void firebaseAuth(GoogleSignInAccount myAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(myAccount.getIdToken(), null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    // when signin success
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            //FirebaseUser user = auth.getCurrentUser();
                            //System.out.println(user.getDisplayName());

                            // comment this
                            //Hashmap to store the userdetails and setting it to fireabse
                            HashMap<String, Object> user_details = new HashMap<>();

                            //Accessing the user details from gmail
                            String id = myAccount.getId().toString();
                            String name = myAccount.getDisplayName().toString();
                            String mail = myAccount.getEmail().toString();
                            String pic = myAccount.getPhotoUrl().toString();

                            //storing data in hashmap
                            user_details.put("id", id);
                            user_details.put("name", name);
                            user_details.put("mail", mail);
                            user_details.put("profilepic", pic);
                            user_details.put("role", myRole);

                            //Adding data to firebase
                            database.getReference().child("AllUsers").child(id)
                                    .updateChildren(user_details).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                // stop waiting progress dialog
                                                progressDialog.cancel();

                                                if (myRole == Constant.ADMIN) {

                                                    // chuyển lại main activitiy sau khi chọn đăng nhập xong
                                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                                    // Clears older activities and tasks
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                } else {

                                                    // chuyển lại main activitiy sau khi chọn đăng nhập xong
                                                    Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                                                    // Clears older activities and tasks
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);

                                                }
                                            }
                                        }
                                    });
                            // end comment
                        }
                    }
                });
    }
}