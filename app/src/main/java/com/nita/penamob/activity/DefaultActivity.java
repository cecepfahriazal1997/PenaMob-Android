package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;

import com.nita.penamob.R;

public class DefaultActivity extends BaseController implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.walkthrough);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}