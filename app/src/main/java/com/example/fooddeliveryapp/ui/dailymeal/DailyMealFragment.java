package com.example.fooddeliveryapp.ui.dailymeal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.DailyMealAdapter;
import com.example.fooddeliveryapp.Models.DailyMealModel;
import com.example.fooddeliveryapp.R;
import com.example.fooddeliveryapp.databinding.DailyMealFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {

    private DailyMealFragmentBinding binding;
    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModels;
    DailyMealAdapter dailyMealAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DailyMealFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        dailyMealModels = new ArrayList<>();
        dailyMealModels.add(new DailyMealModel(R.drawable.breakfast, "Breakfast", "30% off", "Description"));
        dailyMealModels.add(new DailyMealModel(R.drawable.lunch, "Lunch", "30% off", "Description"));
        dailyMealModels.add(new DailyMealModel(R.drawable.dinner, "Dinner", "30% off", "Description"));
        dailyMealModels.add(new DailyMealModel(R.drawable.sweets, "Sweets", "30% off", "Description"));
        dailyMealModels.add(new DailyMealModel(R.drawable.coffe, "Coffee", "30% off", "Description"));

        dailyMealAdapter = new DailyMealAdapter(getContext(), dailyMealModels);
        recyclerView.setAdapter(dailyMealAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyMealAdapter.notifyDataSetChanged();

        Log.d("DailyMealFragment", "RecyclerView set with " + dailyMealModels.size() + " items.");

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (binding != null) {
            binding = null;
        }
    }
}