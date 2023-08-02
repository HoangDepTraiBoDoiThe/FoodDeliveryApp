package com.example.fooddeliveryapp.Activities.HomePage;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.DailyMeal.DetailedDailyAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetailedDailyMealActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DetailedDailyModel> detailedDailyModels;
    DetailedDailyAdapter detailedDailyAdapter;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_daily_meal);

        String type = getIntent().getStringExtra("type");

        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_tittle_img);
        imageView.setImageResource(getResourceIdByName(type));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailedDailyModels = new ArrayList<>();
        detailedDailyAdapter = new DetailedDailyAdapter(detailedDailyModels, this);

        recyclerView.setAdapter(detailedDailyAdapter);

        getDataFromFirebase (type);
    }

    public void getDataFromFirebase (String typeOfFood)
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {

                    String foodType = foodSnapshot.child("foodType").getValue(String.class);
                    if (foodType.equals(typeOfFood)) {
                        detailedDailyModels.add(getFoodItemDataFromFirebase(foodSnapshot));
                    }
                    detailedDailyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // Helper method
    public DetailedDailyModel getFoodItemDataFromFirebase(DataSnapshot foodItem) {
        String foodID = foodItem.getKey();
        String foodName = foodItem.child("foodName").getValue(String.class);
        String foodType = foodItem.child("foodType").getValue(String.class);
        String foodDes = foodItem.child("foodDescription").getValue(String.class);
        String foodTiming = foodItem.child("foodTiming").getValue(String.class);
        String foodRating = foodItem.child("foodRatting").getValue(String.class);
        String foodPrice = foodItem.child("foodPrice").getValue(Integer.class) + ",00$";
        int imageResId = getResourceIdByName(foodItem.child("foodImage").getValue(String.class));

        DetailedDailyModel homeVerModels = new DetailedDailyModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType);

        return homeVerModels;
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}
