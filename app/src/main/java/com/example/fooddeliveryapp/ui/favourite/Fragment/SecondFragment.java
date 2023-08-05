package com.example.fooddeliveryapp.ui.favourite.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SecondFragment extends Fragment {

    private RecyclerView sortedRecyclerView;
    private HomeVerAdapter sortedAdapter;
    ArrayList<HomeVerModel> homeVerModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);

        sortedRecyclerView = rootView.findViewById(R.id.sortedRecyclerView);
        sortedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        homeVerModelList = new ArrayList<>();
        getDataFromFirebase();

        sortedAdapter = new HomeVerAdapter(getContext(), homeVerModelList);
        sortedRecyclerView.setAdapter(sortedAdapter);

        return rootView;
    }

    public void getDataFromFirebase() {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                homeVerModelList.clear();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    homeVerModelList.add(getFoodItemDataFromFirebase(foodSnapshot));
                }
                Collections.sort(homeVerModelList, new Comparator<HomeVerModel>() {
                    @Override
                    public int compare(HomeVerModel model1, HomeVerModel model2) {
                        return Double.compare(Double.parseDouble(model2.getRating()), Double.parseDouble(model1.getRating()));
                    }
                });
                sortedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
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
        return getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }
}
