package com.nita.penamob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.activity.Dashboard;
import com.nita.penamob.activity.Picker;
import com.nita.penamob.adapter.LearningPathAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LessonsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Report extends Fragment implements View.OnClickListener {
    private Dashboard parent;
    private RecyclerView list;
    private LinearLayout emptyState;
    private TextView messageEmptyState;
    private RelativeLayout contentCourses;
    private TextView courses;
    private final int GET_COURSES = 0;
    private String coursesId = "";
    private LearningPathAdapter adapter;
    private List<LearningPathModel> lists = new ArrayList<>();

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
        list = rootView.findViewById(R.id.list_menu);
        emptyState = rootView.findViewById(R.id.empty_state);
        messageEmptyState = rootView.findViewById(R.id.message);
    }

    private void init() {
        contentCourses.setOnClickListener(this::onClick);
        messageEmptyState.setText("Silahkan pilih kelas terlebih dahulu.");
    }

    private void fetchData() {
        lists.clear();

        try {
            parent.clientApiService.apiService(parent.clientApiService.report + coursesId, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONArray data = new JSONArray(hashMap.get("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject detail = data.getJSONObject(i);
                                JSONArray lesson = detail.getJSONArray("child");

                                List<LessonsModel> listLessons = new ArrayList<>();

                                for (int x = 0; x < lesson.length(); x++) {
                                    JSONObject lessonDetail = lesson.getJSONObject(x);
                                    String expired = "";

                                    if (lessonDetail.getInt("is_expired") == 1) {
                                        expired += parent.functionHelper.convertDateString(lessonDetail.getString("start_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                                        expired += " s.d ";
                                        expired += parent.functionHelper.convertDateString(lessonDetail.getString("end_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                                    }

                                    LessonsModel dataLessons = new LessonsModel(lessonDetail.getString("id"), lessonDetail.getString("name"), lessonDetail.getString("type"), lessonDetail.getString("format"), expired, false);
                                    dataLessons.setInformation(lessonDetail.getString("score"));

                                    listLessons.add(dataLessons);
                                }

                                LearningPathModel item = new LearningPathModel(detail.getString("id"), detail.getString("section"), listLessons.size() > 0, listLessons, false, false);
                                item.setWithScore(true);
                                lists.add(item);
                            }
                            emptyState.setVisibility(View.GONE);
                        } else {
                            messageEmptyState.setText("Silahkan pilih kelas terlebih dahulu.");
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getApplicationContext(), RecyclerView.VERTICAL, false);
                        adapter = new LearningPathAdapter(parent.getApplicationContext(), lists, R.layout.item_topic, parent.functionHelper, new LearningPathAdapter.OnClickListener() {
                            @Override
                            public void onClickListener(int position, View view) {
                                LearningPathModel detail = lists.get(position);
                                if (view.getId() == R.id.contentTopic) {
                                    lists.get(position).setOpened(!detail.isOpened());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                        list.setLayoutManager(layoutManager);
                        list.setAdapter(adapter);
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
                                courses.setVisibility(View.VISIBLE);
                                coursesId = data.getString("id");
                                fetchData();
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
                intent.putExtra("id", coursesId);
                intent.putExtra("url", parent.clientApiService.courses);
                intent.putExtra("CODE_RESULT", GET_COURSES);
                activityResultLaunch.launch(intent);
                break;
        }
    }
}