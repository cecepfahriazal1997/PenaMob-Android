package com.nita.penamob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.adapter.CoursesAdapter;
import com.nita.penamob.model.CoursesModel;

import java.util.ArrayList;
import java.util.List;

public class Courses extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private CoursesAdapter adapter;
    private List<CoursesModel> lists = new ArrayList<>();
    private EditText keyword;

    public Courses() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = ((Dashboard) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_courses, container, false);

        findView(rootView);
        this.fetchData();

        return rootView;
    }

    private void findView(View rootView) {
        listMenu = rootView.findViewById(R.id.list_menu);
        keyword = rootView.findViewById(R.id.keyword);
    }

    private void fetchData() {
        String title[] = {
                "Kelas D1 Kebidanan",
                "Kelas D2 Kebidanan",
                "Kelas S1 Keperawatan",
                "Kelas S2 Keperawatan"
        };

        String category[] = {
                "Matematika",
                "Bahasa Indonesia",
                "Bahasa Inggris",
                "Fisika"
        };

        String teacher[] = {
                "Ahmad Subarjo",
                "Bambang Sudiono",
                "Cintya Maharani",
                "Deny Sumar"
        };

        lists.clear();

        for (int i = 0; i < title.length; i++) {
            String totParticipant = String.valueOf((int) (Math.random() * 10 + i));
            CoursesModel item = new CoursesModel(String.valueOf(i), category[i], title[i], null, totParticipant, teacher[i]);
            lists.add(item);
        }

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        adapter = new CoursesAdapter(parent.getApplicationContext(), lists, R.layout.item_courses, new CoursesAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
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