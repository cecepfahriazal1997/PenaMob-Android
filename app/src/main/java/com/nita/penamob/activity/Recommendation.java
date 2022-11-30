package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nita.penamob.R;
import com.nita.penamob.adapter.RecommendationAdapter;
import com.nita.penamob.model.RecommendationModel;

import java.util.ArrayList;
import java.util.List;

public class Recommendation extends BaseController implements View.OnClickListener {
    private RecyclerView list;
    private RecommendationAdapter adapter;
    private List<RecommendationModel> lists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recap);

        findView();
        setQuestion();
    }

    private void findView() {
        list = findViewById(R.id.list);
    }

    private void setQuestion() {
        String title[] = {
                "Kriteria Relevansi",
                "Kriteria Efektifitas",
                "Kriteria Efisiensi",
                "Kriteria Berkelanjutan",
                "Kriteria Berdampak"
        };

        lists.clear();

        for (int i = 0; i < title.length; i++) {
            RecommendationModel item = new RecommendationModel();

            item.setTitle(title[i]);
            item.setPassGrade("168 - 210");
            item.setFailedGrade("167");
            item.setFormula("Nilai = Bobot x Skor = maksimum 42% x 5 = 210; 42% x 4 = 168; 42% x 2 =84; 42% x 1 =42");

            lists.add(item);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new RecommendationAdapter(getApplicationContext(), lists, R.layout.item_recommendation, new RecommendationAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {

            }
        });

        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(adapter);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}