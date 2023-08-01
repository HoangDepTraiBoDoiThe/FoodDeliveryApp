package com.example.fooddeliveryapp.Adapters.Home;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    private BottomSheetDialog bottomSheetDialog;
    Context context;
    ArrayList<HomeVerModel> list;

    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @NotNull
    @Override
    public HomeVerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeVerAdapter.ViewHolder holder, int position) {

        final String mName =  list.get(position).getName();
        final String mTime =  list.get(position).getTiming();
        final String mRate =  list.get(position).getRating();
        final String mPrice =  list.get(position).getPrice();
        final int mImage =  list.get(position).getImage();
        //final int mIsFavorite =  list.get(position).getImage();

        holder.imageView.setImageResource(list.get(position).getImage());
        holder.name.setText(list.get(position).getName());
        holder.price.setText(list.get(position).getPrice());
        holder.rating.setText(list.get(position).getRating());
        holder.timing.setText(list.get(position).getTiming());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);
                sheetView.findViewById(R.id.add2cart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });

                ImageView bottomImg = sheetView.findViewById(R.id.bottom_sheet_img);
                ImageView isFavoriteFood = sheetView.findViewById(R.id.add2favourite);
                TextView bottomName = sheetView.findViewById(R.id.bottom_sheet_name);
                TextView bottomRating = sheetView.findViewById(R.id.bottom_sheet_rating);
                TextView bottomPrice = sheetView.findViewById(R.id.bottom_sheet_price);
                TextView bottomTime = sheetView.findViewById(R.id.bottom_sheet_timing);

                bottomName.setText(mName);
                bottomPrice.setText(mPrice);
                bottomRating.setText(mRate);
                bottomTime.setText(mTime);
                bottomImg.setImageResource(mImage);


                // Fetch the user's favorite list from the server
                fetchUserFavoritesFromServer(list.get(position).getId(), new OnFetchFavoritesListener() {
                    @Override
                    public void onFavoritesFetched(List<String> favoriteFoodIDs) {
                        boolean isFavsorite = favoriteFoodIDs.contains(list.get(position).getId());
                        list.get(position).setFavorite(isFavsorite);

                        // Update the favorite icon based on the isFavorite status of the food
                        if (list.get(position).isFavorite()) {

                            isFavoriteFood.setImageResource(R.drawable.favorite_filled);
                        } else {
                            isFavoriteFood.setImageResource(R.drawable.favorite_border);
                        }
                    }
                });
                isFavoriteFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Toggle the isFavorite status of the food
                        boolean isFavorite = list.get(position).isFavorite();
                        list.get(position).setFavorite(!isFavorite);

                        // Update the favorite icon based on the updated isFavorite status
                        if (list.get(position).isFavorite()) {
                            isFavoriteFood.setImageResource(R.drawable.favorite_filled);
                            // Add the foodID to the user's favorite list in the database
                            addToFavorite(list.get(position).getId());
                        }
                        else {
                            isFavoriteFood.setImageResource(R.drawable.favorite_border);
                            // Remove the foodID from the user's favorite list in the database
                            removeFromFavorite(list.get(position).getId());
                        }
                    }
                });


                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
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

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void fetchUserFavoritesFromServer(String foodID, OnFetchFavoritesListener listener) {
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

    public interface OnFetchFavoritesListener {
        void onFavoritesFetched(List<String> favoriteFoodIDs);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, timing, rating, price;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ver_img);
            //add2Favorite = itemView.findViewById(R.id.add2favourite);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timing);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }
}
