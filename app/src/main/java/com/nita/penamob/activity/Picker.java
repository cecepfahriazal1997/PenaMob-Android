package com.nita.penamob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nita.penamob.R;
import com.nita.penamob.adapter.PickerAdapter;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.PickerModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Picker extends BaseController implements View.OnClickListener {
    private RecyclerView list;
    private PickerAdapter adapter;
    private List<PickerModel> pickerList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);

        findView();
        init();
        fetchData();
    }

    private void findView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        list = findViewById(R.id.list);
    }

    private void init() {
        title.setText(getIntent().getStringExtra("title"));
        back.setOnClickListener(this::onClick);
    }

    private void fetchData() {
        try {
            String url = getIntent().getStringExtra("url");
            String selectedId = "";
            if (getIntent().hasExtra("id"))
                selectedId = getIntent().getStringExtra("id");

            String finalSelectedId = selectedId;
            service.apiService(url, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        pickerList.clear();
                        if (hashMap.get("status").equals("true")) {
                            JSONArray data = new JSONArray(hashMap.get("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject detail = data.getJSONObject(i);

                                PickerModel tmpData = new PickerModel(
                                        detail.getString("id"),
                                        detail.getString("name"),
                                        detail.getString("id").equals(finalSelectedId)
                                );

                                pickerList.add(tmpData);
                            }
                        }

                        setAdapter();
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

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        adapter = new PickerAdapter(getApplicationContext(), pickerList, R.layout.item_picker, new PickerAdapter.OnClickListener() {
            @Override
            public void onClickListener(int position) {
                Intent intent = new Intent();
                intent.putExtra("result", new Gson().toJson(pickerList.get(position)));
                setResult(getIntent().getIntExtra("CODE_RESULT", 0), intent);
                finish();
            }
        });

        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}