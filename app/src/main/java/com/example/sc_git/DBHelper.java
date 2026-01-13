package com.example.scramble_words;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "scramble_db";
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE words (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "word TEXT," +
                        "hint TEXT," +
                        "category TEXT)"
        );

        insertDefaultWords(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS words");
        onCreate(db);
    }

    private void insertDefaultWords(SQLiteDatabase db) {

        // TECHNOLOGY (15)
        insert(db, "computer", "Electronic machine", "Technology");
        insert(db, "internet", "Global network", "Technology");
        insert(db, "keyboard", "Input device", "Technology");
        insert(db, "monitor", "Display device", "Technology");
        insert(db, "software", "Programs of a computer", "Technology");
        insert(db, "hardware", "Physical components", "Technology");
        insert(db, "processor", "Brain of computer", "Technology");
        insert(db, "database", "Organized data storage", "Technology");
        insert(db, "network", "Connected systems", "Technology");
        insert(db, "browser", "Used to access websites", "Technology");
        insert(db, "router", "Directs network traffic", "Technology");
        insert(db, "server", "Provides services", "Technology");
        insert(db, "storage", "Stores data", "Technology");
        insert(db, "algorithm", "Step-by-step solution", "Technology");
        insert(db, "firewall", "Network security system", "Technology");

        // SCIENCE (15)
        insert(db, "gravity", "Force of attraction", "Science");
        insert(db, "atom", "Smallest unit of matter", "Science");
        insert(db, "energy", "Ability to do work", "Science");
        insert(db, "molecule", "Group of atoms", "Science");
        insert(db, "oxygen", "Essential gas", "Science");
        insert(db, "neuron", "Nerve cell", "Science");
        insert(db, "planet", "Celestial body", "Science");
        insert(db, "ecosystem", "Community of organisms", "Science");
        insert(db, "velocity", "Speed with direction", "Science");
        insert(db, "acceleration", "Rate of change of velocity", "Science");
        insert(db, "photosynthesis", "Food-making process", "Science");
        insert(db, "voltage", "Electric potential difference", "Science");
        insert(db, "current", "Flow of electrons", "Science");
        insert(db, "force", "Push or pull", "Science");
        insert(db, "density", "Mass per volume", "Science");

        // ENGLISH VOCABULARY (15)
        insert(db, "expand", "To increase in size", "English Vocabulary");
        insert(db, "brief", "Short in duration", "English Vocabulary");
        insert(db, "fragile", "Easily broken", "English Vocabulary");
        insert(db, "obvious", "Easy to understand", "English Vocabulary");
        insert(db, "efficient", "Works well with less effort", "English Vocabulary");
        insert(db, "reluctant", "Unwilling", "English Vocabulary");
        insert(db, "vivid", "Very clear", "English Vocabulary");
        insert(db, "adapt", "Adjust to new conditions", "English Vocabulary");
        insert(db, "achieve", "Successfully complete", "English Vocabulary");
        insert(db, "confident", "Sure of oneself", "English Vocabulary");
        insert(db, "accurate", "Correct and precise", "English Vocabulary");
        insert(db, "creative", "Able to create new ideas", "English Vocabulary");
        insert(db, "flexible", "Able to bend or change", "English Vocabulary");
        insert(db, "hesitate", "Pause before acting", "English Vocabulary");
        insert(db, "improve", "Make better", "English Vocabulary");

        // GENERAL KNOWLEDGE (15)
        insert(db, "capital", "Head city of a country", "General Knowledge");
        insert(db, "continent", "Large landmass", "General Knowledge");
        insert(db, "currency", "Medium of exchange", "General Knowledge");
        insert(db, "democracy", "Government by people", "General Knowledge");
        insert(db, "constitution", "Supreme law of a country", "General Knowledge");
        insert(db, "parliament", "Law-making body", "General Knowledge");
        insert(db, "republic", "Country without a monarch", "General Knowledge");
        insert(db, "sovereignty", "Supreme power", "General Knowledge");
        insert(db, "population", "Total number of people", "General Knowledge");
        insert(db, "literacy", "Ability to read and write", "General Knowledge");
        insert(db, "economy", "System of production and trade", "General Knowledge");
        insert(db, "inflation", "Rise in prices", "General Knowledge");
        insert(db, "budget", "Estimated income and expenses", "General Knowledge");
        insert(db, "election", "Process of voting", "General Knowledge");
        insert(db, "citizenship", "Status of a citizen", "General Knowledge");
    }

    private void insert(SQLiteDatabase db, String word, String hint, String category) {
        ContentValues cv = new ContentValues();
        cv.put("word", word);
        cv.put("hint", hint);
        cv.put("category", category);
        db.insert("words", null, cv);
    }

    public Cursor getWordsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM words WHERE category = ?",
                new String[]{category}
        );
    }
}
