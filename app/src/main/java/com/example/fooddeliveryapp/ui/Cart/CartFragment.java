package com.example.fooddeliveryapp.ui.Cart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Cart.CartAdapter;
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Models.Cart.CartModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.Models.Order.OrderItemModel;
import com.example.fooddeliveryapp.Models.Order.OrderModel;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.databinding.FragmentMyCartBinding;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.CartAdapterCallback{

    ArrayList<CartModel> cartModelList;
    ArrayList<HomeVerAdapter> foodItem;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;

    Button btnMakeOrder, btnCancelOrder;

    double cartTotalPrice = 0;
    TextView cartTotalPrice_text;
    DatabaseReference cartItemRef;
    AlertDialog dialog;

    private FragmentMyCartBinding binding;
    public CartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyCartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        recyclerView = view.findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnMakeOrder = view.findViewById(R.id.btnMakeOrder);

        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceOrderDialog();

            }
        });

        cartModelList = new ArrayList<>();
        foodItem = new ArrayList<>();

        cartAdapter = new CartAdapter(cartModelList, cartTotalPrice, this, getContext());
        recyclerView.setAdapter(cartAdapter);

        GetCartIDFromFirebase();
        cartTotalPrice_text = view.findViewById(R.id.cartTotalPrice_text);

        cartItemRef = FirebaseDatabase.getInstance().getReference("cartItems");

        return  view;
    }

    String hardcodedUserID = "0";
    private void GetCartIDFromFirebase() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");

        userRef.child(hardcodedUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userCartID = dataSnapshot.child("cartID").getValue(String.class);
                    //fetchCartFromFirebase(userCartID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetchCartFromFirebase(String userCartID) {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cartItems");
        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("foods");
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartModelList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                        String cartID = cartSnapshot.child("cartID").getValue(String.class);
                        String cartItemID = cartSnapshot.getKey();
                        String cartFoodID = cartSnapshot.child("foodID").getValue(String.class);
                        int quantity = cartSnapshot.child("quantity").getValue(Integer.class);
                        if (cartID != null && cartID.equals(userCartID)) {
                            foodRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot foodSnapshot) {
                                    foodItem.clear();
                                    for (DataSnapshot foodSnap : foodSnapshot.getChildren()) {
                                        if (foodSnap.exists()) {
                                            String foodID = foodSnap.getKey();
                                            if (foodID.equals(cartFoodID)) {
                                                String foodName = foodSnap.child("foodName").getValue(String.class);
                                                String foodType = foodSnap.child("foodType").getValue(String.class);
                                                String foodDes = foodSnap.child("foodDescription").getValue(String.class);
                                                String foodTiming = foodSnap.child("foodTiming").getValue(String.class);
                                                String foodRating = foodSnap.child("foodRatting").getValue(String.class);
                                                double foodPrice = foodSnap.child("foodPrice").getValue(Integer.class);
                                                int imageResId = getResourceIdByName(foodSnap.child("foodImage").getValue(String.class));

                                                CartModel cart = new CartModel(cartID, cartItemID, cartFoodID, quantity, imageResId, foodName, foodDes, foodRating, foodPrice, foodTiming, foodType);
                                                cartModelList.add(cart);
                                                cartTotalPrice += cart.getItemTotalPrice();
                                                onQuantityChanged(cartTotalPrice);
                                            }
                                            cartAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (binding != null) {
            binding = null;
        }
    }

    private ValueEventListener cartItemListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            cartModelList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    String cartID = cartSnapshot.child("cartID").getValue(String.class);
                    String cartItemID = cartSnapshot.getKey();
                    String cartFoodID = cartSnapshot.child("foodID").getValue(String.class);
                    int quantity = cartSnapshot.child("quantity").getValue(Integer.class);

                    if (cartID != null && cartID.equals(hardcodedUserID)) {
                        DatabaseReference foodRef = FirebaseDatabase.getInstance().getReference("foods");
                        foodRef.child(cartFoodID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot foodSnap) {
                                if (foodSnap.exists()) {
                                    // Fetch food item information
                                    String foodName = foodSnap.child("foodName").getValue(String.class);
                                    String foodType = foodSnap.child("foodType").getValue(String.class);
                                    String foodDes = foodSnap.child("foodDescription").getValue(String.class);
                                    String foodTiming = foodSnap.child("foodTiming").getValue(String.class);
                                    String foodRating = foodSnap.child("foodRatting").getValue(String.class);
                                    double foodPrice = foodSnap.child("foodPrice").getValue(Integer.class);
                                    int imageResId = getResourceIdByName(foodSnap.child("foodImage").getValue(String.class));

                                    CartModel cart = new CartModel(cartID, cartItemID, cartFoodID, quantity, imageResId, foodName, foodDes, foodRating, foodPrice, foodTiming, foodType);
                                    cartModelList.add(cart);
                                    cartTotalPrice += cart.getItemTotalPrice();
                                    onQuantityChanged(cartTotalPrice);
                                    cartAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle onCancelled
                            }
                        });
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Handle onCancelled
        }
    };


    private void createOrder(String Method, String Address) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("orderItems");
        String userID = hardcodedUserID;

        String orderID = ordersRef.push().getKey();
        String orderDate = getCurrentDateTime();
        String paymentMethod = Address;
        String shippingAddress = Address;
        String status = "Pending";
        OrderModel newOrder = new OrderModel(orderID, status, orderDate, shippingAddress, paymentMethod, userID);
        ordersRef.child(orderID).setValue(newOrder);

        cartItemRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String cartFoodID = snapshot.child("foodID").getValue(String.class);
                        int cartQuantity = snapshot.child("quantity").getValue(Integer.class);

                        OrderItemModel orderItem = new OrderItemModel(orderID, cartFoodID, cartQuantity);
                        String orderItemID = orderItemsRef.push().getKey();
                        orderItemsRef.child(orderItemID).setValue(orderItem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });

        clearCart();
    }

    private void showPlaceOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_place_order, null);
        builder.setView(dialogView);

        Spinner spinnerPaymentMethod = dialogView.findViewById(R.id.spinnerPaymentMethod);
        EditText editShippingAddress = dialogView.findViewById(R.id.editShippingAddress);
        Button btnPlaceOrder = dialogView.findViewById(R.id.btnPlaceOrder);

        dialog = builder.create();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
                String shippingAddress = editShippingAddress.getText().toString();

                // Check if paymentMethod and shippingAddress are filled
                if (!paymentMethod.isEmpty() && !shippingAddress.isEmpty()) {
                    // Call createOrder() with paymentMethod and shippingAddress parameters
                    createOrder(paymentMethod, shippingAddress);
                    dialog.dismiss();
                } else {
                    // Show an error message if any field is empty
                    // You can also customize this part as needed
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

        btnCancelOrder = dialogView.findViewById(R.id.btnCancelOrder);
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private String getCurrentDateTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    // Create a method to clear the cart after order creation
    private void clearCart() {
        DatabaseReference cartItemsRef = FirebaseDatabase.getInstance().getReference("cartItems");
        cartItemsRef.removeValue(); // Remove all cart items
        cartModelList.clear();
        cartAdapter.notifyDataSetChanged();

        // Update UI and notify the user that the order has been placed
        // For example, you can navigate to a new fragment/activity or display a message.
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }

    @Override
    public void onQuantityChanged(double newTotalPrice) {
        cartTotalPrice_text.setText(String.format(Locale.getDefault(), "%.2f", newTotalPrice));
        cartTotalPrice = newTotalPrice;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add your listeners here
        cartItemRef.addValueEventListener(cartItemListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Remove your listeners here
        cartItemRef.removeEventListener(cartItemListener);
    }


}