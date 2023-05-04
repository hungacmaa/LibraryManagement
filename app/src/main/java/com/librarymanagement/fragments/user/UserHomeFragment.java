package com.librarymanagement.fragments.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.HomeAdapter;
import com.librarymanagement.model.Model;

public class UserHomeFragment extends Fragment {

    private RecyclerView userHomeRecyclerView;
    private HomeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        userHomeRecyclerView = view.findViewById(R.id.userHomeRecyclerView);
        userHomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options = new FirebaseRecyclerOptions.Builder<Model>()
                .setQuery(FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Categories"), Model.class)
                .build();

        // Setting adapter to RecyclerView
        adapter = new HomeAdapter(options);
        userHomeRecyclerView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        // Starts listening for data from firebase when this fragment starts
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Stops listening for data from firebase
        adapter.stopListening();
    }
}
