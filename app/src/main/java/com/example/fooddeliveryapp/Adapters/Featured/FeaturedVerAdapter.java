package com.example.fooddeliveryapp.Adapters.Featured;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Featured.FeaturedVerModel;
import com.example.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FeaturedVerAdapter extends RecyclerView.Adapter<FeaturedVerAdapter.ViewHolder> {

    List<FeaturedVerModel> list;
    Context context;

    public FeaturedVerAdapter(List<FeaturedVerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FeaturedVerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_ver_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FeaturedVerAdapter.ViewHolder holder, int position) {
        FeaturedVerModel featuredVerModel = list.get(position);

        holder.imageView.setImageResource(featuredVerModel.getImage());
        holder.name.setText(featuredVerModel.getName());
        holder.rating.setText(featuredVerModel.getRating());
        holder.timing.setText(featuredVerModel.getTiming());
        holder.isFavorite.setImageResource(R.drawable.favorite_filled);

        holder.isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the foodID from the user's favorite list in the database
                String foodID = list.get(position).getId();
                removeFromFavorite(foodID);

                // Remove the food from the list and update the UI
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, list.size());
            }
        });
    }

    String hardcodedUserID = "0";
    private void removeFromFavorite(String foodID) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(hardcodedUserID).child("favorites");

        favoritesRef.child(foodID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Successfully removed the foodID from the user's favorite list
                    showToast("Removed from favorites");
                } else {
                    // Handle error if the removal was not successful
                    showToast("Failed to remove from favorites");
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, isFavorite;
        TextView name, desc, rating, timing;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detailed_img);
            isFavorite = itemView.findViewById(R.id.isFavorite_img);
            name = itemView.findViewById(R.id.detailed_name);
            desc = itemView.findViewById(R.id.detailed_des);
            rating = itemView.findViewById(R.id.detailed_rating);
            timing = itemView.findViewById(R.id.detailed_timing);
        }

    }
}
