package com.example.scramble_words;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Button btnTech = findViewById(R.id.btnTech);
        Button btnScience = findViewById(R.id.btnScience);
        Button btnEnglish = findViewById(R.id.btnEnglish);
        Button btnGK = findViewById(R.id.btnGK);

        btnTech.setOnClickListener(v -> openGame("Technology"));
        btnScience.setOnClickListener(v -> openGame("Science"));
        btnEnglish.setOnClickListener(v -> openGame("English Vocabulary"));
        btnGK.setOnClickListener(v -> openGame("General Knowledge"));
    }

    private void openGame(String category) {
        Intent intent = new Intent(CategoryActivity.this, GameActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}
