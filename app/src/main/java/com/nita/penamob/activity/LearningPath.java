package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.adapter.LearningPathAdapter;
import com.nita.penamob.adapter.LearningPathAdapter;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LearningPathModel;
import com.nita.penamob.model.LessonsModel;

import java.util.ArrayList;
import java.util.List;

public class LearningPath extends BaseController implements View.OnClickListener {
    private RecyclerView list;
    private LearningPathAdapter adapter;
    private List<LearningPathModel> lists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_path);

        findView();
        fetchData();
        init();
    }

    private void init() {
        title.setText("Daftar Pembelajaran");
        back.setOnClickListener(this::onClick);
    }

    private void findView() {
        list = findViewById(R.id.list);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
    }

    private void fetchData() {
        String topic[] = {
                "Topik 1",
                "Topik 2",
                "Topik 3"
        };

        String lessons[] = {
                "Pembelajaran Pertama",
                "Pembelajaran Kedua",
                "Tugas",
                "Kuis"
        };

        String typeLessons[] = {
                "Materi",
                "Materi",
                "Tugas",
                "Kuis"
        };

        String formatLessons[] = {
                "Text",
                "Text",
                "File",
                "Text"
        };

        String expired[] = {
                "Aug 11, 2022 s.d Aug 24, 2022",
                "",
                "",
                ""
        };

        lists.clear();

        for (int i = 0; i < topic.length; i++) {
            List<LessonsModel> listLessons = new ArrayList<>();
            if (i == 0) {
                for (int x = 0; x < lessons.length; x++) {
                    listLessons.add(new LessonsModel(String.valueOf(x), lessons[x], typeLessons[x], formatLessons[x], expired[x]));
                }
            }
            LearningPathModel item = new LearningPathModel(String.valueOf(i), topic[i], listLessons.size() > 0, listLessons, i > 0, false);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}