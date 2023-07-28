package com.example.fooddeliveryapp.Activities.HomePage;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.DailyMeal.DetailedDailyAdapter;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.R;

import java.util.ArrayList;
import java.util.List;

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

        if (type != null && type.equalsIgnoreCase("breakfast")) {

            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav1, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav2, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.fav3, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyAdapter.notifyDataSetChanged();
        }

        if (type != null && type.equalsIgnoreCase("sweets")) {

            imageView.setImageResource(R.drawable.sweets);
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s1, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s2, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyModels.add(new DetailedDailyModel(R.drawable.s3, "breakfast", "Description", "5.0", "40,00", "10:00am - 09:00pm"));
            detailedDailyAdapter.notifyDataSetChanged();
        }
    }
}