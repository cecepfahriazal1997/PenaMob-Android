package com.nita.penamob.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;
import com.nita.penamob.helper.FileDownloader;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class LearningDetail extends BaseController implements View.OnClickListener {
    private String type = "";
    private String id = "";
    private String titleLesson = "";
    private TextView titleLessons, expired, note, descriptionText, score;
    private WebView description;
    private LinearLayout contentExpired, contentNote, notification;
    private RelativeLayout reference, btnAssignment, contentScore;
    private String urlReference;
    private boolean onPause = false;
    private FancyButton download;
    private static final int REQUEST_CODE = 100;
    private FileDownloader fileDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_detail);

        fileDownloader = new FileDownloader(this);

        findView();
        init();
        fetchData();
    }

    private void findView() {
        btnAssignment = findViewById(R.id.button_assignment);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        titleLessons = findViewById(R.id.lesson);
        description = findViewById(R.id.description);
        descriptionText = findViewById(R.id.description_text);
        expired = findViewById(R.id.expired);
        note = findViewById(R.id.note);
        contentExpired = findViewById(R.id.content_expired);
        contentNote = findViewById(R.id.content_note);
        reference = findViewById(R.id.reference);
        contentScore = findViewById(R.id.content_score);
        score = findViewById(R.id.score);
        notification = findViewById(R.id.content_notification);
        download = findViewById(R.id.download);
    }

    private void init() {
        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");

        if (type.equals("tugas")) {
            btnAssignment.setVisibility(View.VISIBLE);
            btnAssignment.setOnClickListener(this::onClick);
        }

        title.setText(StringUtils.capitalize(type));
        back.setOnClickListener(this::onClick);
        reference.setOnClickListener(this::onClick);
        btnAssignment.setOnClickListener(this::onClick);
    }

    private void fetchData() {
        try {
            service.apiService(service.lessonDetail + id, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject detail = new JSONObject(hashMap.get("data"));

                            titleLesson = detail.getString("name");
                            titleLessons.setText(detail.getString("name"));

                            String expiredDate = "";

                            if (detail.getInt("is_expired") == 1) {
                                expiredDate += helper.convertDateString(detail.getString("start_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                                expiredDate += " s.d ";
                                expiredDate += helper.convertDateString(detail.getString("end_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                            }

                            expired.setText(expiredDate);
                            note.setText(detail.getString("note"));

                            if (detail.getInt("is_expired") == 0) {
                                contentExpired.setVisibility(View.GONE);
                            } if (detail.getString("note").isEmpty()) {
                                contentNote.setVisibility(View.GONE);
                            } if (detail.getString("references").isEmpty()) {
                                reference.setVisibility(View.GONE);
                            }

                            if (detail.getString("format").equals("TEXT")) {
                                description.setVisibility(View.GONE);
                                descriptionText.setVisibility(View.VISIBLE);

                                helper.setTextHtml(descriptionText, detail.getString("description"));
                            } else {
                                description.setVisibility(View.VISIBLE);
                                descriptionText.setVisibility(View.GONE);

                                helper.formatIsText(pDialog, description, detail.getString("attachment"), "url");
                                if (!detail.getString("attachment").isEmpty()) {
                                    download.setVisibility(View.VISIBLE);
                                    download.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                        != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(LearningDetail.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                                                } else {
                                                    try {
                                                        fileDownloader.startDownload(detail.getString("attachment_file"));
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                }
                                            } else {
                                                try {
                                                    fileDownloader.startDownload(detail.getString("attachment_file"));
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }
                                    });
                                }
                            }

//                            IF TYPE LESSON IS ASSIGNMENT
                            if (type.equals("tugas")) {
                                if (detail.getJSONObject("assignment") != null) {
                                    JSONObject dataAssignment = detail.getJSONObject("assignment");

                                    notification.setVisibility(View.VISIBLE);
                                    if (!dataAssignment.getString("score").isEmpty() && dataAssignment.getString("score") != "null") {
                                        contentScore.setVisibility(View.VISIBLE);
                                        score.setText(dataAssignment.getString("score"));
                                        btnAssignment.setVisibility(View.GONE);
                                    }
                                }
                            }

                            urlReference = detail.getString("references");
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
            case R.id.button_assignment:
                param.clear();
                param.put("id", id);
                param.put("title", titleLesson);
                helper.startIntent(Assignment.class, false, param);
                break;
            case R.id.reference:
                helper.openUrlToBrowser(urlReference);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        this.onPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (this.onPause) {
            this.fetchData();
            this.onPause = false;
        }
    }
}