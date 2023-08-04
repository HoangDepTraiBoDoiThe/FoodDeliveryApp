package com.example.fooddeliveryapp.Activities.HomePage;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Adapters.Home.SearchResultsAdapter;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodSearchResultActivity extends AppCompatActivity {

    ArrayList<HomeVerModel> homeVerModels;
    EditText searchBox;
    RecyclerView searchRecycleView;

    HomeVerAdapter homeVerAdapter;

    private FloatingActionButton backToTopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search_result);

        backToTopButton = findViewById(R.id.backToTopButton);

        searchBox = findViewById(R.id.searchEditText);
        searchBox.requestFocus();

        searchRecycleView = findViewById(R.id.searchResultsRecyclerView);
        searchRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && backToTopButton.getVisibility() != View.VISIBLE) {
                    backToTopButton.show();
                } else if (dy < 0 && backToTopButton.getVisibility() == View.VISIBLE) {
                    backToTopButton.hide();
                }
            }
        });

        backToTopButton.hide();
        backToTopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Scroll RecyclerView to top
                searchRecycleView.smoothScrollToPosition(0);
            }
        });

        searchRecycleView.setLayoutManager(new LinearLayoutManager(this));

        homeVerModels = new ArrayList<>();
        homeVerAdapter = new HomeVerAdapter(this, homeVerModels);

        searchRecycleView.setAdapter(homeVerAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform search operation based on charSequence (user input)
                String searchQuery = charSequence.toString();
                performSearch(searchQuery);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void performSearch (String searchQuery)
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        Query query = foodsRef.orderByChild("foodType").startAt(searchQuery).endAt(searchQuery + "\uf8ff"); //[chQuery).endAt(searchQuery + "\uf8ff");]

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeVerModels.clear();
                for (DataSnapshot foodItem : dataSnapshot.getChildren()) {
                    // ... Extract data and create HomeVerModel instances

                    homeVerModels.add(getFoodItemDataFromFirebase(foodItem));
                }
                showSearchResultsFragment(homeVerModels);

                homeVerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // Method to show the search results in a new fragment or dialog
    private void showSearchResultsFragment(ArrayList<HomeVerModel> searchResults) {




    }

    // Helper method
    public HomeVerModel getFoodItemDataFromFirebase(DataSnapshot foodItem) {
        String foodID = foodItem.getKey();
        String foodName = foodItem.child("foodName").getValue(String.class);
        String foodType = foodItem.child("foodType").getValue(String.class);
        String foodDes = foodItem.child("foodDescription").getValue(String.class);
        String foodTiming = foodItem.child("foodTiming").getValue(String.class);
        String foodRating = foodItem.child("foodRatting").getValue(String.class);
        double foodPrice = foodItem.child("foodPrice").getValue(double.class);
        int imageResId = getResourceIdByName(foodItem.child("foodImage").getValue(String.class));

        HomeVerModel homeVerModels = new HomeVerModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType);

        return homeVerModels;
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getPackageName());
    }
}