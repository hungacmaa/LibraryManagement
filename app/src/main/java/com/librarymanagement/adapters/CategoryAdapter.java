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
import com.librarymanagement.activities.AdminEditBookDetailActivity;
import com.librarymanagement.model.Model;
import com.squareup.picasso.Picasso;

public class CategoryAdapter extends FirebaseRecyclerAdapter<Model, CategoryAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {

        String imageUrl = model.getCategoryImage();
        String category = model.getCategory();
//        Picasso.get().load(imageUrl).into(holder.categoryImage);

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

        holder.categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdminEditBookDetailActivity.class);
                intent.putExtra("category", category);
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_single_category_file, parent, false);
        return new CategoryAdapter.Viewholder(view);

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
