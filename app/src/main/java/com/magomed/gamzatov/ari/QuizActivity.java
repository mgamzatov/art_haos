package com.magomed.gamzatov.ari;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private TextView countLabel;
    private TextView questionLabel;
    private Button answerBtn1;
    private Button answerBtn2;
    private Button answerBtn3;

    private String rightAnswer;
    private int rightAnswerCount = 0;
    private int quizCount = 0;
    static final public int QUIZ_COUNT = 3;

    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();

    String quizData[][] = {
            {
                "На сюжет какого литературного произведения П.И. Чайковский написал увертюру?",
                    "Гроза",
                    "Братья Карамазовы",
                    "Капитанская дочка"
            },
            {
                "Про кого из этих фантастических существ П.И. Чайковский НЕ сочинял оперы?",
                    "Водяной",
                    "Пиковая Дама",
                    "Чародейка"
            },
            {
                "Почетным доктором какого зарубежного университета был П. И. Чайковский?",
                    "Кембриджский университет",
                    "Болонский университет",
                    "MIT"
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionLabel = (TextView)findViewById(R.id.questionLabel);
        answerBtn1 = (Button)findViewById(R.id.answerBtn1);
        answerBtn2 = (Button)findViewById(R.id.answerBtn2);
        answerBtn3 = (Button)findViewById(R.id.answerBtn3);

        for (int i = 0; i < quizData.length; i++) {
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]);
            tmpArray.add(quizData[i][1]);
            tmpArray.add(quizData[i][2]);
            tmpArray.add(quizData[i][3]);

            quizArray.add(tmpArray);
        }

        showNextQuiz();
    }

    public void showNextQuiz() {
        ImageView wrong_sign1 = (ImageView) findViewById(R.id.wrong_sign1);
        ImageView wrong_sign2 = (ImageView) findViewById(R.id.wrong_sign2);
        ImageView wrong_sign3 = (ImageView) findViewById(R.id.wrong_sign3);

        wrong_sign1.setVisibility(View.INVISIBLE);
        wrong_sign2.setVisibility(View.INVISIBLE);
        wrong_sign3.setVisibility(View.INVISIBLE);

        ImageView right_sign1 = (ImageView) findViewById(R.id.right_sign1);
        ImageView right_sign2 = (ImageView) findViewById(R.id.right_sign2);
        ImageView right_sign3 = (ImageView) findViewById(R.id.right_sign3);

        right_sign1.setVisibility(View.INVISIBLE);
        right_sign2.setVisibility(View.INVISIBLE);
        right_sign3.setVisibility(View.INVISIBLE);

        ArrayList<String> quiz = quizArray.get(quizCount);

        questionLabel.setText(quiz.get(0));
        rightAnswer = quiz.get(1);

        quiz.remove(0);

        answerBtn1.setText(quiz.get(0));
        answerBtn2.setText(quiz.get(1));
        answerBtn3.setText(quiz.get(2));
    }

    public void checkAnswer(View view) {
        Button answerBtn = (Button) findViewById(view.getId());
        String btnText = answerBtn.getText().toString();
        String alertTitle;
        if (btnText.equals(rightAnswer)) {
            answerBtn.setCursorVisible(true);
            rightAnswerCount += 1;
        }
        ImageView wrong_sign1 = (ImageView) findViewById(R.id.wrong_sign1);
        ImageView wrong_sign2 = (ImageView) findViewById(R.id.wrong_sign2);
        ImageView wrong_sign3 = (ImageView) findViewById(R.id.wrong_sign3);

        wrong_sign1.setVisibility(View.INVISIBLE);

        wrong_sign2.setVisibility(View.VISIBLE);
        wrong_sign3.setVisibility(View.VISIBLE);

        ImageView right_sign1 = (ImageView) findViewById(R.id.right_sign1);
        ImageView right_sign2 = (ImageView) findViewById(R.id.right_sign2);
        ImageView right_sign3 = (ImageView) findViewById(R.id.right_sign3);

        right_sign1.setVisibility(View.VISIBLE);

        right_sign2.setVisibility(View.INVISIBLE);
        right_sign3.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                quizCount += 1;
                if (quizCount == QUIZ_COUNT) {
                    if (rightAnswerCount == QUIZ_COUNT) {
                        Intent intent = new Intent(getApplicationContext(), WinQuizResultActivity.class);
                        intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), QuizResultActivity.class);
                        intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount);
                        startActivity(intent);
                    }
                }
                else {
                    showNextQuiz();
                }
            }
        }, 500);
    }
}
