package com.example.fooddeliveryapp.ui.Home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Home.HomeHorAdapter;
import com.example.fooddeliveryapp.Adapters.Home.HomeVerAdapter;
import com.example.fooddeliveryapp.Adapters.Home.UpdateVerticalRec;
import com.example.fooddeliveryapp.Models.Home.HomeHorModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UpdateVerticalRec {

    private FragmentHomeBinding binding;

    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;


    //////////// Vertical
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;
    EditText searchEditText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchEditText = root.findViewById(R.id.editText3);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Perform search operation based on charSequence (user input)
                String searchQuery = charSequence.toString();
                performSearch(searchQuery);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchQuery = editable.toString();
                performSearch(searchQuery);
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
        // Create and show a new fragment or dialog to display search results
        CallBack(0, searchResults);
    }

    public void performSearch (String searchQuery)
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                for (DataSnapshot foodItem : snapshot.getChildren()) {
                    String foodName = foodItem.child("foodName").getValue(String.class);
                    if (searchQuery.equals(foodName)) {
                        homeVerModels.add(getFoodItemDataFromFirebase(foodItem));
                    }
                }
                showSearchResultsFragment(homeVerModels);
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
        String foodPrice = foodItem.child("foodPrice").getValue(Integer.class) + ",00$";
        int imageResId = getResourceIdByName(foodItem.child("foodImage").getValue(String.class));

        HomeVerModel homeVerModels = new HomeVerModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType);

        return homeVerModels;
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
    }
}