package com.batdaulaptrinh.completedictionary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
        Intent historyIt = new Intent(this, HistoryActivity.class);
        Intent settingIt = new Intent(this, SettingActivity.class);
        Intent rememberIt = new Intent(this, RememberGameActivity.class);
        Intent cardGameIt = new Intent(this, CardGameActivity.class);
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

                Intent intent = new Intent(MainActivity.this, WordInfoActivity.class);
                intent.putExtra("en_word", clicked_word);
                intent.putExtra("is_online","false");
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

//                    if (c.getCount() == 0 && !isNetworkAvailable()) {
//                        showAlertDialog();
//                    } else {
//                        search.clearFocus();
//                        search.setFocusable(false);
//
//                        Intent intent = new Intent(MainActivity.this, WordInfoActivity.class);
//                        intent.putExtra("en_word", query);
//                        startActivity(intent);
//
//                    }
                    if (c.getCount() != 0 ){
                        search.clearFocus();
                        search.setFocusable(false);

                        Intent intent = new Intent(MainActivity.this, WordInfoActivity.class);
                        intent.putExtra("en_word", query);
                        intent.putExtra("is_online","false");
                        startActivity(intent);
                    }
                    else {
                        if (!isNetworkAvailable()){
                            showAlertDialog();
                        }
                        else {
                            search.clearFocus();
                            search.setFocusable(false);
                            fetchData(query);

                        }
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        builder.setMessage("Please try again or open you wifi");

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
    public void fetchData (String query){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url="https://api.dictionaryapi.dev/api/v2/entries/en_US/"+query;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().charAt(0) == '[') {
                            Intent intent = new Intent(MainActivity.this, WordInfoActivity.class);
                            intent.putExtra("en_word", response);
                            intent.putExtra("is_online","true");
                            startActivity(intent);
                        } else if(response.trim().charAt(0) == '{') {
                            showAlertDialog();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showAlertDialog();;
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
