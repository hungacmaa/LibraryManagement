package com.librarymanagement.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.librarymanagement.R;
import com.librarymanagement.activities.BooksActivity;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

public class HomeAdapter extends FirebaseRecyclerAdapter<Model, HomeAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {
        String imageUrl = model.getCategoryImage();
        String category = model.getCategory();

        if (category.equals("Romantic")) {
            holder.categoryImage.setImageResource(R.drawable.romantic);
        }

        if (category.equals("Sport")) {
            holder.categoryImage.setImageResource(R.drawable.sport);
        }

        if (category.equals("Science")) {
            holder.categoryImage.setImageResource(R.drawable.science);
        }

        holder.categoryName.setText(category);
//        Picasso.get().load(imageUrl).into(holder.categoryImage);

        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), BooksActivity.class);
                intent.putExtra("category", category);
                view.getContext().startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // the data objects are inflated into the user_single_category_file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_category_file, parent, false);
        return new HomeAdapter.Viewholder(view);

    }

    class Viewholder extends RecyclerView.ViewHolder {

        private ImageView categoryImage;
        private TextView categoryName;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            categoryImage = itemView.findViewById(R.id.categoryImage);
            categoryName = itemView.findViewById(R.id.categoryName);

        }
    }
}
