package com.example.scramble_words;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_game_over);

        int score = getIntent().getIntExtra("score", 0);
        String cat = getIntent().getStringExtra("category");
        String lvl = getIntent().getStringExtra("level");

        TextView tvScore = findViewById(R.id.tvFinalScore);
        TextView tvBest = findViewById(R.id.tvBestScore);

        SharedPreferences p = getSharedPreferences("HIGHSCORE", MODE_PRIVATE);
        String key = cat + "_" + lvl;
        int best = p.getInt(key, 0);

        if (score > best) {
            best = score;
            p.edit().putInt(key, best).apply();
        }

        tvScore.setText("Score: " + score);
        tvBest.setText("Best Score: " + best);

        findViewById(R.id.btnRetry).setOnClickListener(v -> {
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("category", cat);
            i.putExtra("level", lvl);
            startActivity(i);
            finish();
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            startActivity(new Intent(this, CategoryActivity.class));
            finish();
        });
    }
}
