package com.example.fooddeliveryapp.Adapters.DailyMeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.DailyMeal.DailyMealModel;
import com.example.fooddeliveryapp.Models.DailyMeal.DetailedDailyModel;
import com.example.fooddeliveryapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.zip.Inflater;

public class DetailedDailyAdapter extends RecyclerView.Adapter<DetailedDailyAdapter.ViewHolder> {

    List<DetailedDailyModel> detailedDailyModels;

    public DetailedDailyAdapter(List<DetailedDailyModel> detailedDailyModels) {
        this.detailedDailyModels = detailedDailyModels;
    }

    @NonNull
    @NotNull
    @Override
    public DetailedDailyAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DetailedDailyAdapter.ViewHolder holder, int position) {

        holder.imageView.setImageResource(detailedDailyModels.get(position).getImage());
        holder.name.setText(detailedDailyModels.get(position).getName());
        holder.price.setText(detailedDailyModels.get(position).getPrice());
        holder.description.setText(detailedDailyModels.get(position).getDescription());
        holder.rating.setText(detailedDailyModels.get(position).getRating());
        holder.timing.setText(detailedDailyModels.get(position).getTiming());

    }

    @Override
    public int getItemCount() {
        return detailedDailyModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price, description, rating, timing;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detailed_img);
            name = itemView.findViewById(R.id.detailed_name);
            price = itemView.findViewById(R.id.detailed_price);
            description = itemView.findViewById(R.id.detailed_des);
            rating = itemView.findViewById(R.id.detailed_rating);
            timing = itemView.findViewById(R.id.detailed_timing);
        }
    }
}
