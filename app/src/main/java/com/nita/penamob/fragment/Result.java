package com.nita.penamob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.adapter.GridMenuAdapter;
import com.nita.penamob.model.GridMenuModel;

import java.util.ArrayList;
import java.util.List;

public class Result extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private GridMenuAdapter adapter;
    private List<GridMenuModel> lists = new ArrayList<>();

    public Result() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((Dashboard) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        findView(rootView);

        return rootView;
    }

    private void findView(View rootView) {
        listMenu = rootView.findViewById(R.id.list_menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}