package com.nita.penamob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class Assignment extends BaseController implements View.OnClickListener {
    private FancyButton see, chooseFile, submit;
    private EditText description;
    private String id, urlAttachement = "";
    private List<Part> file = new ArrayList<>();
    private boolean fileChoosed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment);

        findView();
        init();
        fetchData();
    }

    private void findView() {
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        see = findViewById(R.id.see);
        description = findViewById(R.id.description);
        chooseFile = findViewById(R.id.choose_file);
        submit = findViewById(R.id.submit);
    }

    private void init() {
        id = getIntent().getStringExtra("id");
        title.setText(getIntent().getStringExtra("title"));
        see.setOnClickListener(this::onClick);
        back.setOnClickListener(this::onClick);
        submit.setOnClickListener(this::onClick);
        chooseFile.setOnClickListener(this::onClick);
    }

    private void fetchData() {
        try {
            service.apiService(service.lessonDetail + id, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject detail = new JSONObject(hashMap.get("data"));
                            if (!detail.isNull("assignment") && detail.getJSONObject("assignment") != null) {
                                JSONObject dataAssignment = detail.getJSONObject("assignment");

                                description.setText(dataAssignment.getString("description"));

                                if (!dataAssignment.getString("attachment").isEmpty() && dataAssignment.getString("attachment") != "null") {
                                    see.setVisibility(View.VISIBLE);

                                    urlAttachement = dataAssignment.getString("attachment");
                                } if (!dataAssignment.getString("score").isEmpty() && dataAssignment.getString("score") != "null") {
                                    submit.setVisibility(View.GONE);
                                    chooseFile.setVisibility(View.GONE);
                                }
                            } else {
                                see.setVisibility(View.GONE);
                                chooseFile.setVisibility(View.VISIBLE);
                            }
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

    private void save() {
        try {
            param.clear();
            param.put("lessons_id", id);
            param.put("description", description.getText().toString());
            service.apiService(service.saveAssignment, param, file, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
//                        JSONObject result = new JSONObject(hashMap.get("data"));
                        helper.showToast(hashMap.get("message"), 0);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    private void resetFile() {
        chooseFile.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        chooseFile.setFocusBackgroundColor(ContextCompat.getColor(this, R.color.primaryDark));
        chooseFile.setText("Pilih File");
        file = new ArrayList<>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image tempImage = ImagePicker.getFirstImageOrNull(data);
            if (tempImage != null) {
                file.clear();
                file.add(new FilePart("attachment", new File(tempImage.getPath())));
                chooseFile.setBackgroundColor(ContextCompat.getColor(this, R.color.grayLevel5));
                chooseFile.setFocusBackgroundColor(ContextCompat.getColor(this, R.color.grayLevel7));
                chooseFile.setText("Hapus File");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                save();
                break;
            case R.id.see:
                helper.openUrlToBrowser(urlAttachement);
                break;
            case R.id.choose_file:
                if (!fileChoosed) {
                    helper.openFileChooser(this, 1, permissionImage);
                    fileChoosed = true;
                } else {
                    resetFile();
                }
                break;
        }
    }
}