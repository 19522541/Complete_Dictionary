package com.batdaulaptrinh.completedictionary;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardGameActivity extends AppCompatActivity {
    ImageButton backBt;
    TextView countCard;
    TextView textWord;
    TextView ipa_usView;
    TextView ipa_ukView;
    Button showButton;
    String  t;
    LinearLayout descirbeLayout;
    //TextView textExample;
    TextView typeTextView;
    TextView textDefinition;
    Button leftButton;
    Button rightButton;
    ImageView thumbnail;
    public  String  example;
    public String type;
    public  Boolean show=false;
    public int  stt=1;
    boolean isLangUS;
    public String currentWord="";
    TextToSpeech tts;
    ImageButton btnSpeakBritish ;
    ImageButton btnSpeakAmerica ;
    public List<String> listWord= new ArrayList<String>();
    public List<String> listEnDifinition= new ArrayList<String>();
    static com.batdaulaptrinh.completedictionary.DatabaseHelper myDbHelper;
    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub
        if(currentWord==null||"".equals(currentWord))
        {
            String text = "Content not available";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,"1");
        }else {
            if (isLangUS) {
                tts.setLanguage(Locale.US) ;}else {tts.setLanguage(Locale.UK);}
            tts.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, "2");
        }
    }
    private  void loadContent( Cursor c){
        listWord.add(currentWord=c.getString(c.getColumnIndex("en_word")));
        while (c.moveToNext()) {
            listWord.add(c.getString(c.getColumnIndex("en_word")));
            listEnDifinition.add(c.getString(c.getColumnIndex("en_definition")));
        }
    }
    private void emtylistword(Intent backtoMain, Intent backToHistory){
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
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
    private  void expandForWord(){
        if(show==false)descirbeLayout.setVisibility(View.INVISIBLE);
        else  descirbeLayout.setVisibility(View.VISIBLE);
    }
    private void openDatabase() {
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void getMeaningOfCurrentWord(String word){
        if(word=="") return;
       this.textWord.setText(word);
        Cursor c = myDbHelper.getMeaning(currentWord=word);
        DatabaseUtils.dumpCursorToString(c);
        if (c.moveToFirst()) {
            textDefinition.setText( c.getString(c.getColumnIndex("en_definition")));
           // textExample.setText(c.getString(c.getColumnIndex("example")));
            typeTextView.setText(c.getString(c.getColumnIndex("type")));
            ipa_usView.setText( c.getString(c.getColumnIndex("ipa_us")));
            ipa_ukView.setText( c.getString(c.getColumnIndex("ipa_uk")));
            thumbnail = findViewById(R.id.image);
            String bitmap = c.getString(c.getColumnIndex("thumbnail"));
            System.out.println("code here" +bitmap);
            byte[] decodedString = Base64.decode(bitmap, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            thumbnail.setImageBitmap(decodedByte);
        }
        else{
            textDefinition.setText( listEnDifinition.get(stt-1));
            //textExample.setText("UNKNOW");
            typeTextView.setText("UNKNOW");
            ipa_usView.setText( "UNKNOW");
            ipa_ukView.setText( "UNKNOW");
            t="UNKNOW";
        }
        countCard.setText(Integer.toString(stt)+" / " +Integer.toString(listWord.size()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLangUS= true;
        setContentView(R.layout.activity_card_game);
        backBt = findViewById(R.id.backButton);
         btnSpeakBritish = findViewById(R.id.button_speaker_british2);
         btnSpeakAmerica = findViewById(R.id.button_speaker_america2);
        descirbeLayout= (LinearLayout)findViewById(R.id.describeBox);
        Intent mainIt = new Intent(this,MainActivity.class);
        Intent backToHistory= new Intent(this,HistoryActivity.class);
        showButton =(Button)findViewById(R.id.showButton);
       textDefinition=(TextView) findViewById(R.id.describeTextView);
       //   textExample=(TextView) findViewById(R.id.exampleTextView);
          typeTextView=(TextView)findViewById(R.id.TypeWord);
           this.ipa_ukView=(TextView)findViewById(R.id.IPA_british2);
            this.ipa_usView=(TextView)findViewById(R.id.IPA_america2);
            this.textWord=(TextView)findViewById(R.id.word);
            leftButton=(Button)findViewById(R.id.leftbutton);
            rightButton =(Button)findViewById(R.id.rightbutton);
            countCard=(TextView)findViewById(R.id.textView6);
        btnSpeakBritish.setOnClickListener(v -> {
            isLangUS = false;
            ConvertTextToSpeech();
        });
        btnSpeakAmerica.setOnClickListener(v -> {
            isLangUS= true;
            ConvertTextToSpeech();
        });
        tts=new TextToSpeech(CardGameActivity.this, status -> {
            // TODO Auto-generated method stub
            if(status == TextToSpeech.SUCCESS){
                int result=tts.setLanguage(Locale.US);
                if(result==TextToSpeech.LANG_MISSING_DATA ||
                        result==TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("error", "This Language is not supported");
                }
                else{
                    ConvertTextToSpeech();
                }
            }
            else
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
                show= !show;
                expandForWord();
                Drawable img;
                if(!show) { img = getDrawable(R.drawable.downbutton);}
                else {   img = getDrawable(R.drawable.upbutton);   }

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
                if(listWord.size()==1) return;
                stt--;
                if(stt<=0) stt=listWord.size();
                getMeaningOfCurrentWord(listWord.get(stt-1));
                show=false;
                expandForWord();
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listWord.size()==1) return;
                  stt++;
            if(stt>listWord.size()) stt=1;
                getMeaningOfCurrentWord(listWord.get(stt-1));
             show=false;
             expandForWord();
            }
        });
    }
    protected void onStart()
    {
        super.onStart();
        Intent mainIt = new Intent(this,MainActivity.class);
        Intent backToHistory= new Intent(this,HistoryActivity.class);
        Cursor c = myDbHelper.getFavourite() ;
        DatabaseUtils.dumpCursorToString(c);
        if (c.moveToFirst()) loadContent(c);
        else { emtylistword(mainIt,backToHistory);
                    this.onStop();
        }
        this.getMeaningOfCurrentWord(currentWord);
    }
}