package com.example.scramble_words;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LevelActivity extends AppCompatActivity {

    String category;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_level);

        category = getIntent().getStringExtra("category");
        prefs = getSharedPreferences("LEVELS", MODE_PRIVATE);

        Button e = findViewById(R.id.btnEasy);
        Button m = findViewById(R.id.btnMedium);
        Button h = findViewById(R.id.btnHard);
        Button x = findViewById(R.id.btnExtreme);

        e.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_fade));
        m.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_fade));
        h.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_fade));
        x.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up_fade));

        e.setOnClickListener(v -> open("Easy"));
        m.setOnClickListener(v -> check("Medium"));
        h.setOnClickListener(v -> check("Hard"));
        x.setOnClickListener(v -> check("Extreme"));
    }

    private void check(String lvl) {
        if (prefs.getBoolean(category + "_" + lvl, false)) open(lvl);
        else Toast.makeText(this, "Complete previous level", Toast.LENGTH_SHORT).show();
    }

    private void open(String lvl) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("category", category);
        i.putExtra("level", lvl);
        startActivity(i);
    }
}
