package com.nita.penamob.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.nita.penamob.R;
import com.nita.penamob.api.Service;
import com.nita.penamob.model.QuizModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;

public class Quiz extends BaseController implements View.OnClickListener {
    private List<QuizModel> lists = new ArrayList<>();
    private TextView number, question, questionUnFilled, totalQuestion, duration;
    private FancyButton numberQuestion, nextQuestion, prevQuestion, finishQuiz;
    private EditText yourAnswer;
    private LinearLayout contentOption;

    private Drawable badgeWhite, badgePrimary;
    private int black, white, currentNumber = 0;
    private String currentAnswer = "";
    private String answerId;
    private String alphabet[] = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        badgeWhite = ContextCompat.getDrawable(Quiz.this, R.drawable.form_radio);
        badgePrimary = ContextCompat.getDrawable(Quiz.this, R.drawable.form_card_primary);
        black = ContextCompat.getColor(Quiz.this, R.color.black);
        white = ContextCompat.getColor(Quiz.this, R.color.white);

        findView();
        init();
        fetchQuestion();
    }

    private void init() {
        currentAnswer = "";
        title.setText("Kuis");
        back.setOnClickListener(this::onClick);

        numberQuestion.setOnClickListener(this::onClick);
        prevQuestion.setOnClickListener(this::onClick);
        nextQuestion.setOnClickListener(this::onClick);
        finishQuiz.setOnClickListener(this::onClick);
    }

    private void findView() {
        number = findViewById(R.id.number);
        question = findViewById(R.id.question);
        numberQuestion = findViewById(R.id.list_quest);
        prevQuestion = findViewById(R.id.prev);
        nextQuestion = findViewById(R.id.next);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);

        contentOption = findViewById(R.id.content_option);
        yourAnswer = findViewById(R.id.your_answer);

        questionUnFilled = findViewById(R.id.total_question_unfilled);
        totalQuestion = findViewById(R.id.total_question);
        duration = findViewById(R.id.duration);
        finishQuiz = findViewById(R.id.submit);
    }

    private void fetchQuestion() {
        String id = getIntent().getStringExtra("id");
        try {
            service.apiService(service.quizStart + id, null, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        if (hashMap.get("status").equals("true")) {
                            JSONObject data = new JSONObject(hashMap.get("data"));

                            duration.setText(data.getString("duration"));
                            answerId = data.getString("answer_id");

                            countDown(data.getLong("time_diff"), duration);

                            if (!data.isNull("question")) {
                                JSONArray question = data.getJSONArray("question");

                                for (int i = 0; i < question.length(); i++) {
                                    JSONObject detail = question.getJSONObject(i);

                                    QuizModel item = new QuizModel();

                                    item.setNumber(String.valueOf(i + 1));
                                    item.setId(detail.getString("id"));
                                    item.setQuestion(detail.getString("question"));
                                    item.setAnswer(detail.getString("your_answer"));
                                    item.setType(detail.getString("type"));
                                    if(detail.getString("type").equals("MULTIPLE_CHOICE"))
                                        item.setOption(detail.getJSONArray("options"));

                                    lists.add(item);
                                }

                                showHideButtonNextPrev();
                                setQuestion(currentNumber);

                                totalQuestion.setText(lists.size() + " Soal");
                            }
                        } else {
                            stopQuiz();
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

    public void countDown(long millisecond,final TextView tv){
        new CountDownTimer(millisecond, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) (millisUntilFinished / (1000 * 60)) % 60;
                int hour = (int) (millisUntilFinished / (1000 * 60 * 60)) % 60;

                tv.setText(String.format("%02d", hour)
                        + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Completed");
                stopQuiz();
            }
        }.start();
    }

    private void setQuestion(int index) {
        try {
            currentAnswer = "";
            QuizModel item = lists.get(index);

            number.setText(item.getNumber());
//            question.setText(item.getQuestion());
            helper.setTextHtml(question, item.getQuestion());

            countUnFilledQuest();

            if (item.getType().equals("ESSAY")) { // if type of question is essay
                yourAnswer.setVisibility(View.VISIBLE);
                contentOption.setVisibility(View.GONE);
                yourAnswer.setText(item.getAnswer());

                if (!item.getAnswer().isEmpty())
                    lists.get(index).setEditAnswer(true);
            } else { // if type of question is multiple choice
                yourAnswer.setVisibility(View.GONE);
                contentOption.setVisibility(View.VISIBLE);

                if (item.getOption().length() > 0) {
                    contentOption.removeAllViews();

                    for (int x = 0; x < item.getOption().length(); x++) {
                        View option = helper.inflateView(R.layout.item_quiz_option);

                        TextView optionText = option.findViewById(R.id.option_text);

                        helper.setTextHtml(optionText, item.getOption().get(x).toString());

                        int finalX = x;
                        option.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                checkedOption(finalX);

                                if (!item.getAnswer().isEmpty())
                                    lists.get(finalX).setEditAnswer(true);
                                currentAnswer = alphabet[finalX];
                                saveQuiz(false);
                            }
                        });
                        contentOption.addView(option);

                        // set selected option by curr answer
                        if (item.getAnswer().equals(alphabet[finalX])) {
                            checkedOption(finalX);
                        }

                    }
                }
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

        for (int i = 0; i < lists.size(); i++) {
            QuizModel item = lists.get(i);

            View number = helper.inflateView(R.layout.item_number);
            TextView numberText = number.findViewById(R.id.number);

            numberText.setId(i);
            numberText.setText(String.valueOf(i + 1));

            if (!item.getAnswer().isEmpty() && !item.getAnswer().equals("null") && currentNumber != i) {
                numberText.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_success));
                numberText.setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));
            }
            if (currentNumber == i) {
                numberText.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_primary));
                numberText.setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));
            }

            int index = i;
            numberText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.getChildCount(); i++) {
                        list.getChildAt(i).getRootView().findViewById(i).setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_white));
                        ((TextView) list.getChildAt(i).getRootView().findViewById(i)).setTextColor(ContextCompat.getColor(Quiz.this, R.color.black));
                    }
                    v.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.badge_primary));
                    ((TextView) v).setTextColor(ContextCompat.getColor(Quiz.this, R.color.white));

                    currentNumber = index;
                    setQuestion(currentNumber);
                    showHideButtonNextPrev();
                    dialog.dismiss();
                }
            });

            list.addView(number);
        }

        dialog.show();
    }

    private void checkedOption(int index) {
        for (int x = 0; x < contentOption.getChildCount(); x++) {
            View view  = contentOption.getChildAt(x);

            LinearLayout content = view.findViewById(R.id.content_item_option);
            TextView textOption = view.findViewById(R.id.option_text);

            textOption.setTextColor(black);
            content.setBackground(badgeWhite);

            if (index == x) {
                textOption.setTextColor(white);
                content.setBackground(badgePrimary);
                content.setPadding(21, 21, 21, 21);
            }
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

    private void countUnFilledQuest() {
        int totalUnFilled = 0;
        for (int x = 0; x < lists.size(); x++) {
            QuizModel item = lists.get(x);
            if (item.getAnswer().isEmpty())
                totalUnFilled++;
        }
        questionUnFilled.setText(totalUnFilled + " Soal");
    }

    private void saveQuiz(boolean nextQuestion) {
        try {
            QuizModel item = lists.get(currentNumber);

            if (item.getType().equals("ESSAY"))
                currentAnswer = yourAnswer.getText().toString();

            if (!currentAnswer.isEmpty()) {
                param.clear();
                param.put("id", item.getId());
                param.put("answer_id", answerId);
                param.put("answer", currentAnswer);
                service.apiService(service.quizSave, param, null, true, new Service.hashMapListener() {
                    @Override
                    public String getHashMap(Map<String, String> hashMap) {
                        try {
                            helper.showToast(hashMap.get("message"), 0);
                            if (hashMap.get("status").equals("true")) {
                                lists.get(currentNumber).setAnswer(currentAnswer);
                                countUnFilledQuest();

                                if (nextQuestion) {
                                    if (currentNumber < lists.size())
                                        currentNumber++;
                                    showHideButtonNextPrev();
                                    setQuestion(currentNumber);

                                    currentAnswer = "";
                                }
                            }
                        } catch (Exception er) {
                            er.printStackTrace();
                        }
                        return null;
                    }
                });
            } else {
                if (currentNumber < lists.size())
                    currentNumber++;
                setQuestion(currentNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmStopQuiz() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        stopQuiz();
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialogInterface.dismiss();
                        return;
                    default:
                        return;
                }
            }
        };
        helper.popupConfirm("Apakah kamu yakin ?", "Kamu akan menyelesaikan kuis ini!", dialogClickListener);
    }

    private void stopQuiz() {
        try {
            param.clear();
            param.put("answer_id", answerId);
            service.apiService(service.quizStop, param, null, true, new Service.hashMapListener() {
                @Override
                public String getHashMap(Map<String, String> hashMap) {
                    try {
                        helper.showToast(hashMap.get("message"), 0);
                        if (hashMap.get("status").equals("true")) {
                            finish();
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
            case R.id.prev:
                if (lists.size() > 0)
                    currentNumber--;
                showHideButtonNextPrev();
                setQuestion(currentNumber);
                break;
            case R.id.next:
                showHideButtonNextPrev();
                saveQuiz(true);
                break;
            case R.id.submit:
                confirmStopQuiz();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}