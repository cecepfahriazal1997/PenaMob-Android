package com.nita.penamob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.activity.Picker;
import com.nita.penamob.adapter.GridMenuAdapter;
import com.nita.penamob.model.GridMenuModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Report extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView listMenu;
    private RelativeLayout contentCourses;
    private TextView courses;
    private GridMenuAdapter adapter;
    private List<GridMenuModel> lists = new ArrayList<>();
    private final int GET_COURSES = 0;
    private String tmpCoursesId = "";

    public Report() {
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
        init();

        return rootView;
    }

    private void findView(View rootView) {
        contentCourses = rootView.findViewById(R.id.content_courses);
        courses = rootView.findViewById(R.id.courses);
        listMenu = rootView.findViewById(R.id.list_menu);
    }

    private void init() {
        contentCourses.setOnClickListener(this::onClick);
    }

    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    switch (result.getResultCode()) {
                        case GET_COURSES:
                            try {
                                JSONObject data = new JSONObject(result.getData().getStringExtra("result"));
                                courses.setText(data.getString("title"));
                                tmpCoursesId = data.getString("id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_courses:
                Intent intent = new Intent(getActivity(), Picker.class);
                intent.putExtra("title", "Pilih Kelas");
                intent.putExtra("url", parent.clientApiService.courses);
                intent.putExtra("CODE_RESULT", GET_COURSES);
                activityResultLaunch.launch(intent);
                break;
        }
    }
}