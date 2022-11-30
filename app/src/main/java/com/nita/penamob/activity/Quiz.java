package com.nita.penamob.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.nita.penamob.R;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.QuizModel;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class Quiz extends BaseController implements View.OnClickListener {
    private List<QuizModel> lists = new ArrayList<>();
    private TextView category, section, number, question, percentage, option1, option2, option3, option4, option5;
    private FancyButton numberQuestion, nextQuestion, prevQuestion;
    private LinearLayout loading, content;

    private Drawable badgeWhite, badgePrimary;
    private int black, white, currentNumber=0;
    private String currentAnswer="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        badgeWhite = ContextCompat.getDrawable(Quiz.this, R.drawable.badge_white);
        badgePrimary = ContextCompat.getDrawable(Quiz.this, R.drawable.badge_primary);
        black = ContextCompat.getColor(Quiz.this, R.color.black);
        white = ContextCompat.getColor(Quiz.this, R.color.white);

        findView();
        init();
        fetchQuestion();
    }

    private void init() {
        if (!getIntent().getStringExtra("category").isEmpty()) {
            switch (getIntent().getStringExtra("category")) {
                case "relevansi": title.setText("Relevansi"); break;
                case "efektifitas": title.setText("Efektifitas"); break;
                case "efisiensi": title.setText("Efisiensi"); break;
                case "keberlanjutan": title.setText("Keberlanjutan"); break;
                case "dampak": title.setText("Dampak"); break;
                default: title.setText(""); break;
            }
        }

        back.setOnClickListener(this::onClick);

        numberQuestion.setOnClickListener(this::onClick);
        option1.setOnClickListener(this::onClick);
        option2.setOnClickListener(this::onClick);
        option3.setOnClickListener(this::onClick);
        option4.setOnClickListener(this::onClick);
        option5.setOnClickListener(this::onClick);
        prevQuestion.setOnClickListener(this::onClick);
        nextQuestion.setOnClickListener(this::onClick);
    }

    private void findView() {
        loading = findViewById(R.id.loading);
        content = findViewById(R.id.content);
        category = findViewById(R.id.category);
        section = findViewById(R.id.section);
        number = findViewById(R.id.number);
        question = findViewById(R.id.question);
        percentage = findViewById(R.id.percentage);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        option3 = findViewById(R.id.option_3);
        option4 = findViewById(R.id.option_4);
        option5 = findViewById(R.id.option_5);
        numberQuestion = findViewById(R.id.list_quest);
        prevQuestion = findViewById(R.id.prev);
        nextQuestion = findViewById(R.id.next);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
    }

    private void fetchQuestion() {
        String category = getIntent().getStringExtra("category");
        loading.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        try {
            service.apiService(service.quiz + "category=" + category, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONArray data = new JSONArray(hashMap.get("data"));

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject detail = data.getJSONObject(i);

                                QuizModel item = new QuizModel();

                                item.setNumber(String.valueOf(i + 1));
                                item.setId(detail.getString("content_id"));
                                item.setCategory(detail.getString("master_header_title"));
                                item.setSection(detail.getString("header_title"));
                                item.setQuestion(detail.getString("title"));
                                item.setAnswer(detail.getString("answer"));
                                item.setPercentage(detail.getString("percentage") + "%");

                                lists.add(item);
                            }
                            showHideButtonNextPrev();
                            setQuestion(currentNumber);
                        }
                        loading.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                    } catch (Exception er) {
                        loading.setVisibility(View.GONE);
                        content.setVisibility(View.VISIBLE);
                        er.printStackTrace();
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            loading.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }

    private void setQuestion(int index) {
        try {
            currentAnswer = "";
            QuizModel item = lists.get(index);

            category.setText(item.getCategory());
            section.setText(item.getSection());
            number.setText(item.getNumber());
            question.setText(item.getQuestion());
            percentage.setText(item.getPercentage());

            checkedOption(null);
            switch (item.getAnswer()) {
                case "1":
                    checkedOption(option1);
                    break;
                case "2":
                    checkedOption(option2);
                    break;
                case "3":
                    checkedOption(option3);
                    break;
                case "4":
                    checkedOption(option4);
                    break;
                case "5":
                    checkedOption(option5);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void numberQuestion() {
        final View content = helper.inflateView(R.layout.quiz_number);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(content);

        FlexboxLayout list = content.findViewById(R.id.list);
        list.removeAllViews();

        for (int i=0;i<lists.size();i++) {
            QuizModel item = lists.get(i);

            View number = helper.inflateView(R.layout.item_number);
            TextView numberText = number.findViewById(R.id.number);

            numberText.setId(i);
            numberText.setText(String.valueOf(i + 1));

            if (!item.getAnswer().isEmpty() && !item.getAnswer().equals("null") && currentNumber != i) {
                numberText.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_success));
                numberText.setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));
            } if (currentNumber == i) {
                numberText.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_primary));
                numberText.setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));
            }

            int index = i;
            numberText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i=0;i<list.getChildCount();i++) {
                        list.getChildAt(i).getRootView().findViewById(i).setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_white));
                        ((TextView) list.getChildAt(i).getRootView().findViewById(i)).setTextColor(ContextCompat.getColor(Quiz.this, R.color.black));
                    }
                    v.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_primary));
                    ((TextView) v).setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));

                    currentNumber = index;
                    setQuestion(currentNumber);
                    dialog.dismiss();
                }
            });

            list.addView(number);
        }

        dialog.show();
    }
    
    private void checkedOption(View view) {
        option1.setTextColor(black);
        option2.setTextColor(black);
        option3.setTextColor(black);
        option4.setTextColor(black);
        option5.setTextColor(black);

        option1.setBackground(badgeWhite);
        option2.setBackground(badgeWhite);
        option3.setBackground(badgeWhite);
        option4.setBackground(badgeWhite);
        option5.setBackground(badgeWhite);

        if (view != null) {
            ((TextView) view).setBackground(badgePrimary);
            ((TextView) view).setTextColor(white);
        }
    }

    private void showHideButtonNextPrev() {
        prevQuestion.setVisibility(View.VISIBLE);
        nextQuestion.setVisibility(View.VISIBLE);

        if (currentNumber == 0) {
            prevQuestion.setVisibility(View.GONE);
        } else if (currentNumber >= lists.size() - 1) {
            nextQuestion.setVisibility(View.GONE);
        }
    }

    private void saveQuiz(boolean nextQuestion) {
        try {
            QuizModel item = lists.get(currentNumber);

            param.clear();
            param.put("id", item.getId());
            param.put("answer", currentAnswer);
            service.apiService(service.quizSave, param, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        helper.showToast(hashMap.get("message"), 0);
                        if (hashMap.get("status").equals("true")) {
                            lists.get(currentNumber).setAnswer(currentAnswer);

                            if (nextQuestion) {
                                if (currentNumber < lists.size())
                                    currentNumber++;
                                showHideButtonNextPrev();
                                setQuestion(currentNumber);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_quest:
                numberQuestion();
                break;
            case R.id.option_1:
                currentAnswer = "1";
                checkedOption(option1);
                saveQuiz(false);
                break;
            case R.id.option_2:
                currentAnswer = "2";
                checkedOption(option2);
                saveQuiz(false);
                break;
//            case R.id.option_3:
//                currentAnswer = "3";
//                checkedOption(option3);
//                saveQuiz(false);
//                break;
            case R.id.option_4:
                currentAnswer = "4";
                checkedOption(option4);
                saveQuiz(false);
                break;
            case R.id.option_5:
                currentAnswer = "5";
                checkedOption(option5);
                saveQuiz(false);
                break;
            case R.id.prev:
                if (lists.size() > 0)
                    currentNumber--;
                showHideButtonNextPrev();
                setQuestion(currentNumber);
                break;
            case R.id.next:
                if (currentNumber < lists.size())
                    currentNumber++;
                showHideButtonNextPrev();
                setQuestion(currentNumber);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}