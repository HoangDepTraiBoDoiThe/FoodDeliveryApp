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
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DailyMealModel;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {

    List<DetailedDailyModel> detailedDailyModels;

    Context context;

    public DetailedDailyAdapter(List<DetailedDailyModel> detailedDailyModels, Context context) {
        this.detailedDailyModels = detailedDailyModels;
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
        DetailedDailyModel detailedDailyModel = detailedDailyModels.get(position);

        holder.imageView.setImageResource(detailedDailyModel.getImage());
        holder.name.setText(detailedDailyModel.getName());
        holder.price.setText(detailedDailyModel.getPrice());
        holder.description.setText(detailedDailyModel.getDescription());
        holder.rating.setText(detailedDailyModel.getRating());
        holder.timing.setText(detailedDailyModel.getTiming());

        fetchUserFavoritesFromServer(detailedDailyModels.get(position).getId(), new DetailedDailyAdapter.OnFetchFavoritesListener() {
            @Override
            public void onFavoritesFetched(List<String> favoriteFoodIDs) {
                boolean isFavsorite = favoriteFoodIDs.contains(detailedDailyModels.get(position).getId());
                detailedDailyModels.get(position).setFavorite(isFavsorite);

                // Update the favorite icon based on the isFavorite status of the food
                if (detailedDailyModel.isFavorite()) {
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
                boolean isFavorite = detailedDailyModel.isFavorite();
                detailedDailyModel.setFavorite(!isFavorite);

                // Update the favorite icon based on the updated isFavorite status
                if (detailedDailyModel.isFavorite()) {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_filled);
                    // Add the foodID to the user's favorite list in the database
                    addToFavorite(detailedDailyModel.getId());
                } else {
                    holder.add2Favorite.setImageResource(R.drawable.favorite_border);
                    // Remove the foodID from the user's favorite list in the database
                    removeFromFavorite(detailedDailyModel.getId());
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
        return detailedDailyModels.size();
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
            rating = itemView.findViewById(R.id.detailed_rating);
            timing = itemView.findViewById(R.id.detailed_timing);
        }
    }
}
