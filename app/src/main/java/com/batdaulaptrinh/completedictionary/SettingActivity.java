package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {

    ImageButton backBt;
    SeekBar sbSpeed;
    TextToSpeech tts;
    Switch aSwitch;
    public static float speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        backBt = findViewById(R.id.backButton);
        sbSpeed = findViewById(R.id.seekBar);
        sbSpeed.setProgress((int)speed*100);
        Intent mainIt = new Intent(this,MainActivity.class);

        aSwitch = findViewById(R.id.darkModeSwitch);
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            aSwitch.setChecked(true);
        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                finish();
            }
        });
        tts=new TextToSpeech(SettingActivity.this, status -> {
            // TODO Auto-generated method stub

            if(status == TextToSpeech.SUCCESS){
                int result=tts.setLanguage(Locale.US);
                if(result==TextToSpeech.LANG_MISSING_DATA ||
                        result==TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("error", "This Language is not supported");
                }
                else{
//                    ConvertTextToSpeech();
                }
            }
            else
                Log.e("error", "Initilization Failed!");
        });


        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue= progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    ConvertTextToSpeech(progressChangedValue);
            }
        });
    }
    private void ConvertTextToSpeech(int speedOnChange) {
        // TODO Auto-generated method stub
        if (speedOnChange==0) speedOnChange=1;
        speed = (float)speedOnChange/50;
        Log.d("speed","="+speed);
        tts.setSpeechRate(speed);
        tts.setLanguage(Locale.US) ;
        String test="hello ";
        tts.speak(test, TextToSpeech.QUEUE_FLUSH, null, "2");
        }
}