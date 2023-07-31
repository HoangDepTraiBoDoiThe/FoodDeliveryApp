package com.example.fooddeliveryapp.Adapters.Home;

import android.app.Activity;
import android.content.res.Resources;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

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
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int AdapterPosition = holder.getAdapterPosition();

        holder.imageView.setImageResource(list.get(AdapterPosition).getImage());
        holder.name.setText(list.get(AdapterPosition).getName());

        // Load data from JSON file
        try {
            JSONArray itemsArray = loadJSONFromRaw(activity.getResources(), R.raw.food_items, "pizza");
            if (itemsArray != null) {
                JSONObject itemObject = itemsArray.getJSONObject(AdapterPosition);
                int imageResId = getResourceIdByName(itemObject.getString("image"));
                String name = itemObject.getString("name");
                String timing = itemObject.getString("timing");
                String rating = itemObject.getString("rating");
                String price = itemObject.getString("price");
                // ... Use timing, rating, and price if needed
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (check) {
            ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
            // Load data for the first item from JSON
            try {
                JSONArray itemsArray = loadJSONFromRaw(activity.getResources(), R.raw.food_items, "pizza");
                if (itemsArray != null) {
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemObject = itemsArray.getJSONObject(i);
                        int imageResId = getResourceIdByName(itemObject.getString("image"));
                        String name = itemObject.getString("name");
                        String timing = itemObject.getString("timing");
                        String rating = itemObject.getString("rating");
                        String price = itemObject.getString("price");
                        homeVerModels.add(new HomeVerModel(imageResId, name, timing, rating, price));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            updateVerticalRec.CallBack(AdapterPosition, homeVerModels);

            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = AdapterPosition;
                notifyDataSetChanged();

                // Load data for the selected item from JSON
                try {
                    String itemName = list.get(AdapterPosition).getId();
                    JSONArray itemsArray = loadJSONFromRaw(activity.getResources(), R.raw.food_items, itemName.toLowerCase());
                    if (itemsArray != null) {
                        ArrayList<HomeVerModel> homeVerModels = new ArrayList<>();
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemObject = itemsArray.getJSONObject(i);

                            int imageResId = getResourceIdByName(itemObject.getString("image"));
                            String name = itemObject.getString("name");
                            String timing = itemObject.getString("timing");
                            String rating = itemObject.getString("rating");
                            String price = itemObject.getString("price");
                            homeVerModels.add(new HomeVerModel(imageResId, name, timing, rating, price));
                        }
                        updateVerticalRec.CallBack(AdapterPosition, homeVerModels);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
    public int getItemCount() {
        return list.size();
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

    private JSONArray GetItemArray(Resources resources, int resourceId) {
        try {

            InputStream inputStream = resources.openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream);
            StringBuilder jsonString = new StringBuilder();
            while (scanner.hasNext()) {
                jsonString.append(scanner.nextLine());
            }
            scanner.close();
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray itemArray = jsonObject.getJSONArray("food_items");
            return itemArray;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to load JSON data from raw resource
    private JSONArray loadJSONFromRaw(Resources resources, int resourceId, String name) {

        try {

            JSONArray itemArray = GetItemArray(resources, resourceId);
            JSONArray pizzaArray = null;
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject itemObject = itemArray.getJSONObject(i);
                pizzaArray = itemObject.getJSONArray(name);
            }
            return pizzaArray;

        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Helper method to get resource ID by name
    private int getResourceIdByName(String name) {
        return activity.getResources().getIdentifier(name, "drawable", activity.getPackageName());
    }
}
