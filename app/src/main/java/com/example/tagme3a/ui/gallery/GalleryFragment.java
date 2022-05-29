package com.example.tagme3a.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tagme3a.AdapterBuild;
import com.example.tagme3a.LoginActivity;
import com.example.tagme3a.MainUserActivity;
import com.example.tagme3a.R;
import com.example.tagme3a.build;
import com.example.tagme3a.buildItemActivity;
import com.example.tagme3a.getBuild;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private GalleryViewModel galleryViewModel;

    ListView lvBuilds;
    build model;
    getBuild gg = new getBuild();
    AdapterBuild AdapterBuild;
    ArrayList<build> dd;
    public static String buildID;
    //public static String ID;


    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        lvBuilds = root.findViewById(R.id.lvBuilds);
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.table_row_appear), 0.5f); //0.5f == time between appearance of listview items.
        lvBuilds.setLayoutAnimation(lac);
        lvBuilds.startLayoutAnimation();

        /*if(!LoginActivity.ID.isEmpty())
            ID = LoginActivity.ID;
        else
            ID = buildItemActivity.ID;*/

        dd = new ArrayList<>(gg.getData(LoginActivity.ID));
        AdapterBuild = new AdapterBuild(getActivity(),dd);
        lvBuilds.setAdapter(AdapterBuild);
        lvBuilds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                model=dd.get(i);
                buildID=model.getBuildID();
                startActivity(new Intent(getActivity(), buildItemActivity.class));

            }
        });
        SwipeRefreshLayout swp2 = root.findViewById(R.id.swp2);
        swp2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lvBuilds.startLayoutAnimation();
                dd = new ArrayList<>(gg.getData(LoginActivity.ID));
                AdapterBuild = new AdapterBuild(getActivity(),dd);
                lvBuilds.setAdapter(AdapterBuild);
                swp2.setRefreshing(false);
            }
        });

        return root;
    }
}