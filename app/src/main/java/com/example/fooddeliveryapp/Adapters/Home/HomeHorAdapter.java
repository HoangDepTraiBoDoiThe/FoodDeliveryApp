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
import com.example.fooddeliveryapp.R;

import java.util.ArrayList;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    HomeIntrface homeIntrface;
    Activity activity;
    ArrayList<HomeHorModel> homeHorModels;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(HomeIntrface homeIntrface, Activity activity, ArrayList<HomeHorModel> homeHorModels) {
        this.homeIntrface = homeIntrface;
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
            homeIntrface.getFoodsDataFromFirebaseByType(AdapterPosition, "pizza");
            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = AdapterPosition;
                notifyDataSetChanged();

                // Load data for the selected item from Firebase
                String selectedHorItem = homeHorModels.get(AdapterPosition).getFoodType();
                homeIntrface.getFoodsDataFromFirebaseByType(AdapterPosition, selectedHorItem);
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
}
