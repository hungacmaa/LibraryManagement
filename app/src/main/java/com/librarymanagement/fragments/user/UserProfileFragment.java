package com.librarymanagement.fragments.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.librarymanagement.R;
import com.librarymanagement.activities.SplashScreenActivity;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private CircleImageView avatar;
    private TextView name;
    private EditText inputPhone, inputAddress, inputSchool, inputStudentId;
    private Button btnSubmit, btnSignOut;
    private DatabaseReference databaseReference;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // init views
        initView(view);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllUsers");

        // get data from firebase database
        getDataFromFirebase();

        return view;
    }

    private void getDataFromFirebase() {

        // Get user data from GoogleSignIn
        GoogleSignInAccount currentAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

        if (currentAccount != null) {
            userId = currentAccount.getId().toString();

            databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    // Get data form the firebase
                    Model model = snapshot.getValue(Model.class);

                    // setting the data to android materials
                    Picasso.get().load(model.getProfilepic()).into(avatar);
                    name.setText(model.getName());
                    inputPhone.setText(model.getPhone());
                    inputAddress.setText(model.getAddress());
                    inputSchool.setText(model.getSchool());
                    inputStudentId.setText(model.getStudentId());

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }

    private void initView(View view) {
        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        inputPhone = view.findViewById(R.id.inputPhone);
        inputAddress = view.findViewById(R.id.inputAddress);
        inputSchool = view.findViewById(R.id.inputSchool);
        inputStudentId = view.findViewById(R.id.inputStudentId);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSignOut = view.findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                // GoogleSignInClient to access the current user
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);

                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            //User Signout
                            FirebaseAuth.getInstance().signOut();

                            //Redirecting to starting Activity
                            Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                });
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the current data from the edit Text to update it to firebase
                String phone = inputPhone.getText().toString();
                String address = inputAddress.getText().toString();
                String school = inputSchool.getText().toString();
                String studentId = inputStudentId.getText().toString();

                //Checking for empty fields
                if (phone.isEmpty() || address.isEmpty() || school.isEmpty() || studentId.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                } else {
                    //calling method to update data to firebase
                    updateUserInfo(phone, address, school, studentId, userId);
                }
            }
        });
    }

    private void updateUserInfo(String phone, String address, String school, String studentId, String userId) {

        //Storing the user details in hashmap
        HashMap userDetails = new HashMap();

        //adding the data to hashmap
        userDetails.put("phone", phone);
        userDetails.put("address", address);
        userDetails.put("school", school);
        userDetails.put("studentId", studentId);

        //adding the data to firebase
        databaseReference.child(userId).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {

                if (task.isSuccessful()) {
                    //Showing the Toast message to user
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    //Showing the toast message to user
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
