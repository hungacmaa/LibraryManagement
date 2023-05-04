package com.librarymanagement.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.MainActivity;
import com.librarymanagement.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class UserDetailsActivity extends AppCompatActivity {

    private EditText inputPhone, inputAddress, inputSchool, inputStudentId;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // init views
        initView();


    }

    private void initView() {
        inputPhone = findViewById(R.id.inputPhone);
        inputAddress = findViewById(R.id.inputAddress);
        inputSchool = findViewById(R.id.inputSchool);
        inputStudentId = findViewById(R.id.inputStudentId);
        btnSubmit = findViewById(R.id.btnSubmit);

        // set onclick for button submit
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get data from edit texts
                String phone = inputPhone.getText().toString();
                String address = inputAddress.getText().toString();
                String school = inputSchool.getText().toString();
                String studentId = inputStudentId.getText().toString();

                // validate and save data to firebase database
                if (phone.isEmpty() || address.isEmpty() || school.isEmpty() || studentId.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();

                } else {

                    addUserDetails(phone, address, school, studentId);

                }
            }
        });
    }

    private void addUserDetails(String phone, String address, String school, String studentId) {

        //Getting the user id form the google signin
        String id = GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getId();

        //Creating Hashmap to store data
        HashMap userDetails = new HashMap();
        userDetails.put("phone", phone);
        userDetails.put("address", address);
        userDetails.put("school", school);
        userDetails.put("studentId", studentId);

        //Adding the user detail to firebase
        FirebaseDatabase.getInstance().getReference().child("AllUsers").child(id)
                .updateChildren(userDetails)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task task) {
                        if (task.isSuccessful()) {

                            // showing the toast message to user
                            Toast.makeText(getApplicationContext(), "Thêm thông tin thành công", Toast.LENGTH_SHORT).show();

                            // Changing current intent after adding the details to firebase
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();


                        }
                    }
                });

    }
}