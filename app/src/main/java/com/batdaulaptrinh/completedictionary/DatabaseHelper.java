package com.batdaulaptrinh.completedictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAME = "demo.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {

            this.getReadableDatabase();
            try {
                myContext.deleteDatabase(DB_NAME);
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("copyDataBase", "Database copied");


    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }


    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAME);
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Cursor getMeaning(String text) {
        Cursor c = myDataBase.rawQuery("SELECT en_word,en_definition,example,synonyms,antonyms,ipa_us,ipa_uk,type,thumbnail FROM demo WHERE en_word==UPPER('" + text + "')", null);
        return c;
    }

    public Cursor getSuggestions(String text) {
        Cursor c = myDataBase.rawQuery("SELECT _id, en_word FROM demo WHERE en_word LIKE '" + text + "%' LIMIT 40", null);
        return c;
    }


    public void insertHistory(String word, String definition, int favourite) {
        myDataBase.execSQL("INSERT INTO history(en_word,en_definition,favourite) VALUES(UPPER('" + word + "'),'" + definition + "','" + favourite + "')");

    }


    public Cursor getHistory() {
            Cursor c = myDataBase.rawQuery("select distinct  en_word, en_definition,favourite from history", null);
            return c;
    }

    public Cursor getFavourite() {
        Cursor c = myDataBase.rawQuery("select distinct  en_word, en_definition from history where favourite = 1", null);
        return c;
    }

    public void changeFavourite(String word,int favourite) {
        myDataBase.execSQL("update history set favourite = '" + favourite + "' where en_word = '" + word + "'");

    }


    public void deleteHistory() {
        myDataBase.execSQL("DELETE  FROM history");
    }


}
