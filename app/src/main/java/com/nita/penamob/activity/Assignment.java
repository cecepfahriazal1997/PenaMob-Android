package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nita.penamob.R;

import org.apache.commons.lang3.StringUtils;

import mehdi.sakout.fancybuttons.FancyButton;

public class Assignment extends BaseController implements View.OnClickListener {
    private FancyButton see, chooseFile, submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment);

        findView();
        init();
    }

    private void findView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        see = findViewById(R.id.see);
        chooseFile = findViewById(R.id.choose_file);
        submit = findViewById(R.id.submit);
    }

    private void init() {
        back.setOnClickListener(this::onClick);
        submit.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                helper.showToast("Tugas berhasil dikirim!", 0);
                finish();
                break;
        }
    }
}