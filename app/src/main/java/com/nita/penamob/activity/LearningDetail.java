package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Map;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learning_detail);

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
                                expiredDate += helper.convertDateString(detail.getString("start_date"), "YYYY-MM-DD", "MMM DD, YYYY");
                                expiredDate += " s.d ";
                                expiredDate += helper.convertDateString(detail.getString("end_date"), "YYYY-MM-DD", "MMM DD, YYYY");
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