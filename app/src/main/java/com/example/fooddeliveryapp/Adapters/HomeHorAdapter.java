package com.example.fooddeliveryapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Models.HomeHorModel;
import com.example.fooddeliveryapp.Models.HomeVerModel;
import com.example.fooddeliveryapp.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<HomeHorModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<HomeHorModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        int AdapterPosition = holder.getAdapterPosition();
        holder.imageView.setImageResource(list.get(AdapterPosition).getImage());
        holder.name.setText(list.get(AdapterPosition).getName());
        if (check) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "10:00 - 23:00", "5.0", "Min - 34$"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "10:00 - 23:00", "5.0", "Min - 34$"));
            homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "10:00 - 23:00", "5.0", "Min - 34$"));

            updateVerticalRec.CallBack(AdapterPosition, homeVerModels);

            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = AdapterPosition;
                notifyDataSetChanged();

                if (AdapterPosition == 0) {

                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza1, "Pizza 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza2, "Pizza 2", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza3, "Pizza 3", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.pizza4, "Pizza 4", "10:00 - 23:00", "5.0", "Min - 34$"));

                    updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                } else if (AdapterPosition == 1) {
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.burger1, "Burger 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger2, "Burger 2", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.burger4, "Burger 4", "10:00 - 23:00", "5.0", "Min - 34$"));
                    updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                } else if (AdapterPosition == 2) {
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.fries1, "Fries 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries2, "Fries 2", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries3, "Fries 3", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.fries4, "Fries 4", "10:00 - 23:00", "5.0", "Min - 34$"));
                    updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                } else if (AdapterPosition == 3) {
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream1, "Ice cream 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream2, "Ice cream 2", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream3, "Ice cream 3", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.icecream4, "Ice cream 4", "10:00 - 23:00", "5.0", "Min - 34$"));
                    updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                } else if (AdapterPosition == 4) {
                    ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich1, "Sandwich 1 ", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich2, "Sandwich 2", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich3, "Sandwich 3", "10:00 - 23:00", "5.0", "Min - 34$"));
                    homeVerModels.add(new HomeVerModel(R.drawable.sandwich4, "Sandwich 4", "10:00 - 23:00", "5.0", "Min - 34$"));
                    updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                }
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
    public int getItemCount() { return list.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        CardView cardView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
