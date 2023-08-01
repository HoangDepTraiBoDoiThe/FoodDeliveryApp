package com.example.fooddeliveryapp.Activities;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Order.OrderItemAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.Models.Order.OrderItemModel;
import com.example.fooddeliveryapp.Models.Order.OrderModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    OrderModel order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        order = (OrderModel) getIntent().getSerializableExtra("order");

        loadOrderDetailsFromFirebase(order.getOrderID());
        loadOrderItemsFromFirebase(order.getOrderID());
    }

    private void loadOrderDetailsFromFirebase(String orderID) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders").child(orderID);
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String totalPrice = "$" + order.getTotalPrice();
                    String status = dataSnapshot.child("status").getValue(String.class);
                    String orderDate = dataSnapshot.child("orderDate").getValue(String.class);
                    String paymentMethod = dataSnapshot.child("paymentMethod").getValue(String.class);
                    String shippingAddress = dataSnapshot.child("shippingAddress").getValue(String.class);

                    // Update the TextView elements with the fetched order details
                    TextView totalPriceTextView = findViewById(R.id.total_price);
                    totalPriceTextView.setText(totalPrice);

                    TextView statusTextView = findViewById(R.id.status);
                    statusTextView.setText(status);

                    TextView orderDateTextView = findViewById(R.id.order_date);
                    orderDateTextView.setText(orderDate);

                    TextView paymentMethodTextView = findViewById(R.id.payment_method);
                    paymentMethodTextView.setText(paymentMethod);

                    TextView shippingAddressTextView = findViewById(R.id.shipping_address);
                    shippingAddressTextView.setText(shippingAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });

    }

    private void loadOrderItemsFromFirebase(String orderID) {
        DatabaseReference orderItemsRef = FirebaseDatabase.getInstance().getReference("orderItems");
        Query query = orderItemsRef.orderByChild("orderID").equalTo(orderID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<OrderItemModel> orderItemModelList = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    OrderItemModel orderItem = new OrderItemModel(itemSnapshot.child("foodID").getValue(String.class));
                    orderItemModelList.add(orderItem);
                }

                // Fetch the details of the food items using the foodID from each OrderItemModel
                fetchFoodItemsDetails(orderItemModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });
    }

    private void fetchFoodItemsDetails(ArrayList<OrderItemModel> orderItems) {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<OrderItemModel> orderItemModels = new ArrayList<>();
                for (OrderItemModel orderItem : orderItems) {
                    String foodID = orderItem.getId();
                    DataSnapshot foodSnapshot = dataSnapshot.child(foodID);
                    if (foodSnapshot.exists()) {

                        String foodType = foodSnapshot.child("foodType").getValue(String.class);
                        foodID = foodSnapshot.child("foodID").getValue(String.class);
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String foodDes = foodSnapshot.child("foodDescription").getValue(String.class);
                        String foodTiming = foodSnapshot.child("foodTiming").getValue(String.class);
                        String foodRating = foodSnapshot.child("foodRatting").getValue(String.class);
                        String foodPrice = foodSnapshot.child("foodPrice").getValue(Integer.class) + ",00$";
                        int imageResId = getResourceIdByName(foodSnapshot.child("foodImage").getValue(String.class));

                        orderItemModels.add(new OrderItemModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType));
                    }
                }

                // Display the food items in the RecyclerView
                setupOrderItemsRecyclerView(orderItemModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });
    }


    private void setupOrderItemsRecyclerView(ArrayList<OrderItemModel> foodItems) {
        RecyclerView orderItemsRecyclerView = findViewById(R.id.order_items_recycler_view);
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(foodItems);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.setAdapter(orderItemAdapter);
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}