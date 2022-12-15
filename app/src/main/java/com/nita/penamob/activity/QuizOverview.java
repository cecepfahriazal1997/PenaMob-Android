package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nita.penamob.R;

public class QuizOverview extends BaseController implements View.OnClickListener {
    private ImageButton back;
    private TextView title;
    private RelativeLayout btnOverviewQuiz;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_overview);

        findById();
        init();
    }

    private void findById() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        btnOverviewQuiz = findViewById(R.id.btn_overview_quiz);
    }

    private void init() {
        title.setText("Kuis Overview");

        back.setOnClickListener(this::onClick);
        btnOverviewQuiz.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_overview_quiz:
                helper.startIntent(Quiz.class, false, null);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}