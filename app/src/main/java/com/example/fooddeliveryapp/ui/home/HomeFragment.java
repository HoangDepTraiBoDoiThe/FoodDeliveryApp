package com.example.fooddeliveryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment implements UpdateVerticalRec {

    private FragmentHomeBinding binding;

    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<HomeHorModel> homeHorModelList;
    HomeHorAdapter homeHorAdapter;


    //////////// Vertical
    ArrayList<HomeVerModel> homeVerModelList;
    HomeVerAdapter homeVerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

    ////// Horizontal
        homeHorModelList = new ArrayList<>();
        homeHorModelList.add(new HomeHorModel((R.drawable.pizza6), "Pizza"));
        homeHorModelList.add(new HomeHorModel((R.drawable.burger5), "Burger"));
        homeHorModelList.add(new HomeHorModel((R.drawable.fries5), "Fries"));
        homeHorModelList.add(new HomeHorModel((R.drawable.ice_cream5), "Ice cream"));
        homeHorModelList.add(new HomeHorModel((R.drawable.sandwich5), "Sandwich"));

        homeHorAdapter = new HomeHorAdapter(this, getActivity(), homeHorModelList);

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);

    ////// Vertical
        homeVerModelList = new ArrayList<>();

        homeVerAdapter = new HomeVerAdapter(getActivity(), homeVerModelList);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        return root;
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
}