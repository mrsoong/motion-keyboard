package com.example.motionkey.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * A word prediction class which utilizes a SQLite database to store a word frequency chart
 * This class will be used to auto-complete the users word.
 * Created by Mark on 2017-03-21.
 */

public class WordPredict extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.motionkey/databases/";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private static final String DATABASE_NAME = "wordFrequency.db";

    public WordPredict (Context current){
        super(current, DATABASE_NAME, null, 1);
        this.myContext = current;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.

        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your assets folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * Opens database
     * @throws SQLException
     */
    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    /**
     * Closes database
     */
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * A function which tries to predict the word give a substring. This function uses a word
     * frequency database provided by Wikitonary and can be found here :
     * https://en.wiktionary.org/wiki/Wiktionary:Frequency_lists#TV_and_movie_scripts
     *
     * @param search The beginning of the string you want to try to auto-complete
     * @return The two most likely words
     */
    public String[] getTwoMostLikelyWords(String search) {
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
        String query = "SELECT * FROM en_freq"
                + " WHERE  words LIKE \"%" + search + "%\" ORDER BY frequency DESC LIMIT 2";
        Cursor resultSet = myDataBase.rawQuery(query, null);
        if (!resultSet.equals(null)) {

            if (resultSet.moveToFirst()) {

                if (!resultSet.isLast() || resultSet.isNull(0)) {

                    result[0] = resultSet.getString(0);
                    if (resultSet.moveToNext()){
                        resultSet.moveToPosition(1);
                        result[1] = resultSet.getString(0);
                    }
                }
            }
        }
        resultSet.close();
        return result;
    }

}