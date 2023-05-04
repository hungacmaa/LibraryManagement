package com.librarymanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.librarymanagement.R;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditBookAdapter extends FirebaseRecyclerAdapter<Model, EditBookAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public EditBookAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {

        // Set data to android materials
        holder.inputName.setText(model.getBookName());
        holder.inputAmount.setText(model.getBooksCount());
        holder.inputLocation.setText(model.getBookLocation());

        Picasso.get().load(model.getImageUrl()).into(holder.bookImage);

        String pushKey = model.getPushKey().toString();
        String category = model.getCategory().toString();

        //Implementing the OnClick Listener to delete the data from the database
        holder.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bookName = holder.inputName.getText().toString();
                String booksCount = holder.inputAmount.getText().toString();
                String bookLocation = holder.inputLocation.getText().toString();

                //Hash map to store values
                HashMap bookDetails = new HashMap();

                //adding the data to hashmap
                bookDetails.put("bookName", bookName);
                bookDetails.put("booksCount", booksCount);
                bookDetails.put("bookLocation", bookLocation);
                bookDetails.put("category", category);
                bookDetails.put("pushKey", pushKey);


                FirebaseDatabase.getInstance().getReference().child("AllBooks")
                        .child(category)
                        .child(pushKey)
                        .updateChildren(bookDetails)
                        .addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                                Toast.makeText(view.getContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_single_edit_book, parent, false);
        return new EditBookAdapter.Viewholder(view);
    }

    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView bookImage;
        private EditText inputName, inputAmount, inputLocation;
        private Button btnSubmit;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.bookImage);
            inputName = itemView.findViewById(R.id.inputName);
            inputAmount = itemView.findViewById(R.id.inputAmount);
            inputLocation = itemView.findViewById(R.id.inputLocation);
            btnSubmit = itemView.findViewById(R.id.btnSubmit);

        }
    }
}
