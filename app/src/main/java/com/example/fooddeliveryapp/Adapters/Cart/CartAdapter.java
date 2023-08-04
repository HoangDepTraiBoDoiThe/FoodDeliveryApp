package com.example.fooddeliveryapp.Adapters.Cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Cart.CartModel;
import com.example.fooddeliveryapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    ArrayList<CartModel> cartModels;

    Context context;

    int currentValue = 0;
    double cartTotalPrice = 0;

    private CartAdapterCallback callback;

    public CartAdapter(ArrayList<CartModel> list, double cartTotalPrice, CartAdapterCallback callback, Context context) {
        this.cartModels = list;
        this.cartTotalPrice = cartTotalPrice;
        this.callback = callback;
        this.context = context;
    }

    public interface CartAdapterCallback {
        void onQuantityChanged(double newTotalPrice);
    }

    @NonNull
    @NotNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CartAdapter.ViewHolder holder, int position) {

        CartModel cartModel = cartModels.get(position);
        currentValue = cartModels.get(position).getQuantity();
        holder.imageView.setImageResource(cartModels.get(position).getImage());
        holder.name.setText(cartModels.get(position).getName());
        holder.price.setText(Double.toString(cartModels.get(position).getPrice()));
        holder.itemTotalPrice.setText(String.format(Locale.getDefault(), "%.2f", cartModels.get(position).getItemTotalPrice()));
        holder.price.setText(String.format(Locale.getDefault(), "%.2f", cartModels.get(position).getPrice()));
        holder.rating.setText(cartModels.get(position).getRating());
        holder.quantityEditText.setText(String.valueOf(cartModels.get(position).getQuantity()));

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartModel.getQuantity() + 1;
                updateCartItemQuantity(cartModel.getCartItemID(), newQuantity);
            }
        });

        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = cartModel.getQuantity() - 1;
                if (newQuantity >= 1) {
                    updateCartItemQuantity(cartModel.getCartItemID(), newQuantity);
                }
            }
        });

        holder.quantityEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String inputValue = holder.quantityEditText.getText().toString();
                    try {
                        int newQuantity = Integer.parseInt(inputValue);
                        if (newQuantity >= 1) {
                            updateCartItemQuantity(cartModel.getCartItemID(), newQuantity);
                        } else {
                            holder.quantityEditText.setError("Enter a positive value");
                        }
                    } catch (NumberFormatException e) {
                        holder.quantityEditText.setError("Invalid input");
                    }
                }
                return false;
            }
        });

        holder.deleteCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog(position, holder);
            }
        });

    }

    private void showDeleteConfirmationDialog(int position, ViewHolder holder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Muốn xóa á ?");
        builder.setMessage("Tại sao lại xóa ??? TẠI SAO LẠI XÓA ????!!!! PHẢI MUA ĐI CHỨ !!!");
        builder.setPositiveButton("Cứ xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCartItem(position, holder);
            }
        });
        builder.setNegativeButton("OK", null);
        builder.create().show();
    }
    private void deleteCartItem(int pos, ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            CartModel cartItem = cartModels.get(position);
            String cartItemID = cartItem.getCartItemID();

            DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference("cartItems").child(cartItemID);
            cartItemRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Item removed successfully, update the cart total price
                        updateCartTotalPrice();
                        notifyItemRemoved(pos);
                    } else {
                        // Handle error
                    }
                }
            });
        }
    }

    private void updateCartItemQuantity(String cartItemID, int newQuantity) {
        DatabaseReference cartItemRef = FirebaseDatabase.getInstance().getReference("cartItems").child(cartItemID);
        cartItemRef.child("quantity").setValue(newQuantity);

        cartItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CartModel cartModel = dataSnapshot.getValue(CartModel.class);
                if (cartModel != null) {
                    double newTotalPrice = cartModel.getPrice() * newQuantity;
                    cartItemRef.child("totalPrice").setValue(newTotalPrice);

                    // Call a method to update the cart's total price
                    updateCartTotalPrice();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    private void updateCartTotalPrice() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cartItems");
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartTotalPrice = 0;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                    if (cartModel != null) {
                        String CartFoodID = cartSnapshot.child("foodID").getValue(String.class);

                        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("foods");

                        foodRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot foodSnapshot) {
                                for (DataSnapshot foodsSnapshot : foodSnapshot.getChildren()) {
                                    String foodID = foodsSnapshot.getKey();
                                    if (foodID.equals(CartFoodID)) {
                                        double foodPrice = foodsSnapshot.child("foodPrice").getValue(double.class);
                                        cartTotalPrice += cartModel.getQuantity() * foodPrice;
                                        String x = "";
                                    }
                                }
                                callback.onQuantityChanged(cartTotalPrice);

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }


    private void deleteCartItemFromDatabase(String cartID, int position) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cartItems").child(cartID);
        cartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {
                    // Handle error
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, deleteCartItem;
        TextView name, price, rating, itemTotalPrice;
        EditText quantityEditText;

        Button incrementButton, decrementButton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cartItem_img);
            deleteCartItem = itemView.findViewById(R.id.cartItemDelete);
            name = itemView.findViewById(R.id.cartItem_name);
            rating = itemView.findViewById(R.id.cartItem_ratting);
            price = itemView.findViewById(R.id.cartItem_price);
            itemTotalPrice = itemView.findViewById(R.id.cart_total_price);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            quantityEditText = itemView.findViewById(R.id.quantityEditText);
        }
    }
}
