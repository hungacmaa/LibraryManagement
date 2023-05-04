package com.librarymanagement.fragments.admin;

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
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.CategoryAdapter;
import com.librarymanagement.model.Model;

public class AdminEditBookDetailFragment extends Fragment {

    private RecyclerView adminEditBookRecyclerView;
    private CategoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_edit_book, container, false);

        adminEditBookRecyclerView = (RecyclerView) view.findViewById(R.id.adminEditBookRecyclerView);
        adminEditBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories"), Model.class)
                        .build();

        adapter = new CategoryAdapter(options);
        adminEditBookRecyclerView.setAdapter(adapter);

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
