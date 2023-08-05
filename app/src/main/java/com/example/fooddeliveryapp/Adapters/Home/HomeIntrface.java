package com.example.fooddeliveryapp.Adapters.Home;

import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface HomeIntrface {
    public void CallBack(int position, ArrayList<HomeVerModel> list);
    public void getFoodsDataFromFirebaseByType(int position, String typeOfFood);
    public HomeVerModel getFoodItemDataFromFirebase(DataSnapshot foodItem);
}
