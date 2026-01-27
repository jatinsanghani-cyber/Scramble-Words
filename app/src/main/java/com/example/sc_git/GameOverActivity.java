package com.example.scramble_words;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    TextView tvFinalScore, tvBestScore;
    ImageButton btnRetry, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Bind views
        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvBestScore = findViewById(R.id.tvBestScore);
        btnRetry = findViewById(R.id.btnRetry);
        btnBack = findViewById(R.id.btnBack);

        // Get data from GameActivity
        int score = getIntent().getIntExtra("score", 0);
        String category = getIntent().getStringExtra("category");
        String level = getIntent().getStringExtra("level");

        // Show final score
        tvFinalScore.setText("Score: " + score);

        // Load best score
        SharedPreferences sp = getSharedPreferences("BEST_SCORES", MODE_PRIVATE);
        int bestScore = sp.getInt(category + "_" + level + "_BEST", 0);

        tvBestScore.setText("Best Score: " + bestScore);

        // Retry same level
        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, GameActivity.class);
            intent.putExtra("category", category);
            intent.putExtra("level", level);
            startActivity(intent);
            finish();
        });

        // Back to categories
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, CategoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
