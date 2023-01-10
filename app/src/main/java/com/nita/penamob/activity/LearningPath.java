package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.adapter.LearningPathAdapter;
import com.nita.penamob.adapter.LearningPathAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LessonsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LearningPath extends BaseController implements View.OnClickListener {
    private RecyclerView list;
    private LearningPathAdapter adapter;
    private List<LearningPathModel> lists = new ArrayList<>();
    private TextView courses;

    private String coursesId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_path);

        findView();
        init();
        fetchDetailClass();
        fetchData();
    }

    private void init() {
        title.setText("Daftar Pembelajaran");
        back.setOnClickListener(this::onClick);

        coursesId = getIntent().getStringExtra("id");
    }

    private void findView() {
        list = findViewById(R.id.list);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        courses = findViewById(R.id.courses);
    }

    private void fetchData() {
        lists.clear();

        try {
            service.apiService(service.learningPath + coursesId, null, null, true, new Service.hashMapListener() {
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
                                        expired += helper.convertDateString(lessonDetail.getString("start_date"), "YYYY-MM-DD", "MMM DD, YYYY");
                                        expired += " s.d ";
                                        expired += helper.convertDateString(lessonDetail.getString("end_date"), "YYYY-MM-DD", "MMM DD, YYYY");
                                    }

                                    listLessons.add(new LessonsModel(lessonDetail.getString("id"), lessonDetail.getString("name"), lessonDetail.getString("type"), lessonDetail.getString("format"), expired, lessonDetail.getInt("locked") == 1));
                                }

                                LearningPathModel item = new LearningPathModel(detail.getString("id"), detail.getString("section"), listLessons.size() > 0, listLessons, detail.getInt("locked") == 1, false);
                                lists.add(item);
                            }

                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            adapter = new LearningPathAdapter(getApplicationContext(), lists, R.layout.item_topic, helper, new LearningPathAdapter.OnClickListener() {
                                @Override
                                public void onClickListener(int position, View view) {
                                    LearningPathModel detail = lists.get(position);
                                    if (!detail.isLocked()) {
                                        if (view.getId() == R.id.contentTopic){
                                            lists.get(position).setOpened(!detail.isOpened());
                                            adapter.notifyDataSetChanged();
                                        }
                                    } else {
                                        helper.showToast("Oops! topik masih terkunci, silahkan buka topik sebelumnya", 0);
                                    }
                                }
                            });

                            list.setLayoutManager(layoutManager);
                            list.setAdapter(adapter);
                        }
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

    private void fetchDetailClass() {
        try {
            service.apiService(service.coursesDetail + coursesId, null, null, false, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));

                            courses.setText(data.getString("name"));
                        }
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
            case R.id.back:
                finish();
                break;
        }
    }
}