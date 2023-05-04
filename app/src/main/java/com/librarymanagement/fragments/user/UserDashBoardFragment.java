package com.librarymanagement.fragments.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.UserDashBoardAdapter;
import com.librarymanagement.model.Model;

public class UserDashBoardFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserDashBoardAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_dash_board, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Getting User id
        String userId = GoogleSignIn.getLastSignedInAccount(getContext()).getId();

        //Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId), Model.class)
                        .build();


        //Setting adapter to RecyclerView
        adapter = new UserDashBoardAdapter(options);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Starts listening for data from firebase when this fragment starts
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Stops listening for data from firebase
        adapter.stopListening();
    }
}
