package com.example.dictionary_ui;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class history extends AppCompatActivity {

    ImageButton backBt;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        backBt = findViewById(R.id.backButton);
        Intent mainIt = new Intent(this,MainActivity.class);
        backBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
                startActivity(mainIt);
            }
        });
        listView = findViewById(R.id.history);
        HashMap<String,String> data = new HashMap<>();
        data.put("Yasuo","số 1 thế giới");
        data.put("Yone","số 1 thế giới");
        List<HashMap<String,String>> itemList = new ArrayList<>();
        loadListviewData(itemList,data);
        SimpleAdapter adapter = new SimpleAdapter(this,itemList,R.layout.history_item,new String[] {"First Line","Second Line"},new int[]{
                R.id.word,R.id.defination});
        listView.setAdapter(adapter);
    }

    private void loadListviewData(List<HashMap<String,String>> itemList ,HashMap<String,String> data){
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String,String> rsMap = new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            rsMap.put("First Line",pair.getKey().toString());
            rsMap.put("Second Line", pair.getValue().toString());
            itemList.add(rsMap);
        }
    }
}