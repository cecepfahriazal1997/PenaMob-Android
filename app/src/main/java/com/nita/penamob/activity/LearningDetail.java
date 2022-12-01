package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nita.penamob.R;

import org.apache.commons.lang3.StringUtils;

public class LearningDetail extends BaseController implements View.OnClickListener {
    private String type = "";
    private RelativeLayout buttonTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_detail);

        findView();
        init();
    }

    private void findView() {
        buttonTask = findViewById(R.id.button_task);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
    }

    private void init() {
        type = getIntent().getStringExtra("type");

        if (type.equals("tugas")) {
            buttonTask.setVisibility(View.VISIBLE);
        }

        title.setText(StringUtils.capitalize(type));
        back.setOnClickListener(this::onClick);
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