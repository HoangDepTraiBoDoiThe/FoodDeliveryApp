package com.example.fooddeliveryapp.Adapters.DailyMeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {

    List<HomeVerModel> homeVerModels;

    Context context;

    public DetailedDailyAdapter(List<HomeVerModel> homeVerModels, Context context) {
        this.homeVerModels = homeVerModels;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public DetailedDailyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DetailedDailyAdapter.ViewHolder holder, int position) {
        HomeVerModel homeVerModel = homeVerModels.get(position);

        holder.imageView.setImageResource(homeVerModel.getImage());
        holder.name.setText(homeVerModel.getName());
        holder.price.setText(homeVerModel.getPrice());
        holder.description.setText(homeVerModel.getDescription());
        holder.rating.setText(homeVerModel.getRating());
        holder.timing.setText(homeVerModel.getTiming());

        fetchUserFavoritesFromServer(homeVerModels.get(position).getId(), new DetailedDailyAdapter.OnFetchFavoritesListener() {
            @Override
            public void onFavoritesFetched(List<String> favoriteFoodIDs) {
                boolean isFavsorite = favoriteFoodIDs.contains(homeVerModels.get(position).getId());
                homeVerModels.get(position).setFavorite(isFavsorite);

                // Update the favorite icon based on the isFavorite status of the food
                if (homeVerModel.isFavorite()) {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_filled);
                } else {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_border);
                }
            }
        });

        holder.add2Favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the isFavorite status of the food
                boolean isFavorite = homeVerModel.isFavorite();
                homeVerModel.setFavorite(!isFavorite);

                // Update the favorite icon based on the updated isFavorite status
                if (homeVerModel.isFavorite()) {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_filled);
                    // Add the foodID to the user's favorite list in the database
                    addToFavorite(homeVerModel.getId());
                } else {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_border);
                    // Remove the foodID from the user's favorite list in the database
                    removeFromFavorite(homeVerModel.getId());
                }
            }
        });
    }
    private void fetchUserFavoritesFromServer(String foodID, DetailedDailyAdapter.OnFetchFavoritesListener listener) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(hardcodedUserID).child("favorites");

        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> favoriteFoodIDs = new ArrayList<>();
                for (DataSnapshot foodIDSnapshot : dataSnapshot.getChildren()) {
                    String favoriteFoodID = foodIDSnapshot.getValue(String.class);
                    favoriteFoodIDs.add(favoriteFoodID);
                }
                listener.onFavoritesFetched(favoriteFoodIDs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });
    }
    interface OnFetchFavoritesListener {
        void onFavoritesFetched(List<String> favoriteFoodIDs);
    }
    String hardcodedUserID = "0";
    private void addToFavorite(String foodID) {
        // Create a reference to the user's favorite list in the database
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(hardcodedUserID).child("favorites");

        // Add the foodID to the user's favorite list
        favoritesRef.child(foodID).setValue(foodID.toString(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    // Successfully added the foodID to the user's favorite list
                    showToast("Added to favorites");
                } else {
                    // Handle error if adding to favorites was not successful
                    showToast("Failed to add to favorites");
                }
            }
        });
    }
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    private void removeFromFavorite(String foodID) {
        // Create a reference to the user's favorite list in the database
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("users")
                .child(hardcodedUserID).child("favorites");

        // Remove the foodID from the user's favorite list
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

    @Override
    public int getItemCount() {
        return homeVerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, add2Favorite;
        TextView name, price, description, rating, timing;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detailed_img);
            add2Favorite = itemView.findViewById(R.id.add2favourite); // Assuming you have an ImageView for the favorite icon
            name = itemView.findViewById(R.id.detailed_name);
            price = itemView.findViewById(R.id.detailed_price);
            description = itemView.findViewById(R.id.detailed_des);
            rating = itemView.findViewById(R.id.order_time);
            timing = itemView.findViewById(R.id.detailed_timing);
        }
    }
}
