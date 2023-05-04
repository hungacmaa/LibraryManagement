package com.librarymanagement.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.adapters.BooksAdapter;
import com.librarymanagement.model.Model;

public class BooksActivity extends AppCompatActivity {

    private RecyclerView booksRecyclerView;
    private BooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        String category = getIntent().getStringExtra("category");

        // Assigning the Recyclerview
        booksRecyclerView = findViewById(R.id.booksRecyclerView);
        booksRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // Firebase Recycler Options to get the data form firebase database using model class and reference
        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance()
                                .getReference()
                                .child("AllBooks")
                                .child(category), Model.class)
                        .build();

        //Setting adapter to RecyclerView
        adapter = new BooksAdapter(options);
        booksRecyclerView.setAdapter(adapter);
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