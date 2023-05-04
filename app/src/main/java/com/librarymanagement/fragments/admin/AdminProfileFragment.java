package com.librarymanagement.fragments.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.librarymanagement.R;
import com.librarymanagement.activities.SplashScreenActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileFragment extends Fragment {

    private CircleImageView avatar;
    private TextView name;
    private Button btnSignOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        initView(view);

        getDataFromFirebase();

        return view;
    }

    private void getDataFromFirebase() {

        // get user info from google sign in
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            name.setText(acct.getDisplayName());
            Picasso.get().load(acct.getPhotoUrl()).into(avatar);
        }

    }

    private void initView(View view) {

        avatar = view.findViewById(R.id.avatar);
        name = view.findViewById(R.id.name);
        btnSignOut = view.findViewById(R.id.btnSighOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .build();

                // get current user info
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            // sign out
                            FirebaseAuth.getInstance().signOut();

                            // redirecting to starting Activity
                            Intent intent = new Intent(getContext(), SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }

                    }
                });

            }
        });
    }
}
