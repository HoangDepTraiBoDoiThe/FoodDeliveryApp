package com.example.fooddeliveryapp.ui.Order;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Activities.OrderDetailsActivity;
import com.example.fooddeliveryapp.Adapters.Order.OrderAdapter;
import com.example.fooddeliveryapp.Models.Order.OrderModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private List<OrderModel> orderList;
    private OrderAdapter orderAdapter;
    private RecyclerView recyclerView;

    public OrderFragment() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerView = view.findViewById(R.id.order_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderModel order) {
                openOrderDetailsActivity(order);
            }
        });

        fetchOrdersFromFirebase();

        return view;
    }

    private void openOrderDetailsActivity(OrderModel order) {
        Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
        intent.putExtra("order", order);
        startActivity(intent);
    }


    String hardcodedUserID = "1";
    private void fetchOrdersFromFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderID = orderSnapshot.child("orderID").getValue(String.class);
                    String orderStatus = orderSnapshot.child("status").getValue(String.class);
                    String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                    String orderAddress = orderSnapshot.child("shippingAddress").getValue(String.class);
                    String paymentMethod = orderSnapshot.child("paymentMethod").getValue(String.class);
                    String userID = orderSnapshot.child("userId").getValue(String.class);

                    if (userID != null && userID.equals(hardcodedUserID)) {
                        OrderModel order = new OrderModel(orderID, orderStatus, orderDate, orderAddress, paymentMethod, userID);
                        orderList.add(order);
                        fetchOrderItemsFromFirebase(order);
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchOrderItemsFromFirebase(OrderModel order) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("orderItems");
        Query query = orderItemsRef.orderByChild("orderID").equalTo(order.getOrderID());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Initialize a list to keep track of processed orders
                ArrayList<String> processedOrders = new ArrayList<>();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String foodID = itemSnapshot.child("foodID").getValue(String.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    // Fetch the corresponding FoodItemModel using the foodID
                    DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference("foods").child(foodID);
                    foodItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                            double totalPrice = 0.0;

                            if (foodSnapshot.exists()) {
                                double foodPrice = foodSnapshot.child("foodPrice").getValue(Double.class);
                                totalPrice += (foodPrice * quantity);

                                // Add the order to the processed list to avoid duplicates
                                processedOrders.add(order.getOrderID());

                                // If all order items for this order have been processed, update the order and notify the adapter
                                if (processedOrders.size() == dataSnapshot.getChildrenCount()) {
                                    // Update the order with the calculated total price
                                    order.setTotalPrice(String.valueOf(totalPrice));

                                    // If the order is not already in the list, add it
                                    if (!orderList.contains(order)) {
                                        orderList.add(order);
                                    }

                                    // Notify the adapter that the data has changed
                                    orderAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database errors, if any
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });
    }

}

