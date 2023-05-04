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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class AdminDashBoardAdapter extends FirebaseRecyclerAdapter<Model, AdminDashBoardAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminDashBoardAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {

        //Setting data to android materials
        holder.bookName.setText("Tên sách: " + model.getBookName());
        holder.bookAmount.setText("Số lương: " + model.getBooksCount());
        holder.bookLocation.setText("Vi trí: " + model.getBookLocation());

        holder.userName.setText("Tên người dùng: " + model.getName());
        holder.userPhone.setText("Số điện thoại: " + model.getPhone());
        holder.userAddress.setText("Địa chỉ: " + model.getAddress());

        Picasso.get().load(model.getImageUrl()).into(holder.bookImage);

        // handle cancel submit
        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = model.getId();

                // Path to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("OrderedBooks");
                reference.orderByChild("bookName").equalTo(model.getBookName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            // getting the parent node of the data
                            String key = ds.getKey();

                            // removing the data from the database
                            reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("myOrderedBooks").child(userId)
                                                .child(key).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            // Generating Unique Key
                                                            String pushKey = FirebaseDatabase.getInstance().getReference().child("UserNotifications").push().getKey();

                                                            // Adding Notification To Database
                                                            FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(userId).child(pushKey)
                                                                    .child("notification").setValue("Yêu cầu mượn cuốn " + model.getBookName().toUpperCase() + " đã bị từ chối bởi admin")
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                            //Showing the Toast message to the user
                                                                            Toast.makeText(view.getContext(), "Từ chối thành công", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });


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

        // handle accept submit
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Generating Unique key
                String pushKey = FirebaseDatabase.getInstance().getReference().child("UserNotifications").push().getKey();

                //Adding Notification to database
                FirebaseDatabase.getInstance().getReference().child("UserNotifications").child(model.getId())
                        .child(pushKey)
                        .child("notification").setValue("Yêu cầu mượn cuốn " + model.getBookName().toUpperCase() + " đã được chấp nhận bởi admin")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(view.getContext(), "Chấp thuận thành công", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_single_dash_board, parent, false);
        return new AdminDashBoardAdapter.Viewholder(view);

    }

    class Viewholder extends RecyclerView.ViewHolder {


        ImageView bookImage;
        TextView bookName, bookAmount, bookLocation, userName, userPhone, userAddress;
        Button btnAccept, btnCancel;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.bookImage);
            bookName = itemView.findViewById(R.id.bookName);
            bookAmount = itemView.findViewById(R.id.bookAmount);
            bookLocation = itemView.findViewById(R.id.bookLocation);
            userName = itemView.findViewById(R.id.userName);
            userPhone = itemView.findViewById(R.id.userPhone);
            userAddress = itemView.findViewById(R.id.userAddress);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnCancel = itemView.findViewById(R.id.btnCancel);

        }
    }
}
