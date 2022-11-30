package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.json.JSONObject;

import java.util.Map;

public class Theory extends BaseController implements View.OnClickListener {
    private WebView content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theory);

        findView();
        init();
        fetchData();
    }

    private void findView() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
    }

    private void init() {
        back.setOnClickListener(this::onClick);
    }

    private void fetchData() {
        try {
            service.apiService(service.theory, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            helper.formatIsText(pDialog, content, hashMap.get("data"), "text");
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