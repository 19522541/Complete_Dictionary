package com.batdaulaptrinh.completedictionary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    static DatabaseHelper myDbHelper;
    ImageButton backButton;
    ImageButton deleteButton;
    //HuyThanh1x
    ArrayList<History> historyList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;
    String enWord;
    public String enDefinition;
    RelativeLayout emptyHistory;
    Cursor cursorHistory;
    static boolean databaseOpened = false;
    static void changeFavourite(String word,int favourite){
        myDbHelper.changeFavourite(word,favourite);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        myDbHelper = new DatabaseHelper(this);

        if (myDbHelper.checkDataBase()) {
            openDatabase();
        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(getApplicationContext());
            task.execute();
        }

        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        Intent mainIt = new Intent(this, MainActivity.class);
        backButton.setOnClickListener(v -> finish());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = HistoryActivity.this;
                AlertDialog.Builder alertDelete = new AlertDialog.Builder(context);
                alertDelete.setTitle("Are you sure")
                        .setMessage("ALl of your data will be erased permanently")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                myDbHelper.deleteHistory();
                                emptyHistory.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDelete.show();
            }
        });

        emptyHistory = findViewById(R.id.empty_history);
        recyclerView = findViewById(R.id.recycler_view_history);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        displayHistory();
    }

    protected static void openDatabase() {
        try {
            myDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void displayHistory() {
        historyList = new ArrayList<>();
        historyAdapter = new RecyclerViewAdapterHistory(this, historyList);
        recyclerView.setAdapter(historyAdapter);

        History h;

        if (databaseOpened) {
            cursorHistory = myDbHelper.getHistory();
            if (cursorHistory.moveToFirst()) {
                do {
                    h = new History(cursorHistory.getString(cursorHistory.getColumnIndex("en_word")), cursorHistory.getString(cursorHistory.getColumnIndex("en_definition")),cursorHistory.getInt(cursorHistory.getColumnIndex("favourite")));
                    historyList.add(h);
                }
                while (cursorHistory.moveToNext());
            }

            historyAdapter.notifyDataSetChanged();
        }

        if (historyAdapter.getItemCount() == 0) {
            emptyHistory.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyHistory.setVisibility(View.GONE);
        }
    }

}