package com.nita.penamob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.json.JSONObject;

import java.util.Map;

public class QuizOverview extends BaseController implements View.OnClickListener {
    private ImageButton back;
    private TextView title, alertTitle, lesson, description, expired, note, duration,
                    totalCorrect, totalWrong, score;
    private LinearLayout alert, contentLesson, contentScore, contentExpire, contentNote, contentDuration;
    private RelativeLayout btnStartQuiz, btnReference;
    private ImageView alertIcon;
    private String urlReference;
    private boolean isPause = false;

    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_overview);

        findById();
        init();
        fetchData();
    }

    private void findById() {
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
        alert = findViewById(R.id.alert);
        contentLesson = findViewById(R.id.content_lesson);
        contentScore = findViewById(R.id.content_score);
        alertIcon = findViewById(R.id.alert_icon);
        alertTitle = findViewById(R.id.alert_title);
        lesson = findViewById(R.id.lesson);
        description = findViewById(R.id.description);
        expired = findViewById(R.id.expired);
        note = findViewById(R.id.note);
        duration = findViewById(R.id.duration);
        contentExpire = findViewById(R.id.content_expired);
        contentNote = findViewById(R.id.content_note);
        contentDuration = findViewById(R.id.content_duration);
        btnReference = findViewById(R.id.btn_reference);
        totalCorrect = findViewById(R.id.total_correct);
        totalWrong = findViewById(R.id.total_wrong);
        score = findViewById(R.id.score);
    }

    private void init() {
        title.setText("Kuis Overview");

        id = getIntent().getStringExtra("id");

        back.setOnClickListener(this::onClick);
        btnStartQuiz.setOnClickListener(this::onClick);
        btnReference.setOnClickListener(this::onClick);
    }

    private void fetchData() {
        try {
            service.apiService(service.quizOverview + id, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));

                            // get detail information of lesson
                            JSONObject dataLesson = data.getJSONObject("detail");

                            urlReference = dataLesson.getString("references");

                            lesson.setText(dataLesson.getString("name"));
                            helper.setTextHtml(description, dataLesson.getString("description"));

                            if (dataLesson.getInt("is_expired") == 0) {
                                contentExpire.setVisibility(View.GONE);
                            } if (dataLesson.getString("note").isEmpty()) {
                                contentNote.setVisibility(View.GONE);
                            } if (dataLesson.getString("references").isEmpty()) {
                                btnReference.setVisibility(View.GONE);
                            } if (dataLesson.getString("duration").isEmpty()) {
                                contentDuration.setVisibility(View.GONE);
                            }

                            String expiredDate = "";

                            if (dataLesson.getInt("is_expired") == 1) {
                                expiredDate += helper.convertDateString(dataLesson.getString("start_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                                expiredDate += " s.d ";
                                expiredDate += helper.convertDateString(dataLesson.getString("end_date"), "yyyy-MM-dd", "MMM dd, yyyy");
                            }

                            expired.setText(expiredDate);
                            note.setText(dataLesson.getString("note"));
                            duration.setText(dataLesson.getString("duration"));

                            // if user has finished quiz
                            if (data.getBoolean("is_done")) {
                                // get all score
                                JSONObject tmpScore = data.getJSONObject("score");
                                totalCorrect.setText(tmpScore.getString("correct"));
                                totalWrong.setText(tmpScore.getString("wrong"));
                                score.setText(tmpScore.getString("score"));

                                alert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.badge_success));
                                alertIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.check));
                                alertTitle.setText("Kamu telah menyelesaikan kuis ini");
                                contentScore.setVisibility(View.VISIBLE);
                                contentLesson.setVisibility(View.VISIBLE);
                                alert.setVisibility(View.VISIBLE);
                                btnStartQuiz.setVisibility(View.GONE);
                            } else { // if user unfinish quiz
                                if (!data.getBoolean("is_started")) {
                                    alert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.badge_warning));
                                    alertIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.warning));
                                    alertTitle.setText("Kamu belum mengerjakan kuis ini");
                                    alert.setVisibility(View.GONE);
                                    contentScore.setVisibility(View.GONE);
                                    contentLesson.setVisibility(View.GONE);
                                } else {
                                    if (!data.getBoolean("is_done"))
                                        goToQuiz();
                                }
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


    private void goToQuiz() {
        param.clear();
        param.put("id", id);
        helper.startIntent(Quiz.class, false, param);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_quiz:
                goToQuiz();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_reference:
                helper.openUrlToBrowser(urlReference);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isPause) {
            isPause = false;
            fetchData();
        }
    }
}