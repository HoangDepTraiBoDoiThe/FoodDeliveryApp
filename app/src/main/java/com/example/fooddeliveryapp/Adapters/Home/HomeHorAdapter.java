package com.example.fooddeliveryapp.Adapters.Home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.Home.HomeHorModel;
import com.example.fooddeliveryapp.Models.Home.HomeVerModel;
import com.example.fooddeliveryapp.R;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> homeHorModels;
    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> homeHorModels) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.homeHorModels = homeHorModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int AdapterPosition = holder.getAdapterPosition();

        holder.imageView.setImageResource(homeHorModels.get(AdapterPosition).getImage());
        holder.name.setText(homeHorModels.get(AdapterPosition).getName());

        if (check) {
            getDataFromFirebase(AdapterPosition, "pizza");
            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = AdapterPosition;
                notifyDataSetChanged();

                // Load data for the selected item from Firebase
                String selectedHorItem = homeHorModels.get(AdapterPosition).getFoodType();
                getDataFromFirebase(AdapterPosition, selectedHorItem);
            }
        });


        if (select) {
            if (AdapterPosition == 0) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
                select = false;
            }
        } else {
            if (row_index == AdapterPosition) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
            } else {
                holder.cardView.setBackgroundResource(R.drawable.default_bg);
            }
        }
    }

    public void getDataFromFirebase (int position, String typeOfFood)
    {
        DatabaseReference foodsRef = FirebaseDatabase.getInstance().getReference("foods");
        foodsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<HomeVerModel> foodsList = new ArrayList<>();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {

                    String foodType = foodSnapshot.child("foodType").getValue(String.class);
                    String foodID = null;
                    String foodName = null;
                    String foodDes = null;
                    String foodTiming = null;
                    String foodRating = null;
                    String foodPrice = null;
                    int imageResId = 0;
                    boolean isFav = false;

                    if (foodType.equals(typeOfFood)) {
                        // Extract data from the snapshot and create a foods object
                        foodID = foodSnapshot.getKey();
                        foodName = foodSnapshot.child("foodName").getValue(String.class);
                        foodDes = foodSnapshot.child("foodDescription").getValue(String.class);
                        foodTiming = foodSnapshot.child("foodTiming").getValue(String.class);
                        foodRating = foodSnapshot.child("foodRatting").getValue(String.class);
                        foodPrice = String.valueOf(foodSnapshot.child("foodPrice").getValue(Integer.class)) + ",00$";
                        imageResId = getResourceIdByName(foodSnapshot.child("foodImage").getValue(String.class));
                        // Add the foods object to the list
                        foodsList.add(new HomeVerModel(foodID, foodName, foodDes, imageResId, foodPrice, foodRating, foodTiming, foodType));
                    }
                }
                updateVerticalRec.CallBack(position, foodsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database errors, if any
            }
        });

    }

    @Override
    public int getItemCount() {
        return homeHorModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return activity.getResources().getIdentifier(name, "drawable", activity.getPackageName());
    }
}
