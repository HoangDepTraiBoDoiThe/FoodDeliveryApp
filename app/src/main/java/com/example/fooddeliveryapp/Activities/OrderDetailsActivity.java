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

                    String foodId_order = itemSnapshot.child("foodID").getValue(String.class);
                    int quantity_order = itemSnapshot.child("quantity").getValue(Integer.class);
                    OrderItemModel orderItem = new OrderItemModel(foodId_order, quantity_order);
                    orderItemModelList.add(orderItem);
                }

                fetchFoodItemsDetails(orderItemModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        foodID = foodSnapshot.getKey();
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String foodDes = foodSnapshot.child("foodDescription").getValue(String.class);
                        String foodTiming = foodSnapshot.child("foodTiming").getValue(String.class);
                        String foodRating = foodSnapshot.child("foodRatting").getValue(String.class);
                        String foodPrice = foodSnapshot.child("foodPrice").getValue(Integer.class) + ".00$";
                        String orderTotalPrice = order.getTotalPrice() + "0$";
                        int foodQuantity = orderItem.getQuantity_int();
                        int imageResId = getResourceIdByName(foodSnapshot.child("foodImage").getValue(String.class));

                        orderItemModels.add(new OrderItemModel(foodID, foodName, foodDes, imageResId, orderTotalPrice, foodPrice, foodRating, foodTiming, foodType, foodQuantity));
                    }
                }

                setupOrderItemsRecyclerView(orderItemModels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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