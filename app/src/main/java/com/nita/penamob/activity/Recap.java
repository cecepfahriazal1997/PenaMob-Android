package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.adapter.RecapAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.RecapModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recap extends BaseController implements View.OnClickListener {
    private RecyclerView list;
    private RecapAdapter adapter;
    private List<RecapModel> lists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap);

        findView();
        setQuestion();
        init();
    }

    private void findView() {
        list = findViewById(R.id.list);
        back = findViewById(R.id.back);
    }

    private void init() {
        back.setOnClickListener(this::onClick);
    }

    private void setQuestion() {
        lists.clear();

        try {
            service.apiService(service.quizResult, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));
                            JSONArray listScore = data.getJSONArray("list_score");

                            for (int i = 0; i < listScore.length(); i++) {
                                JSONObject detail = listScore.getJSONObject(i);
                                RecapModel item = new RecapModel();

                                item.setTitle(detail.getString("title"));
                                item.setScore(detail.getString("score"));
                                item.setPassGrade(data.getString("passing_grade"));
                                item.setStatus(detail.getString("status"));

                                lists.add(item);
                            }
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                        adapter = new RecapAdapter(getApplicationContext(), lists, R.layout.item_recap, new RecapAdapter.OnClickListener() {
                            @Override
                            public void onClickListener(int position) {

                            }
                        });

                        list.setLayoutManager(linearLayoutManager);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}