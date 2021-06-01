package com.batdaulaptrinh.completedictionary;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    ArrayAdapter<String> arrayAdapter;
    CardView setting;
    CardView rememberGame;
    CardView cardGame;
    CardView history;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = findViewById(R.id.settingBt);
        rememberGame = findViewById(R.id.rememberGameBt);
        cardGame = findViewById(R.id.cardGameBt);
        history = findViewById(R.id.historyBt);
        Intent historyIt = new Intent(this, com.batdaulaptrinh.completedictionary.history.class);
        Intent settingIt = new Intent(this, com.batdaulaptrinh.completedictionary.setting.class);
        Intent rememberIt = new Intent(this, com.batdaulaptrinh.completedictionary.rememberGame.class);
        Intent cardGameIt = new Intent(this, com.batdaulaptrinh.completedictionary.cardGame.class);
        setting.setOnClickListener(v -> {
//            setContentView(R.layout.activity_setting);
            startActivity(settingIt);
        });
        history.setOnClickListener(v -> {
//            setContentView(R.layout.activity_history);
            startActivity(historyIt);
        });
        rememberGame.setOnClickListener(v -> {
//            setContentView(R.layout.activity_remember_game);
            startActivity(rememberIt);
        });
        cardGame.setOnClickListener(v -> {
//            setContentView(R.layout.activity_card_game);
            startActivity(cardGameIt);
        });


        searchView = findViewById(R.id.search_view_id);
        ListView listView = findViewById(R.id.rs_list);
        List<String> myList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        listView.setAdapter(arrayAdapter);
        myList.add("test");
        myList.add("yasuo");
        myList.add("zed");
        listView.setAdapter(arrayAdapter);
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                listView.setVisibility(View.GONE);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setVisibility(View.VISIBLE);
                arrayAdapter.getFilter().filter(newText);
                return true;
            }
        });


//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            ConstraintLayout temp = findViewById(R.id.main_bg);
//
//            @Override
//            public boolean onClose() {
//                listView.setVisibility(View.GONE);
//                return false;
//            }
//        });
        //HuyThanh0
        search = findViewById(R.id.search_view_id);
        search.setOnClickListener(v -> search.setIconified(false));


        myDbHelper = new com.batdaulaptrinh.completedictionary.DatabaseHelper(this);

        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            com.batdaulaptrinh.completedictionary.LoadDatabaseAsync task = new com.batdaulaptrinh.completedictionary.LoadDatabaseAsync(MainActivity.this);
            task.execute();
        }


        // setup SimpleCursorAdapter

        final String[] from = new String[]{"en_word"};
        final int[] to = new int[]{R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.suggestion_row, null, from, to, 0) {
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }

        };

        search.setSuggestionsAdapter(suggestionAdapter);


        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                // Add clicked text to search box
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word, false);

                //search.setQuery("",false);

                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(MainActivity.this, com.batdaulaptrinh.completedictionary.wordInfo.class);
                intent.putExtra("en_word",clicked_word);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });


        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String text = search.getQuery().toString();

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(text);

                if (m.matches()) {
                    Cursor c = myDbHelper.getMeaning(text);

                    if (c.getCount() == 0) {
                        showAlertDialog();
                    } else {
                        //search.setQuery("",false);
                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, com.batdaulaptrinh.completedictionary.wordInfo.class);
                        intent.putExtra("en_word",query);
                        startActivity(intent);

                    }

                } else {
                    showAlertDialog();
                }


                return false;
            }


            @Override
            public boolean onQueryTextChange(final String s) {
                search.setIconifiedByDefault(false); //Give Suggestion list margins

                Pattern p = Pattern.compile("[A-Za-z \\-.]{1,25}");
                Matcher m = p.matcher(s);

                if (m.matches()) {
                    Cursor cursorSuggestion = myDbHelper.getSuggestions(s);
                    suggestionAdapter.changeCursor(cursorSuggestion);
                }


                return false;
            }

        });

//        layoutManager = new LinearLayoutManager(MainActivity.this);
//
//        recyclerView.setLayoutManager(layoutManager);

    }

    // HuyThanh0x
    SearchView search;

    static com.batdaulaptrinh.completedictionary.DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;
    SimpleCursorAdapter suggestionAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    boolean doubleBackToExitPressedOnce = false;

    //HuyThanh0x
    protected static void openDatabase() {
        try {
            myDbHelper.openDataBase();
            databaseOpened = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        search.setQuery("", false);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Word Not Found");
        builder.setMessage("Please search again");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, (dialog, which) -> {
            // positive button logic
        });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText, (dialog, which) -> search.clearFocus());

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }
//
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
//    }

    //HuyThanh1x


}
