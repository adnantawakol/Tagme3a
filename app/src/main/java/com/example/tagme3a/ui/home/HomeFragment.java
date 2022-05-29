package com.example.tagme3a.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tagme3a.AdapterCategory;
import com.example.tagme3a.ItemActivity;
import com.example.tagme3a.LoginActivity;
import com.example.tagme3a.R;
import com.example.tagme3a.category;
import com.example.tagme3a.getCategory;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    GridView gv;
    category model;
    getCategory gg = new getCategory();
    AdapterCategory adapterCategory;
    ArrayList<category> dd;
    public static String dno;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        gv = root.findViewById(R.id.gv);
        dd = new ArrayList<>(gg.getData());
        adapterCategory = new AdapterCategory(getActivity(),dd);
        gv.setAdapter(adapterCategory);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.fadein), 0.5f); //0.5f == time between appearance of listview items.
        gv.setLayoutAnimation(lac);
        gv.startLayoutAnimation();
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model=dd.get(i);
                dno=model.getCatID();
                startActivity(new Intent(getActivity(), ItemActivity.class));
            }
        });
        SwipeRefreshLayout swp = root.findViewById(R.id.swp);
        swp.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gv.startLayoutAnimation();
                dd = new ArrayList<>(gg.getData());
                adapterCategory = new AdapterCategory(getActivity(),dd);
                gv.setAdapter(adapterCategory);
                swp.setRefreshing(false);
            }
        });

        return root;
    }

}