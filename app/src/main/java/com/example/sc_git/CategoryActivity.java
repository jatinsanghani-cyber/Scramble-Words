package com.example.scramble_words;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    ImageButton btnTech, btnScience, btnEnglish, btnGK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        btnTech = findViewById(R.id.btnTech);
        btnScience = findViewById(R.id.btnScience);
        btnEnglish = findViewById(R.id.btnEnglish);
        btnGK = findViewById(R.id.btnGK);

        btnTech.setOnClickListener(v ->
                selectCategory(btnTech, R.drawable.tec_g, "Technology")
        );

        btnScience.setOnClickListener(v ->
                selectCategory(btnScience, R.drawable.sc_g, "Science")
        );

        btnEnglish.setOnClickListener(v ->
                selectCategory(btnEnglish, R.drawable.e_g, "English Vocabulary")
        );

        btnGK.setOnClickListener(v ->
                selectCategory(btnGK, R.drawable.gk_g, "General Knowledge")
        );
    }

    private void selectCategory(ImageButton button, int selectedImage, String category) {

        // Change image to selected (_g)
        button.setBackgroundResource(selectedImage);

        // Delay so user sees the change
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(CategoryActivity.this, LevelActivity.class);
            intent.putExtra("category", category);
            startActivity(intent);
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  RESET ALL BUTTONS TO ORIGINAL IMAGES
        btnTech.setBackgroundResource(R.drawable.tec);
        btnScience.setBackgroundResource(R.drawable.sc);
        btnEnglish.setBackgroundResource(R.drawable.e);
        btnGK.setBackgroundResource(R.drawable.gk);
    }
}
