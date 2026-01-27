package com.example.scramble_words;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    TextView tvCategory, tvWord, tvHint, tvScore, tvCount, tvCorrectAnswer, tvTimer;
    EditText etAnswer;
    ImageButton btnCheck, btnNext, btnHint;

    ArrayList<WordItem> wordList = new ArrayList<>();
    int index = 0, score = 0, maxQ;
    boolean isAnswerRevealed = false;

    String category, level;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Views
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
        btnNext.setOnClickListener(v -> handleNext());

        btnHint.setOnClickListener(v -> {
            tvHint.setText("Hint: " + wordList.get(index).hint);
            tvHint.setVisibility(View.VISIBLE);
            btnHint.setVisibility(View.INVISIBLE);
        });
    }

    // ---------------- LEVEL LIMIT ----------------
    private int getLimit(String level) {
        if (level.equals("Easy")) return 5;
        if (level.equals("Medium")) return 4;
        if (level.equals("Hard")) return 3;
        return 2; // Extreme
    }

    // ---------------- LOAD WORDS ----------------
    private void loadWords() {
        DBHelper db = new DBHelper(this);
        Cursor c = db.getWordsByCategory(category);

        while (c.moveToNext()) {
            wordList.add(new WordItem(c.getString(1), c.getString(2)));
        }
        c.close();
        Collections.shuffle(wordList);
    }

    // ---------------- SHOW WORD ----------------
    private void showWord() {
        if (timer != null) timer.cancel();

        // GAME OVER
        if (index >= maxQ || index >= wordList.size()) {
            endGame();
            return;
        }

        WordItem item = wordList.get(index);

        tvWord.setText(scramble(item.word));
        tvWord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        // Reset UI
        tvHint.setVisibility(View.INVISIBLE);
        tvCorrectAnswer.setVisibility(View.INVISIBLE);
        etAnswer.setText("");

        tvScore.setText("Score: " + score);
        tvCount.setText((index + 1) + "/" + maxQ);

        btnNext.setBackgroundResource(R.drawable.pass);
        isAnswerRevealed = false;

        // ---------- HINT & TIMER LOGIC ----------
        btnHint.setVisibility(View.VISIBLE);
        if (level.equals("Easy") || level.equals("Medium")) {
            // Timer OFF
            tvTimer.setText("");
        } else {
            // Timer ON
            startTimer();
        }
    }

    // ---------------- TIMER (HARD & EXTREME) ----------------
    private void startTimer() {
        timer = new CountDownTimer(15000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Time: " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                handleNext(); // auto-pass
            }
        }.start();
    }

    // ---------------- NEXT / PASS ----------------
    private void handleNext() {
        if (!isAnswerRevealed) {
            tvCorrectAnswer.setText("Correct Answer: " + wordList.get(index).word);
            tvCorrectAnswer.setVisibility(View.VISIBLE);
            btnNext.setBackgroundResource(R.drawable.next);
            isAnswerRevealed = true;
        } else {
            index++;
            showWord();
        }
    }

    // ---------------- CHECK ANSWER ----------------
    private void checkAnswer() {
        if (isAnswerRevealed) return;

        String user = etAnswer.getText().toString().trim().toLowerCase();
        String correct = wordList.get(index).word.toLowerCase();

        if (user.equals(correct)) {
            score++;
            index++;
            showWord();
        } else {
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }
    }

    // ---------------- GAME OVER ----------------
    private void endGame() {
        saveBestScore();
        unlockNextLevel();

        Intent i = new Intent(this, GameOverActivity.class);
        i.putExtra("score", score);
        i.putExtra("category", category);
        i.putExtra("level", level);
        startActivity(i);
        finish();
    }

    // ---------------- SAVE BEST SCORE ----------------
    private void saveBestScore() {
        SharedPreferences sp = getSharedPreferences("BEST_SCORES", MODE_PRIVATE);
        String key = category + "_" + level + "_BEST";
        int best = sp.getInt(key, 0);
        if (score > best) sp.edit().putInt(key, score).apply();
    }

    // ---------------- UNLOCK NEXT LEVEL ----------------
    private void unlockNextLevel() {
        SharedPreferences.Editor e = getSharedPreferences("LEVELS", MODE_PRIVATE).edit();

        if (level.equals("Easy")) e.putBoolean(category + "_Medium", true);
        if (level.equals("Medium")) e.putBoolean(category + "_Hard", true);
        if (level.equals("Hard")) e.putBoolean(category + "_Extreme", true);

        e.apply();
    }

    // ---------------- SCRAMBLE WORD ----------------
    private String scramble(String word) {
        ArrayList<Character> chars = new ArrayList<>();
        for (char c : word.toCharArray()) chars.add(c);
        Collections.shuffle(chars);

        StringBuilder sb = new StringBuilder();
        for (char c : chars) sb.append(c);
        return sb.toString();
    }
}
