package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class wordInfo extends AppCompatActivity {
    String enWord;
    public String enDefinition;
    public String example;
    public String synonyms;
    public String antonyms;
    public String ipa_us;
    String ipa_uk;
    TextView textWord;
    String type;
    TextView partOfSpeech;
    TextView textIPABritish;
    TextView textIPAAmerica;
    TextView textDefinition;
    TextView textExample;
    SearchView searchView;
    ImageView thumbnail;
    static com.batdaulaptrinh.completedictionary.DatabaseHelper myDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);

        textWord = findViewById(R.id.text_word);
        textIPAAmerica = findViewById(R.id.IPA_america);
        textIPABritish = findViewById(R.id.IPA_british);
        partOfSpeech = findViewById(R.id.parts_of_speech);
        textDefinition = findViewById(R.id.text_definition);
        textExample = findViewById(R.id.text_example);
        searchView = findViewById(R.id.search_view);
        thumbnail = findViewById(R.id.thumbnail);
        ImageButton btnSpeakBritish = findViewById(R.id.button_speaker_british);
        ImageButton btnSpeakAmerica = findViewById(R.id.button_speaker_america);

        myDbHelper = new com.batdaulaptrinh.completedictionary.DatabaseHelper(this);
        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            com.batdaulaptrinh.completedictionary.LoadDatabaseAsync task = new com.batdaulaptrinh.completedictionary.LoadDatabaseAsync(wordInfo.this);
            task.execute();
        }


        Intent intent = getIntent();
        enWord = intent.getStringExtra("en_word");
        Cursor c = myDbHelper.getMeaning(enWord);
        DatabaseUtils.dumpCursorToString(c);
        if (c.moveToFirst()) {
            enDefinition = c.getString(c.getColumnIndex("en_definition"));
            synonyms = c.getString(c.getColumnIndex("synonyms"));
            antonyms = c.getString(c.getColumnIndex("antonyms"));
            ipa_us= c.getString(c.getColumnIndex("ipa_us"));
            ipa_uk= c.getString(c.getColumnIndex("ipa_uk"));
            type = c.getString(c.getColumnIndex("type"));
            textWord.setText(c.getString(c.getColumnIndex("en_word")));
            example = c.getString(c.getColumnIndex("example"));
            textDefinition.setText(enDefinition);
            textIPAAmerica.setText(ipa_us);
            textIPABritish.setText(ipa_uk);
            textExample.setText(example);

            String bitmap = c.getString(c.getColumnIndex("thumbnail"));
            System.out.println("code here" +bitmap);
//            byte[] blob = c.getBlob(6);
//            System.out.println("bitmap" + Arrays.toString(blob));
//            ByteArrayInputStream imageStream = new ByteArrayInputStream(blob);
//            System.out.println("bitmap" + imageStream);
//            Bitmap theImage = BitmapFactory.decodeStream(imageStream);an
            byte[] decodedString = Base64.decode(bitmap, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            thumbnail.setImageBitmap(decodedByte);

        }

    }


    protected static void openDatabase() {
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}