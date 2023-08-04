package com.example.fooddeliveryapp.ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.fooddeliveryapp.Activities.HomePage.FoodSearchResultActivity;
import com.example.fooddeliveryapp.Adapters.Home.HomeHorAdapter;
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Adapters.Home.HomeIntrface;
import com.example.fooddeliveryapp.Models.Home.HomeHorModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements HomeIntrface {

    private FragmentHomeBinding binding;

    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;

    ViewPager2 foodSrearchView;

    //////////// Vertical
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;
    TextView searchEditText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchEditText = root.findViewById(R.id.searchEditText);
        foodSrearchView = root.findViewById(R.id.view_paper2);
        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FoodSearchResultActivity.class);
                getContext().startActivity(intent);
            }
        });

    ////// Horizontal
        homeHorModelList = new ArrayList<>();
        homeHorModelList.add(new HomeHorModel((R.drawable.pizza6), "Pizza", "pizza"));
        homeHorModelList.add(new HomeHorModel((R.drawable.burger5), "Burger", "burger"));
        homeHorModelList.add(new HomeHorModel((R.drawable.fries5), "Fries", "fries"));
        homeHorModelList.add(new HomeHorModel((R.drawable.ice_cream5), "Ice cream", "icecream"));
        homeHorModelList.add(new HomeHorModel((R.drawable.sandwich5), "Sandwich", "sandwich"));

        homeHorAdapter = new HomeHorAdapter(this, getActivity(), homeHorModelList);

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);

    ////// Vertical
        homeVerModelList = new ArrayList<>();

        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        return root;
    }

    // Method to show the search results in a new fragment or dialog
    private void showSearchResultsFragment(ArrayList<HomeVerModel> searchResults) {

        Intent intent = new Intent(getContext(), FoodSearchResultActivity.class);
       // intent.putExtra("searchResults", searchResults);
        getContext().startActivity(intent);

    }

    public void performSearch (String searchQuery)
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        Query query = foodsRef.orderByChild("foodType").startAt(searchQuery).endAt(searchQuery + "\uf8ff"); //[chQuery).endAt(searchQuery + "\uf8ff");]

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                for (DataSnapshot foodItem : dataSnapshot.getChildren()) {
                    // ... Extract data and create HomeVerModel instances
                    homeVerModels.add(getFoodItemDataFromFirebase(foodItem));
                }
                showSearchResultsFragment(homeVerModels);
                //CallBack(0, homeVerModels);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (binding != null) {
            binding = null;
        }
    }

    @Override
    public void CallBack(int position, ArrayList<HomeVerModel> list) {

        homeVerAdapter = new HomeVerAdapter(getContext(), list);
        homeVerAdapter.notifyDataSetChanged();
        homeVerticalRec.setAdapter(homeVerAdapter);

    }

    @Override
    public void getFoodsDataFromFirebaseByType(int position, String typeOfFood) {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HomeVerModel> foodsList = new ArrayList<>();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {

                    String foodType = foodSnapshot.child("foodType").getValue(String.class);

                    if (foodType.equals(typeOfFood)) {
                        foodsList.add(getFoodItemDataFromFirebase(foodSnapshot));
                    }
                }
                CallBack(position, foodsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        double foodPrice = foodItem.child("foodPrice").getValue(Integer.class);
        int imageResId = getResourceIdByName(foodItem.child("foodImage").getValue(String.class));

        HomeVerModel homeVerModels = new HomeVerModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType);

        return homeVerModels;
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
    }
}