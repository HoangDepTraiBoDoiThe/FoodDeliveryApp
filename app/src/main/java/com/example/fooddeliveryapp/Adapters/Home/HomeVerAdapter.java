package com.example.fooddeliveryapp.Adapters.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Cart.CartModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    private BottomSheetDialog bottomSheetDialog;
    Context context;
    ArrayList<HomeVerModel> homeVerModels;
    int newQuantity;
    int curQuantity = 1;

    HomeIntrface homeIntrface;

    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> homeVerModels) {
        this.context = context;
        this.homeVerModels = homeVerModels;
    }
    public HomeVerAdapter(Context context, ArrayList<HomeVerModel> homeVerModels, HomeIntrface homeIntrface) {
        this.context = context;
        this.homeVerModels = homeVerModels;
        this.homeIntrface = homeIntrface;
    }
    @NonNull
    @NotNull
    @Override
    public HomeVerAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeVerAdapter.ViewHolder holder, int position) {

        final String mName =  homeVerModels.get(position).getName();
        final String mTime =  homeVerModels.get(position).getTiming();
        final String mRate =  homeVerModels.get(position).getRating();
        final double mPrice =  homeVerModels.get(position).getPrice();
        final int mImage =  homeVerModels.get(position).getImage();
        //final int mIsFavorite =  list.get(position).getImage();

        holder.imageView.setImageResource(homeVerModels.get(position).getImage());
        holder.name.setText(homeVerModels.get(position).getName());
        holder.price.setText(String.format(Locale.getDefault(), "%.2f", homeVerModels.get(position).getPrice()));
        holder.rating.setText(homeVerModels.get(position).getRating());
        holder.timing.setText(homeVerModels.get(position).getTiming());

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
                Button bottomAdd2Cart = sheetView.findViewById(R.id.add2cart);
                Button incrementButton = sheetView.findViewById(R.id.incrementButton);
                Button decrementButton = sheetView.findViewById(R.id.decrementButton);
                TextView totalPrice = sheetView.findViewById(R.id.total_price);
                TextView quantity = sheetView.findViewById(R.id.quantityEditText);

                String foodPrice = String.format(Locale.getDefault(), "%.2f", homeVerModels.get(position).getPrice());
                totalPrice.setText(foodPrice);

                bottomName.setText(mName);
                bottomPrice.setText(foodPrice);
                bottomRating.setText(mRate);
                bottomTime.setText(mTime);
                bottomImg.setImageResource(mImage);

                incrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curQuantity++;
                        quantity.setText(String.valueOf(curQuantity));
                        totalPrice.setText(CalculateTotalPrice(curQuantity, homeVerModels.get(position).getPrice()) + "");
                    }
                });

                decrementButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (curQuantity >= 1) {
                            curQuantity--;
                            quantity.setText(String.valueOf(curQuantity));
                            totalPrice.setText(CalculateTotalPrice(curQuantity, homeVerModels.get(position).getPrice()) + "");
                        }
                    }
                });

                bottomAdd2Cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addToCart(homeVerModels.get(position).getId());
                        bottomSheetDialog.dismiss();
                    }
                });

                // Fetch the user's favorite list from the server
                fetchUserFavoritesFromServer(homeVerModels.get(position).getId(), new OnFetchFavoritesListener() {
                    @Override
                    public void onFavoritesFetched(List<String> favoriteFoodIDs) {
                        boolean isFavsorite = favoriteFoodIDs.contains(homeVerModels.get(position).getId());
                        homeVerModels.get(position).setFavorite(isFavsorite);

                        // Update the favorite icon based on the isFavorite status of the food
                        if (homeVerModels.get(position).isFavorite()) {

                            isFavoriteFood.setImageResource(R.drawable.favorite_filled);
                        } else {
                            isFavoriteFood.setImageResource(R.drawable.favorite_border);
                        }
                    }
                });

                isFavoriteFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isFavorite = homeVerModels.get(position).isFavorite();
                        homeVerModels.get(position).setFavorite(!isFavorite);

                        if (homeVerModels.get(position).isFavorite()) {
                            isFavoriteFood.setImageResource(R.drawable.favorite_filled);
                            addToFavorite(homeVerModels.get(position).getId());
                        }
                        else {
                            isFavoriteFood.setImageResource(R.drawable.favorite_border);
                            removeFromFavorite(homeVerModels.get(position).getId());
                        }
                    }
                });


                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    private double CalculateTotalPrice(int quantity, double price) {
        return quantity * price;
    }

    private void addToCart(String foodID) {
        DatabaseReference cartItemsRef = FirebaseDatabase.getInstance().getReference("cartItems");

        // Check if the food item already exists in the user's cart
        String cartID = hardcodedUserID;
        cartItemsRef.orderByChild("foodID").equalTo(foodID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Food item already exists in the cart, update its quantity
                    for (DataSnapshot cartItemSnapshot : dataSnapshot.getChildren()) {
                        String existingCartItemID = cartItemSnapshot.getKey();
                        int existingQuantity = cartItemSnapshot.child("quantity").getValue(Integer.class);
                        int newQuantity = existingQuantity + curQuantity;

                        cartItemsRef.child(existingCartItemID).child("quantity").setValue(newQuantity)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showToast("Added to cart");
                                        } else {
                                            showToast("Failed to add to cart");
                                        }
                                    }
                                });
                    }
                } else {
                    // Food item does not exist in the cart, create a new cart item
                    CartModel cartItem = new CartModel(cartID, foodID, curQuantity); // 1 represents initial quantity
                    String newCartItemID = cartItemsRef.push().getKey();

                    cartItemsRef.child(newCartItemID).setValue(cartItem)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        showToast("Added to cart");
                                    } else {
                                        showToast("Failed to add to cart");
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
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
        return homeVerModels.size();
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
