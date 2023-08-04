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
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    String hardcodedUserID = "0";
    private void fetchOrdersFromFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    if (orderSnapshot.exists()) {
                        String orderID = orderSnapshot.getKey();
                        String orderStatus = orderSnapshot.child("status").getValue(String.class);
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        String orderAddress = orderSnapshot.child("shippingAddress").getValue(String.class);
                        String paymentMethod = orderSnapshot.child("paymentMethod").getValue(String.class);
                        String userID = orderSnapshot.child("userID").getValue(String.class);
                        int statusImage = getResourceIdByName(orderStatus.toLowerCase());

                        if (userID != null && userID.equals(hardcodedUserID)) {
                            OrderModel order = new OrderModel(orderID, orderStatus, orderDate, orderAddress, paymentMethod, userID, statusImage);
                            orderList.add(order);
                            GetOrderTotalPrice(order);
                        }
                    }
                }
                Collections.sort(orderList, new Comparator<OrderModel>() {
                    @Override
                    public int compare(OrderModel order1, OrderModel order2) {
                        return order1.getOrderDate().compareTo(order2.getOrderDate());
                    }
                });

                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void GetOrderTotalPrice(OrderModel order) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("orderItems");
        Query query = orderItemsRef.orderByChild("orderID").equalTo(order.getOrderID());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AtomicDouble totalPrice = new AtomicDouble(0.0);

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    String foodID = itemSnapshot.child("foodID").getValue(String.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);

                    DatabaseReference foodItemsRef = FirebaseDatabase.getInstance().getReference("foods").child(foodID);
                    foodItemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot foodSnapshot) {
                            if (foodSnapshot.exists()) {
                                double foodPrice = foodSnapshot.child("foodPrice").getValue(Double.class);
                                totalPrice.addAndGet(foodPrice * quantity);
                                order.setTotalPrice(String.valueOf(totalPrice.get()));
                                handleOrder(order);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

    private void handleOrder(OrderModel order) {
        // Check if the order is not already in the list, then add it
        if (!orderList.contains(order)) {
            orderList.add(order);
        }

        orderAdapter.notifyDataSetChanged();
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        if (getContext() != null) {
            return getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        } else {
            return 0;
        }
    }
}
