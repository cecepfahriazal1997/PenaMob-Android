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
import com.nita.penamob.activity.LearningPath;
import com.nita.penamob.adapter.CoursesAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.CoursesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.reflect.KParameter;

public class Courses extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private CoursesAdapter adapter;
    private List<CoursesModel> lists = new ArrayList<>();
    private EditText keyword;
    protected Map<String, String> param = new HashMap<>();

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
        try {
            lists.clear();
            parent.clientApiService.apiService(parent.clientApiService.courses, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONArray data = new JSONArray(hashMap.get("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject detail = data.getJSONObject(i);

                                CoursesModel item = new CoursesModel(detail.getString("id"),
                                        detail.getString("category"),
                                        detail.getString("name"),
                                        detail.getString("cover"),
                                        detail.getString("participant"),
                                        detail.getString("teacher_name"));
                                lists.add(item);
                            }
                        }

                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        adapter = new CoursesAdapter(parent.getApplicationContext(), lists, R.layout.item_courses, new CoursesAdapter.OnClickListener() {
                            @Override
                            public void onClickListener(int position) {
                                param.clear();
                                param.put("id", lists.get(position).getId());
                                parent.functionHelper.startIntent(LearningPath.class, false, param);
                            }
                        });

                        listMenu.setLayoutManager(layoutManager);
                        listMenu.setAdapter(adapter);
                    } catch (Exception er) {
                        er.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);

        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_) {
                this.fetchData();
            }
        }
    }
}