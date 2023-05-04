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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.librarymanagement.R;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class UserDashBoardAdapter extends FirebaseRecyclerAdapter<Model, UserDashBoardAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserDashBoardAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        //Setting data to android materials
        holder.name.setText("Tên sách: " + model.getBookName());
        holder.amount.setText("Số lượng: " + model.getBooksCount());
        holder.location.setText("Vị trí: " + model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.bookImage);

        //Implementing the OnClick Listener to delete the data from the database
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting user id from the gmail sing in
                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();

                // Path to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId);
                reference.orderByChild("bookName").equalTo(model.getBookName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            //getting the parent node of the data
                            String key = ds.getKey();

                            //removing the data from the database
                            reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("OrderedBooks")
                                                .child(key).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //Showing the Toast message to the user
                                                            Toast.makeText(view.getContext(), "Book Order Canceled Successfully", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });

                        }
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
        return new UserDashBoardAdapter.Viewholder(view);

    }

    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private TextView name, amount, location;
        private Button btnSubmit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            //Assigning Address of the android materials
            bookImage = itemView.findViewById(R.id.bookImage);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            location = itemView.findViewById(R.id.location);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);

            btnSubmit.setText("Hủy mượn");

        }
    }
}
