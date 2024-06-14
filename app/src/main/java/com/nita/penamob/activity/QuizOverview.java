package com.nita.penamob.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuizOverview extends BaseController implements View.OnClickListener {
    private ImageButton back;
    private TextView title, alertTitle, lesson, description, expired, note, duration,
            totalCorrect, totalWrong, score, discussion, labelStartQuiz;
    private LinearLayout alert, contentLesson, contentScore, contentExpire, contentNote, contentDuration,
            contentDiscussion, listLesson;
    private RelativeLayout btnStartQuiz, btnReference;
    private TextView totalReadLessons, totalLessons;
    private ImageView alertIcon;
    private String urlReference;
    private boolean isPause = false;
    private boolean isRemedial = false;

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
        labelStartQuiz = findViewById(R.id.label_start_quiz);
        alert = findViewById(R.id.alert);
        contentLesson = findViewById(R.id.content_lesson);
        listLesson = findViewById(R.id.list_lesson);
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
        totalReadLessons = findViewById(R.id.read_lesson);
        totalLessons = findViewById(R.id.total_lesson);
        score = findViewById(R.id.score);
        contentDiscussion = findViewById(R.id.conten_discussion);
        discussion = findViewById(R.id.discussion);
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
                            }
                            if (dataLesson.getString("note").isEmpty()) {
                                contentNote.setVisibility(View.GONE);
                            }
                            if (dataLesson.getString("references").isEmpty()) {
                                btnReference.setVisibility(View.GONE);
                            }
                            if (dataLesson.getString("duration").isEmpty()) {
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

                                if (!dataLesson.getString("discussion").isEmpty()) {
                                    contentDiscussion.setVisibility(View.VISIBLE);
                                    helper.setTextHtml(discussion, dataLesson.getString("discussion"));
                                }

                                btnStartQuiz.setVisibility(View.GONE);
                                contentScore.setVisibility(View.VISIBLE);

                                listLesson.removeAllViews();
                                if (tmpScore.getString("have_passed").equals("false")) {
                                    contentLesson.setVisibility(View.VISIBLE);

                                    JSONArray relatedLesson = tmpScore.getJSONObject("related_lessons").getJSONArray("list");
                                    int totalRead = tmpScore.getJSONObject("related_lessons").getInt("total_read");
                                    if (totalRead == relatedLesson.length()) {
                                        labelStartQuiz.setText("Mulai Remedial Sekarang");
                                        btnStartQuiz.setVisibility(View.VISIBLE);

                                        isRemedial = true;
                                    }

                                    totalReadLessons.setText(String.valueOf(totalRead));
                                    totalLessons.setText("/" + relatedLesson.length());

                                    for (int x = 0; x < relatedLesson.length(); x++) {
                                        View layoutRelatedLesson = helper.inflateView(R.layout.item_lesson_remedial);

                                        ImageView icon = layoutRelatedLesson.findViewById(R.id.icon);
                                        TextView title = layoutRelatedLesson.findViewById(R.id.title);

                                        JSONObject detailRelatedLesson = relatedLesson.getJSONObject(x);

                                        title.setText(detailRelatedLesson.getString("name"));

                                        layoutRelatedLesson.setPadding(0, 10, 0, 10);

                                        layoutRelatedLesson.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try {
                                                    readLesson(data.getString("answer_id"), detailRelatedLesson.getString("id"));
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        });

                                        listLesson.addView(layoutRelatedLesson);
                                    }
                                    showAlert("warning", "Kamu belum lulus pada kuis ini\nsilahkan pelajari kembali materi dibawah ini", true);
                                } else {
                                    showAlert("success", hashMap.get("message"), true);
                                }
                            } else { // if user unfinish quiz
                                if (!data.getBoolean("is_started")) {
                                    showAlert("warning", hashMap.get("message"), true);
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

    private void readLesson(String quizId, String lessonId) {
        try {
            HashMap<String, String> post = new HashMap<>();
            post.put("quiz_id", quizId);
            post.put("lessons_id", lessonId);
            service.apiService(service.readLesson, post, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        HashMap<String, String> params;
                        params = new HashMap();
                        params.put("id", lessonId);
                        params.put("type", "materi");
                        helper.startIntent(LearningDetail.class, false, params);
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    private void showAlert(String type, String message, boolean isShow) {
        if (type.equals("success")) {
            alert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.badge_success));
            alertIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.check));
        } else {
            alert.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.badge_warning));
            alertIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.warning));
        }
        alertTitle.setText(message);

        alert.setVisibility(View.GONE);
        if (isShow)
            alert.setVisibility(View.VISIBLE);
    }

    private void goToQuiz() {
        param.clear();
        param.put("id", id);
        param.put("is_remedial", String.valueOf(isRemedial));

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