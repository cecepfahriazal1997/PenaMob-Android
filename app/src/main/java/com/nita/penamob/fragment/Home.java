package com.nita.penamob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.adapter.GridMenuAdapter;
import com.nita.penamob.model.GridMenuModel;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private GridMenuAdapter adapter;
    private List<GridMenuModel> lists = new ArrayList<>();

    public Home() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((Dashboard) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        findView(rootView);
        this.setMenu();

        return rootView;
    }

    private void findView(View rootView) {
        listMenu = rootView.findViewById(R.id.list_menu);
    }

    private void setMenu() {
        String title[] = {
                "Total Kuis",
                "Total Tugas",
                "Total Materi",
                "Total Kelas"
        };

        lists.clear();

        for (int i = 0; i < title.length; i++) {
            GridMenuModel item = new GridMenuModel(title[i], String.valueOf((int) (Math.random() * 10 + i)));
            lists.add(item);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(parent.getApplicationContext(), 2);
        adapter = new GridMenuAdapter(parent.getApplicationContext(), lists, R.layout.grid_menu, new GridMenuAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                parent.selectPage(position + 1);
            }
        });

        listMenu.setLayoutManager(layoutManager);
        listMenu.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}