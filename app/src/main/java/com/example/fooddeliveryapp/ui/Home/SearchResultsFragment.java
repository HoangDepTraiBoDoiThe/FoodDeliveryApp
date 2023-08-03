package com.example.fooddeliveryapp.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Activities.OrderDetailsActivity;
import com.example.fooddeliveryapp.Adapters.Home.SearchResultsAdapter;
import com.example.fooddeliveryapp.Adapters.Order.OrderAdapter;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.Models.Order.OrderModel;
import com.example.fooddeliveryapp.R;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.firebase.database.*;
import com.google.firebase.database.core.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {

    private Context context;
    private List<HomeVerModel> searchResults;

    private SearchResultsAdapter searchResultsAdapter;

    public SearchResultsFragment() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);



        //fetchOrdersFromFirebase();

        return view;
    }


    String hardcodedUserID = "1";
    private void fetchOrdersFromFirebase() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchResults.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderID = orderSnapshot.getKey();
                    String orderStatus = orderSnapshot.child("status").getValue(String.class);
                    String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                    String orderAddress = orderSnapshot.child("shippingAddress").getValue(String.class);
                    String paymentMethod = orderSnapshot.child("paymentMethod").getValue(String.class);
                    String userID = orderSnapshot.child("userId").getValue(String.class);

                    if (userID != null && userID.equals(hardcodedUserID)) {
                        OrderModel order = new OrderModel(orderID, orderStatus, orderDate, orderAddress, paymentMethod, userID);
                       // searchResults.add(order);
                        fetchOrderItemsFromFirebase(order);
                    }
                }
                searchResultsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetchOrderItemsFromFirebase(OrderModel order) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("foods");
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

                                // Add the order to the list
                               // handleOrder(order);
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

    private void handleOrder(HomeVerModel homeVerModel) {
        // Check if the order is not already in the list, then add it
        if (!searchResults.contains(homeVerModel)) {
            searchResults.add(homeVerModel);
        }

        searchResultsAdapter.notifyDataSetChanged();
    }

}