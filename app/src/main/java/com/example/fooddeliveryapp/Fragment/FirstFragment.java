package com.example.fooddeliveryapp.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fooddeliveryapp.Adapters.Featured.FeaturedAdapter;
import com.example.fooddeliveryapp.Adapters.Featured.FeaturedVerAdapter;
import com.example.fooddeliveryapp.Models.Featured.FeaturedModel;
import com.example.fooddeliveryapp.Models.Featured.FeaturedVerModel;
import com.example.fooddeliveryapp.R;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {


    ///////////// Featured Hor Rec
    List<FeaturedModel> featuredModelList;
    RecyclerView recyclerView;
    FeaturedAdapter featuredAdapter;

    ///////////// Featured ver Rec
    List<FeaturedVerModel> featuredVerModelList;
    RecyclerView recyclerView2;
    FeaturedVerAdapter featuredVerAdapter;

    public FirstFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ///////////// Featured Hor Rec
        recyclerView = view.findViewById(R.id.featured_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        featuredModelList = new ArrayList<>();

        featuredModelList.add(new FeaturedModel(R.drawable.fav1, "Featured 1", "Description 1"));
        featuredModelList.add(new FeaturedModel(R.drawable.fav2, "Featured 2", "Description 2"));
        featuredModelList.add(new FeaturedModel(R.drawable.fav3, "Featured 3", "Description 3"));

        featuredAdapter = new FeaturedAdapter(featuredModelList);
        recyclerView.setAdapter(featuredAdapter);

        ///////////// Featured Ver Rec
        recyclerView2 = view.findViewById(R.id.featured_ver_rec);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        featuredVerModelList = new ArrayList<>();

        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver1, "Featured 1", "Description 1", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver2, "Featured 2", "Description 2", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver3, "Featured 3", "Description 3", "5.0", "10:00am - 80:00pm"));

        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver1, "Featured 1", "Description 1", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver2, "Featured 2", "Description 2", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver3, "Featured 3", "Description 3", "5.0", "10:00am - 80:00pm"));

        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver1, "Featured 1", "Description 1", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver2, "Featured 2", "Description 2", "5.0", "10:00am - 80:00pm"));
        featuredVerModelList.add(new FeaturedVerModel(R.drawable.ver3, "Featured 3", "Description 3", "5.0", "10:00am - 80:00pm"));

        featuredVerAdapter = new FeaturedVerAdapter(featuredVerModelList);
        recyclerView2.setAdapter(featuredVerAdapter);

        return view;
    }
}