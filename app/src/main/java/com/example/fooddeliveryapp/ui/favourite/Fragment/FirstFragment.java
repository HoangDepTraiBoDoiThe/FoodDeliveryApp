package com.example.fooddeliveryapp.ui.favourite.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Featured.FeaturedAdapter;
import com.example.fooddeliveryapp.Adapters.Featured.FeaturedVerAdapter;
import com.example.fooddeliveryapp.Models.Featured.FeaturedModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {


    ///////////// Featured Hor Rec
    List<FeaturedModel> featuredModelList;
    RecyclerView recyclerView;
    FeaturedAdapter featuredAdapter;

    ///////////// Featured ver Rec
    RecyclerView recyclerView2;
    FeaturedVerAdapter featuredVerAdapter;

    public FirstFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ///////////// Featured Hor Rec
        recyclerView = view.findViewById(R.id.featured_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredModelList = new ArrayList<>();

        featuredModelList.add(new FeaturedModel(R.drawable.fav1, "Featured 1", "Description 1"));
        featuredModelList.add(new FeaturedModel(R.drawable.fav2, "Featured 2", "Description 2"));
        featuredModelList.add(new FeaturedModel(R.drawable.fav3, "Featured 3", "Description 3"));

        featuredAdapter = new FeaturedAdapter(featuredModelList);
        recyclerView.setAdapter(featuredAdapter);

        ///////////// Featured Ver Rec
        recyclerView2 = view.findViewById(R.id.featured_ver_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        GetFavouriteFoodFromFirebase();

        return view;
    }

    String hardcodedUserID = "0";

    private void GetFavouriteFoodFromFirebase() {
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference currentUser = FirebaseDatabase.getInstance().getReference("users").child(hardcodedUserID);

        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> favoriteFoodIDs = new ArrayList<>();
                    DataSnapshot favoritesSnapshot = dataSnapshot.child("favorites");
                    for (DataSnapshot foodIDSnapshot : favoritesSnapshot.getChildren()) {
                        String foodID = foodIDSnapshot.getValue(String.class);
                        favoriteFoodIDs.add(foodID);
                    }

                    fetchFavoriteFoodsDetails(favoriteFoodIDs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });
    }

    private void fetchFavoriteFoodsDetails(List<String> favoriteFoodIDs) {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();

                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    String foodType = null;
                    String foodID = foodSnapshot.getKey();
                    String foodName = null;
                    String foodDes = null;
                    String foodTiming = null;
                    String foodRating = null;
                    String foodPrice = null;
                    int imageResId = 0;

                    if (favoriteFoodIDs.contains(foodID)) {
                        // Extract data from the snapshot and create a foods object
                        foodID = foodSnapshot.getKey();
                        foodName = foodSnapshot.child("foodName").getValue(String.class);
                        foodDes = foodSnapshot.child("foodDescription").getValue(String.class);
                        foodTiming = foodSnapshot.child("foodTiming").getValue(String.class);
                        foodRating = foodSnapshot.child("foodRatting").getValue(String.class);
                        foodPrice = String.valueOf(foodSnapshot.child("foodPrice").getValue(Integer.class)) + ",00$";
                        imageResId = getResourceIdByName(foodSnapshot.child("foodImage").getValue(String.class));
                        foodType = foodSnapshot.child("foodType").getValue(String.class);

                        homeVerModels.add(new HomeVerModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType));
                    }
                }
                featuredVerAdapter = new FeaturedVerAdapter(homeVerModels, getContext());
                recyclerView2.setAdapter(featuredVerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getContext().getPackageName());
    }
}