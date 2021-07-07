package com.batdaulaptrinh.completedictionary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CardGameActivity extends AppCompatActivity {
    ImageButton backBt;
    TextView countCard;
    TextView textWord;
    TextView ipa_usView;
    TextView ipa_ukView;
    Button showButton;
    String t;
    SettingActivity setting;
    LinearLayout descirbeLayout;
    ConstraintLayout cardGameLayout;
    //TextView textExample;
    TextView typeTextView;
    TextView textDefinition;
    Button leftButton;
    Button rightButton;
    ImageView thumbnail;
    public String example;
    public String type;
    public Boolean show = false;
    public int stt = 1;
    boolean isLangUS;
    public String currentWord = "";
    TextToSpeech tts;
    ImageButton btnSpeakBritish;
    ImageButton btnSpeakAmerica;
    GestureDetector gesture;
    public List<String> listWord = new ArrayList<String>();
    public List<String> listEnDifinition = new ArrayList<String>();
    static com.batdaulaptrinh.completedictionary.DatabaseHelper myDbHelper;

    void afterRightClick() {
        if (listWord.size() == 1) return;
        stt++;
        if (stt > listWord.size()) stt = 1;
        getMeaningOfCurrentWord(listWord.get(stt - 1));
        showButton.setBackground(getDrawable(R.drawable.downbutton));
        show = false;
        expandForWord();

    }

    void afterLeftClick() {
        if (listWord.size() == 1) return;
        stt--;
        if (stt <= 0) stt = listWord.size();
        getMeaningOfCurrentWord(listWord.get(stt - 1));
        showButton.setBackground(getDrawable(R.drawable.downbutton));
        show = false;
        expandForWord();
    }

    private void ConvertTextToSpeech() {
        tts.setSpeechRate(setting.speed);


            if (isLangUS) {
                tts.setLanguage(Locale.US) ;}else {tts.setLanguage(Locale.UK);}
            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, "2");

//        // TODO Auto-generated method stub
//        if (currentWord == null || "".equals(currentWord)) {
//            String text = "Content not available";
//            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "1");
//        } else {
//            if (isLangUS) {
//                tts.setLanguage(Locale.US);
//            } else {
//                tts.setLanguage(Locale.UK);
//            }
//            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, "2");
//        }
    }

    private void loadContent(Cursor c) {
        listWord.add(currentWord = c.getString(c.getColumnIndex("en_word")));
        listEnDifinition.add(c.getString(c.getColumnIndex("en_definition")));
        while (c.moveToNext()) {

            listWord.add(c.getString(c.getColumnIndex("en_word")));

            listEnDifinition.add(c.getString(c.getColumnIndex("en_definition")));
            String x="empty";
            System.out.println(x=c.getString(c.getColumnIndex("en_word"))+"::"+c.getString(c.getColumnIndex("en_definition")));
            Log.d("%%%%LISTWORD%%%%", x);

        }
    }

    private void emtylistword(Intent backtoMain, Intent backToHistory) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Your favourite word list is empty");
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(backToHistory);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(backtoMain);
            }
        });
        dialog.show();
    }

    private void expandForWord() {
        if (show == false) descirbeLayout.setVisibility(View.INVISIBLE);
        else descirbeLayout.setVisibility(View.VISIBLE);
    }

    private void openDatabase() {
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceType")
    void getMeaningOfCurrentWord(String word) {
        for (String t :this.listWord){
            Log.d("#####WORD####",t);
        }
        if (word == "") return;
        this.textWord.setText(word);
        Cursor c = myDbHelper.getMeaning(currentWord = word);
        DatabaseUtils.dumpCursorToString(c);

        if (c.moveToFirst()) {
            textDefinition.setText(c.getString(c.getColumnIndex("en_definition")));
            // textExample.setText(c.getString(c.getColumnIndex("example")));
            typeTextView.setText(c.getString(c.getColumnIndex("type")));
            ipa_usView.setText(c.getString(c.getColumnIndex("ipa_us")));
            ipa_ukView.setText(c.getString(c.getColumnIndex("ipa_uk")));

            String bitmap = c.getString(c.getColumnIndex("thumbnail"));
            System.out.println("code here" + bitmap);
            byte[] decodedString = Base64.decode(bitmap, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            thumbnail.setImageBitmap(decodedByte);

        } else {

            textDefinition.setText(listEnDifinition.get(stt-1));
//            for(String k :listEnDifinition){
//
//                Log.d("%%%%listEnDifinition::",k);
//            }
            //textDefinition.setText("UNKNOW");
            //textExample.setText("UNKNOW");
           thumbnail.setImageResource(R.drawable.cardgame);
            typeTextView.setText("UNKNOW");
            ipa_usView.setText("UNKNOW");
            ipa_ukView.setText("UNKNOW");
            t = "UNKNOW";
        }
        countCard.setText(Integer.toString(stt) + " / " + Integer.toString(listWord.size()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLangUS = true;
        setContentView(R.layout.activity_card_game);
        backBt = findViewById(R.id.backButton);
        btnSpeakBritish = findViewById(R.id.button_speaker_british2);
        btnSpeakAmerica = findViewById(R.id.button_speaker_america2);
        thumbnail = findViewById(R.id.image);
        descirbeLayout = (LinearLayout) findViewById(R.id.describeBox);
        Intent mainIt = new Intent(this, MainActivity.class);
        Intent backToHistory = new Intent(this, HistoryActivity.class);
        showButton = (Button) findViewById(R.id.showButton);

        textDefinition = (TextView) findViewById(R.id.describeTextView);
        //   textExample=(TextView) findViewById(R.id.exampleTextView);
        typeTextView = (TextView) findViewById(R.id.TypeWord);
        this.ipa_ukView = (TextView) findViewById(R.id.IPA_british2);
        this.ipa_usView = (TextView) findViewById(R.id.IPA_america2);
        this.textWord = (TextView) findViewById(R.id.word);
        leftButton = (Button) findViewById(R.id.leftbutton);
        rightButton = (Button) findViewById(R.id.rightbutton);
        countCard = (TextView) findViewById(R.id.textView6);
        cardGameLayout = (ConstraintLayout) findViewById(R.id.cardgamelayout);
        this.gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent me1, MotionEvent me2, float vx, float vy) {
                if (me2.getX() - me1.getX() > 150 && Math.abs(vx) > 100) {
                    afterLeftClick();
                }
                if (me1.getX() - me2.getX() > 150 && Math.abs(vx) > 100) {
                    afterRightClick();
                }
                return super.onFling(me1, me2, vx, vy);
            }


        });
        this.cardGameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gesture.onTouchEvent(event);
                return true;
            }
        });
        btnSpeakBritish.setOnClickListener(v -> {
            isLangUS = false;
            ConvertTextToSpeech();
        });
        btnSpeakAmerica.setOnClickListener(v -> {
            isLangUS = true;
            ConvertTextToSpeech();
        });
        tts = new TextToSpeech(CardGameActivity.this, status -> {
            // TODO Auto-generated method stub
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("error", "This Language is not supported");
                } else {
             //     ConvertTextToSpeech();
                }
            } else
                Log.e("error", "Initilization Failed!");
        });
        myDbHelper = new com.batdaulaptrinh.completedictionary.DatabaseHelper(this);
        expandForWord();
        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            com.batdaulaptrinh.completedictionary.LoadDatabaseAsync task = new com.batdaulaptrinh.completedictionary.LoadDatabaseAsync(CardGameActivity.this);
            task.execute();
        }
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show = !show;
                expandForWord();
                Drawable img;
                if (!show) {
                    img = getDrawable(R.drawable.downbutton);
                } else {
                    img = getDrawable(R.drawable.upbutton);
                }

                showButton.setBackground(img);
                //    showButton.setCompoundDrawablesWithIntrinsicBounds(null, img, null, null);


            }
        });
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                finish();
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterLeftClick();

            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterRightClick();

            }
        });
    }

    protected void onStart() {
        super.onStart();
        Intent mainIt = new Intent(this, MainActivity.class);
        Intent backToHistory = new Intent(this, HistoryActivity.class);
        Cursor c = myDbHelper.getFavourite();

        DatabaseUtils.dumpCursorToString(c);
        if (c.moveToFirst()) loadContent(c);
        else {
            emtylistword(mainIt, backToHistory);
            this.onStop();
        }
        this.getMeaningOfCurrentWord(currentWord);
    }
}