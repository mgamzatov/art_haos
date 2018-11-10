package com.magomed.gamzatov.ari;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class QuizResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        TextView resultLabel = (TextView) findViewById(R.id.quizResultLabel);

        int score = getIntent().getIntExtra("RIGHT_ANSWER_COUNT", 0);
        resultLabel.setText(score + "/" + QuizActivity.QUIZ_COUNT);

    }

    public void returnTop(View view) {
        Intent intent = new Intent(getApplicationContext(), MoreActivity.class);
        startActivity(intent);
    }
}
