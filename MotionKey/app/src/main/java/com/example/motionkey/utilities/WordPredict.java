package com.example.motionkey.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Mark on 2017-03-21.
 */

public class WordPredict extends SQLiteOpenHelper {
    private Context context;
    private SQLiteDatabase db;
    private int mWordLimit;

    private static final String DATABASE_NAME = "wordFrequency.db";

    public WordPredict (Context current){
        super(current, DATABASE_NAME, null, 1);
        this.context = current;
        this.mWordLimit = 50;
    }
    public void onCreate(SQLiteDatabase db){
        String SQL_CREATE_TABLE = "CREATE TABLE wordFrequency " +
                "(word text priamry key, frequency integer)";
        db.execSQL(SQL_CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS wordFrequency");
        onCreate(db);
    }
    public void importData(InputStream databaseInputStream){
        int word_limit_counter = 0;
        db = this.getWritableDatabase();
        BufferedReader buffer = new BufferedReader(new InputStreamReader(databaseInputStream));
        String line = "";
        String test = "";
        try {
            while ((line = buffer.readLine()) != null && word_limit_counter < this.mWordLimit) {
                String[] colums = line.split(",");
                if (colums.length != 2) {
//                    Log.d("MarkTest", "here??" + line);
                    continue;
                }
                ContentValues cv = new ContentValues();
                cv.put("word", colums[0].trim());
                cv.put("frequency", colums[1].trim());
                db.insert("wordFrequency", null, cv);
                test = colums[0] + " - " + colums[1];
                word_limit_counter ++;
//                Log.d("insert", test);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("MARK", test);
//        db.setTransactionSuccessful();
//        db.endTransaction();
        db.close();
    }

    public String[] getTwoMostLikelyWords(String search) {
        db = this.getReadableDatabase();
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
        String query = "SELECT * FROM wordFrequency"
                + " WHERE  word LIKE \"%" + search + "%\" ORDER BY frequency DESC LIMIT 2";
        Cursor resultSet;
        resultSet = db.rawQuery(query, null);
        if (!resultSet.equals(null)) {

            if (resultSet.moveToFirst()) {

                if (!resultSet.isLast() || resultSet.isNull(0)) {

                    result[0] = resultSet.getString(0);
                    if (resultSet.moveToNext()){
                        resultSet.moveToPosition(1);
                        result[1] = resultSet.getString(0);
                        Log.d("Mark", result[1]);
                    }
                }
            }
        }
        resultSet.close();
        return result;
    }
//InputStream is = getAssets().open("path/file.ext");

}
