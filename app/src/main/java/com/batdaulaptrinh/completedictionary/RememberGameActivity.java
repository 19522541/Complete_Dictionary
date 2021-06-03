package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RememberGameActivity extends AppCompatActivity {

    ImageButton backBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember_game);
        backBt = findViewById(R.id.backButton);
        Intent mainIt = new Intent(this,MainActivity.class);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                startActivity(mainIt);
            }
        });
    }
}