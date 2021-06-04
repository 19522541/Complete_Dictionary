package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CardGameActivity extends AppCompatActivity {

    ImageButton backBt;
    TextView countCart;
    TextView textWord;
    TextView ipa_usView;
    TextView ipa_ukView;
    Button showButton;
    LinearLayout descirbeLayout;
    TextView textExample;
    TextView typeTextView;
    public   String  enDefinition;
    public  String  example;
    public String type;
    public String ipa_us;
    public String ipa_uk;
    public  Boolean show;
    public int  stt;
    static com.batdaulaptrinh.completedictionary.DatabaseHelper myDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);
        backBt = findViewById(R.id.backButton);
        descirbeLayout= (LinearLayout)findViewById(R.id.describeBox);
        Intent mainIt = new Intent(this,MainActivity.class);
        showButton =(Button)findViewById(R.id.showButton);
        myDbHelper = new com.batdaulaptrinh.completedictionary.DatabaseHelper(this);

        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            com.batdaulaptrinh.completedictionary.LoadDatabaseAsync task = new com.batdaulaptrinh.completedictionary.LoadDatabaseAsync(CardGameActivity.this);
            task.execute();
        }
      Cursor c = myDbHelper.getFavourite();
      //  DatabaseUtils.dumpCursorToString(c);
      DatabaseUtils.dumpCursorToString(c);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descirbeLayout.setVisibility(View.INVISIBLE);
            }
        });
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                startActivity(mainIt);
            }
        });
    }

    private void openDatabase() {
        try {
            myDbHelper.openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}