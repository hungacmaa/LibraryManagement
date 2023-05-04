package com.librarymanagement.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.UserNotificationAdapter;
import com.librarymanagement.model.Model;

public class UserNotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserNotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_notification, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String userId = GoogleSignIn.getLastSignedInAccount(getContext()).getId();

        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(userId), Model.class)
                        .build();

        adapter = new UserNotificationAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {

        super.onStop();
        adapter.stopListening();

    }
}
