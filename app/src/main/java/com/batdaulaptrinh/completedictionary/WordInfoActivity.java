package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class WordInfoActivity extends AppCompatActivity {
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
    TextView textSynonyms;
    TextView textAntonyms;
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
        textSynonyms = findViewById(R.id.text_synonyms);

        ImageButton btnSpeakBritish = findViewById(R.id.button_speaker_british);
        ImageButton btnSpeakAmerica = findViewById(R.id.button_speaker_america);

        myDbHelper = new com.batdaulaptrinh.completedictionary.DatabaseHelper(this);
        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            com.batdaulaptrinh.completedictionary.LoadDatabaseAsync task = new com.batdaulaptrinh.completedictionary.LoadDatabaseAsync(WordInfoActivity.this);
            task.execute();
        }


        Intent intent = getIntent();
        enWord = intent.getStringExtra("en_word");
        fetchData(enWord);
        Cursor c = myDbHelper.getMeaning(enWord);
        DatabaseUtils.dumpCursorToString(c);
        if (c.moveToFirst()) {
            enDefinition = c.getString(c.getColumnIndex("en_definition"));
            synonyms = c.getString(c.getColumnIndex("synonyms"));
            antonyms = c.getString(c.getColumnIndex("antonyms"));
            ipa_us= c.getString(c.getColumnIndex("ipa_us"));
            ipa_uk= c.getString(c.getColumnIndex("ipa_uk"));
            type = c.getString(c.getColumnIndex("type"));
            example = c.getString(c.getColumnIndex("example"));
            synonyms = c.getString(c.getColumnIndex("synonyms"));
            if(synonyms!=null){
                String[] parts = synonyms.split(",");
                synonyms = parts[0];
            }

            textWord.setText(c.getString(c.getColumnIndex("en_word")));
            textDefinition.setText(enDefinition);
            textIPAAmerica.setText(ipa_us);
            textIPABritish.setText(ipa_uk);
            textExample.setText(example);
            textSynonyms.setText(synonyms);

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


            myDbHelper.insertHistory(enWord,enDefinition,0);

        }
    }
    protected static void openDatabase()
    {
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public void fetchData (String query){
        RequestQueue queue = Volley.newRequestQueue(WordInfoActivity.this);

        String url="https://api.dictionaryapi.dev/api/v2/entries/en_US/"+query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().charAt(0) == '[') {
                            parseJSON(response);
                        } else if(response.trim().charAt(0) == '{') {
                            Log.e("error" , "word not found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error" , "server error");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseJSON(String data) {
        System.out.println("data are " + data);
        try {
            JSONArray jsonMainNode = new JSONArray(data);
            String word = jsonMainNode.getJSONObject(0).getString("word");
            String IPAAmerican = jsonMainNode.getJSONObject(0).getJSONArray("phonetics").getJSONObject(0).getString("text");
            String audioURLAmerican = jsonMainNode.getJSONObject(0).getJSONArray("phonetics").getJSONObject(0).getString("audio");
            String partOfSpeech = jsonMainNode.getJSONObject(0).getJSONArray("meanings").getJSONObject(0).getString("partOfSpeech");
            String definition = jsonMainNode.getJSONObject(0).getJSONArray("meanings").getJSONObject(0).getJSONArray("definitions").getJSONObject(0).getString("definition");
            String example = jsonMainNode.getJSONObject(0).getJSONArray("meanings").getJSONObject(0).getJSONArray("definitions").getJSONObject(0).getString("example");
            String synonyms = jsonMainNode.getJSONObject(0).getJSONArray("meanings").getJSONObject(0).getJSONArray("definitions").getJSONObject(0).getJSONArray("synonyms").getString(0);

            textWord.setText(word);
            textDefinition.setText(definition);
            textIPAAmerica.setText(IPAAmerican);
            textIPABritish.setText(IPAAmerican);
            textExample.setText(example);
            textSynonyms.setText(synonyms);
        } catch (Exception e) {
            Log.i("App", "Error parsing data" + e.getMessage());

        }

    }
}