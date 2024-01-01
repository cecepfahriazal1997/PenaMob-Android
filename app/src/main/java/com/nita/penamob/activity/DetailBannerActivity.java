package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.nita.penamob.R;

public class DetailBannerActivity extends BaseController implements View.OnClickListener {
    private WebView frame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_banner);

        init();
    }

    private void init() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        frame = findViewById(R.id.frame);

        back.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.clear));
        title.setText(getIntent().getStringExtra("title"));

        frame.setInitialScale(-1);
        helper.formatIsText(pDialog, frame, getIntent().getStringExtra("url"), "url");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: finish(); break;
        }
    }
}