package com.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.EditBookAdapter;
import com.librarymanagement.model.Model;

public class AdminEditBookDetailActivity extends AppCompatActivity {

    private RecyclerView AdminEditBookRecyclerView;
    private EditBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_book_detail);

        AdminEditBookRecyclerView = findViewById(R.id.AdminEditBookRecyclerView);
        AdminEditBookRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        String category=getIntent().getStringExtra("category");

        // Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference().child("AllBooks")
                                .child(category), Model.class)
                        .build();

        adapter = new EditBookAdapter(options);
        AdminEditBookRecyclerView.setAdapter(adapter);

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