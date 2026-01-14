package com.example.scramble_words;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    TextView tvCategory, tvWord, tvHint, tvScore, tvCount, tvCorrectAnswer, tvTimer;
    EditText etAnswer;
    Button btnCheck, btnNext, btnHint;

    ArrayList<WordItem> wordList = new ArrayList<>();

    int index = 0, score = 0, maxQ;
    boolean isAnswerRevealed = false;

    String category, level;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvCategory = findViewById(R.id.tvCategory);
        tvWord = findViewById(R.id.tvWord);
        tvHint = findViewById(R.id.tvHint);
        tvScore = findViewById(R.id.tvScore);
        tvCount = findViewById(R.id.tvCount);
        tvCorrectAnswer = findViewById(R.id.tvCorrectAnswer);
        tvTimer = findViewById(R.id.tvTimer);

        etAnswer = findViewById(R.id.etAnswer);
        btnCheck = findViewById(R.id.btnCheck);
        btnNext = findViewById(R.id.btnNext);
        btnHint = findViewById(R.id.btnHint);

        category = getIntent().getStringExtra("category");
        level = getIntent().getStringExtra("level");

        tvCategory.setText(category + " - " + level);
        maxQ = getLimit(level);

        loadWords();
        showWord();

        btnCheck.setOnClickListener(v -> checkAnswer());
        btnNext.setOnClickListener(v -> handlePassOrNext());

        btnHint.setOnClickListener(v -> {
            tvHint.setText("Hint: " + wordList.get(index).hint);
            tvHint.setVisibility(TextView.VISIBLE);
            btnHint.setVisibility(Button.GONE); // 👈 hide after use
        });
    }

    private int getLimit(String level) {
        if (level.equals("Easy")) return 5;
        if (level.equals("Medium")) return 4;
        if (level.equals("Hard")) return 3;
        return 2;
    }

    private void loadWords() {
        DBHelper db = new DBHelper(this);
        Cursor c = db.getWordsByCategory(category);

        while (c.moveToNext()) {
            wordList.add(new WordItem(
                    c.getString(1),
                    c.getString(2)
            ));
        }
        c.close();

        Collections.shuffle(wordList);
    }

    private void showWord() {
        if (timer != null) timer.cancel();

        if (index >= maxQ || index >= wordList.size()) {
            unlockNextLevel();

            Intent i = new Intent(this, GameOverActivity.class);
            i.putExtra("score", score);
            i.putExtra("category", category);
            i.putExtra("level", level);
            startActivity(i);
            finish();
            return;
        }

        WordItem item = wordList.get(index);

        tvWord.setText(scramble(item.word));
        tvWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        // Reset hint state
        tvHint.setVisibility(TextView.GONE);
        btnHint.setVisibility(Button.GONE);

        if (level.equals("Easy") || level.equals("Medium")) {
            btnHint.setVisibility(Button.VISIBLE);
        }

        tvCorrectAnswer.setVisibility(TextView.GONE);
        etAnswer.setText("");

        tvScore.setText("Score: " + score);
        tvCount.setText((index + 1) + "/" + maxQ);

        btnNext.setText("Pass");
        isAnswerRevealed = false;

        // Timer only for Hard & Extreme
        if (level.equals("Hard") || level.equals("Extreme")) {
            startTimer();
        } else {
            tvTimer.setText("");
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Time: " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                handlePassOrNext();
            }
        }.start();
    }

    private void checkAnswer() {
        if (isAnswerRevealed) return;

        String userAnswer = etAnswer.getText().toString().trim().toLowerCase();
        String correctAnswer = wordList.get(index).word.toLowerCase();

        if (userAnswer.equals(correctAnswer)) {
            score++;
            index++;
            showWord();
        } else {
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }

    private void handlePassOrNext() {
        if (!isAnswerRevealed) {
            tvCorrectAnswer.setText("Correct Answer: " + wordList.get(index).word);
            tvCorrectAnswer.setVisibility(TextView.VISIBLE);
            tvCorrectAnswer.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.fade_in)
            );
            btnNext.setText("Next Question");
            isAnswerRevealed = true;
        } else {
            index++;
            showWord();
        }
    }

    private void unlockNextLevel() {
        SharedPreferences.Editor e =
                getSharedPreferences("LEVELS", MODE_PRIVATE).edit();

        if (level.equals("Easy")) e.putBoolean(category + "_Medium", true);
        if (level.equals("Medium")) e.putBoolean(category + "_Hard", true);
        if (level.equals("Hard")) e.putBoolean(category + "_Extreme", true);

        e.apply();
    }

    private String scramble(String word) {
        ArrayList<Character> chars = new ArrayList<>();
        for (char c : word.toCharArray()) chars.add(c);
        Collections.shuffle(chars);

        StringBuilder sb = new StringBuilder();
        for (char c : chars) sb.append(c);
        return sb.toString();
    }
}
