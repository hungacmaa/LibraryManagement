package com.librarymanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.librarymanagement.R;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class BooksAdapter extends FirebaseRecyclerAdapter<Model, BooksAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public BooksAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {

        // Setting data to android materials
        holder.name.setText("Tên sách: " + model.getBookName());
        holder.amount.setText("Số lượng còn: " + model.getBooksCount());
        holder.location.setText("Vị trí: " + model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.bookImage);

        // Implementing OnClickListener
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bookLocation = model.getBookLocation();
                String bookName = model.getBookName();
                String booksCount = model.getBooksCount();
                String imageUrl = model.getImageUrl();

                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();

                FirebaseDatabase.getInstance().getReference().child("AllUsers").child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                //Getting user data using Model Class
                                Model model1 = snapshot.getValue(Model.class);
                                String name = model1.getName();
                                String school = model1.getSchool();
                                String phone = model1.getPhone();
                                String address = model1.getAddress();
                                String studentId = model1.getStudentId();

                                HashMap userDetails = new HashMap();
                                userDetails.put("name", name);
                                userDetails.put("school", school);
                                userDetails.put("phone", phone);
                                userDetails.put("address", address);
                                userDetails.put("studentId", studentId);
                                userDetails.put("bookLocation", bookLocation);
                                userDetails.put("bookName", bookName);
                                userDetails.put("booksCount", booksCount);
                                userDetails.put("imageUrl", imageUrl);
                                userDetails.put("id", userId);

                                String push = FirebaseDatabase.getInstance().getReference().child("OrderedBooks").push().getKey();
                                FirebaseDatabase.getInstance().getReference().child("OrderedBooks").child(push)
                                        .updateChildren(userDetails)
                                        .addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {

                                                FirebaseDatabase.getInstance().getReference().child("myOrderedBooks")
                                                        .child(userId).child(push)
                                                        .updateChildren(userDetails)
                                                        .addOnSuccessListener(new OnSuccessListener() {
                                                            @Override
                                                            public void onSuccess(Object o) {

                                                                Toast.makeText(view.getContext(), "Book Ordered Successfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });

                                            }
                                        });

                                //Toast message
                                Toast.makeText(view.getContext(), "Book Ordered", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_book, parent, false);
        return new BooksAdapter.Viewholder(view);
    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private TextView name, amount, location;
        private Button btnSubmit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.bookImage);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            location = itemView.findViewById(R.id.location);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);

        }

    }
}
