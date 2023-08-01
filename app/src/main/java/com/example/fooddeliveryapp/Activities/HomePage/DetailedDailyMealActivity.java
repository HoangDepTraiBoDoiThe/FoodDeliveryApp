package com.example.fooddeliveryapp.Activities.HomePage;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.DailyMeal.DetailedDailyAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
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
                    String foodID = null;
                    String foodName = null;
                    String foodDes = null;
                    String foodTiming = null;
                    String foodRating = null;
                    String foodPrice = null;
                    int imageResId = 0;
                    if (foodType.equals(typeOfFood)) {
                        // Extract data from the snapshot and create a foods object
                        foodID = foodSnapshot.child("foodID").getValue(String.class);
                        foodName = foodSnapshot.child("foodName").getValue(String.class);
                        foodDes = foodSnapshot.child("foodDescription").getValue(String.class);
                        foodTiming = foodSnapshot.child("foodTiming").getValue(String.class);
                        foodRating = foodSnapshot.child("foodRatting").getValue(String.class);
                        foodPrice = String.valueOf(foodSnapshot.child("foodPrice").getValue(Integer.class)) + ",00$";
                        imageResId = getResourceIdByName(foodSnapshot.child("foodImage").getValue(String.class));
                        // Add the foods object to the list
                        detailedDailyModels.add(new DetailedDailyModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType));
                    }
                    detailedDailyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });

    }

    // Helper method to load JSON data from raw resource
    private JSONArray loadJSONFromRaw(android.content.res.Resources resources, int resourceId) {
        try {
            InputStream inputStream = resources.openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream);
            StringBuilder jsonString = new StringBuilder();
            while (scanner.hasNext()) {
                jsonString.append(scanner.nextLine());
            }
            scanner.close();
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            return jsonObject.getJSONArray("food_items");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}
