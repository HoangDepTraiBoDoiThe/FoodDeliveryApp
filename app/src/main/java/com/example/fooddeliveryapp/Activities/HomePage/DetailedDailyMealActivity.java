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
import org.json.JSONException;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        detailedDailyModels = new ArrayList<>();
        detailedDailyAdapter = new DetailedDailyAdapter(detailedDailyModels);
        recyclerView.setAdapter(detailedDailyAdapter);

        getDataFromFirebase ();
    }

    public void getDataFromFirebase ()
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<DetailedDailyModel> foodsList = new ArrayList<>();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    // Extract data from the snapshot and create a foods object
                    DetailedDailyModel foodName = foodSnapshot.getValue(DetailedDailyModel.class);


                    // Add the foods object to the list
                    foodsList.add(foodName);
                }

                detailedDailyAdapter.notifyDataSetChanged();
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
