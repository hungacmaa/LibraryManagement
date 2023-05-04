package com.librarymanagement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.librarymanagement.R;
import com.librarymanagement.model.Model;

public class UserNotificationAdapter extends FirebaseRecyclerAdapter<Model, UserNotificationAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserNotificationAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Model model) {

        holder.notification.setText(model.getNotification());

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_notification, parent, false);
        return new UserNotificationAdapter.Viewholder(view);

    }

    class Viewholder extends RecyclerView.ViewHolder {

        private TextView notification;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            notification = itemView.findViewById(R.id.notification);
        }
    }
}
