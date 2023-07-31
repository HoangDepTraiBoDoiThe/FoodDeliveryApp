package com.example.fooddeliveryapp.Activities.HomePage;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.DailyMeal.DetailedDailyAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.R;

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

        // Load data from JSON file
        try {
            JSONArray itemArray = loadJSONFromRaw(getResources(), R.raw.detailed_daily_meal);
            if (itemArray != null) {

                JSONArray typeObject = null;

                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject itemObject = itemArray.getJSONObject(i);
                    typeObject = itemObject.getJSONArray(type);
                }

                if (typeObject != null) {
                    imageView.setImageResource(getResourceIdByName(typeObject.getJSONObject(0).getString("type")));
                   // JSONArray itemsArray = typeObject.getJSONArray("items");
                    for (int i = 0; i < typeObject.length(); i++) {
                        JSONObject itemObject = typeObject.getJSONObject(i);

                        int imageResId = getResourceIdByName(itemObject.getString("image"));
                        String name = itemObject.getString("name");
                        String description = itemObject.getString("description");
                        String rating = itemObject.getString("rating");
                        String price = itemObject.getString("price");
                        String timing = itemObject.getString("timing");

                        detailedDailyModels.add(new DetailedDailyModel(imageResId, name, description, rating, price, timing));
                    }

                    detailedDailyAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
