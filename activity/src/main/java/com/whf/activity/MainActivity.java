package com.whf.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.snackbar.Snackbar;
import com.whf.activity.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private int mCurrentIndex = 0;
    private static final String KEY_INDEX = "index";
    private float score =0;

    private ActivityMainBinding binding;

    private final Question[] list = new Question[]{
        new Question(R.string.question_one,false),
        new Question(R.string.question_two, true),
        new Question(R.string.question_three, true),
        new Question(R.string.question_four, true),
        new Question(R.string.question_five, false),
        new Question(R.string.question_six, true),
        new Question(R.string.question_seven, false),
        new Question(R.string.question_eight, true),
        new Question(R.string.question_nine, false),
        new Question(R.string.question_ten, false)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            Log.d("TAG", "onCreate: mCurrentIndex 为"+mCurrentIndex);

        }


        binding.trueButton.setOnClickListener(v -> checkAnswer(true));
        binding.falseButton.setOnClickListener(v -> checkAnswer(false));

        updateQuestion();

        binding.next.setOnClickListener(v -> {
            mCurrentIndex = ( mCurrentIndex + 1 ) % list.length;
            updateQuestion();
            binding.trueButton.setEnabled(true);
            binding.falseButton.setEnabled(true);
        });

        binding.back.setOnClickListener(v -> {
            mCurrentIndex = (mCurrentIndex > 0) ? (mCurrentIndex - 1) : (list.length - 1);
            updateQuestion();

        });
    }

    private void updateQuestion(){
        int question = list[mCurrentIndex].getmTextResId();
        binding.question.setText(question);

    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = list[mCurrentIndex].ismAnswerTrue();
        binding.trueButton.setEnabled(false);
        binding.falseButton.setEnabled(false);

        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            score++;
            messageResId = R.string.correct;
        } else {
            messageResId = R.string.incorrect;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
        Log.d("TAG", "checkAnswer: "+mCurrentIndex);
        if (mCurrentIndex == list.length-1){
            Log.d("TAG", "checkAnswer: 得分："+(score/list.length)*100);
            // snackbar 顶部显示分数
            Snackbar snackbar = Snackbar.make(binding.main, "You score: " + (score / list.length) * 100 + "分", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
            params.gravity = Gravity.TOP;
            snackbarView.setLayoutParams(params);
            snackbar.show();
            // 分数归零
            score = 0;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX,mCurrentIndex);
    }
}