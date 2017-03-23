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
 * Created by Mark on 2017-03-21.
 */

public class WordPredict extends SQLiteOpenHelper {
//    private Context context;
//    private SQLiteDatabase db;

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
     * Copies your database from your local assets-folder to the just created empty database in the
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

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

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
//    public void onCreate(SQLiteDatabase db){
//        String SQL_CREATE_TABLE = "CREATE TABLE wordFrequency " +
//                "(word text priamry key, frequency integer)";
//        db.execSQL(SQL_CREATE_TABLE);
//    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS wordFrequency");
//        onCreate(db);
//    }
//    public void importData(InputStream databaseInputStream){
//        db = this.getWritableDatabase();
//        BufferedReader buffer = new BufferedReader(new InputStreamReader(databaseInputStream));
//        String line = "";
//        String test = "";
//        try {
//            while ((line = buffer.readLine()) != null) {
//                String[] colums = line.split(",");
//                if (colums.length != 2) {
////                    Log.d("MarkTest", "here??" + line);
//                    continue;
//                }
//                ContentValues cv = new ContentValues();
//                cv.put("word", colums[0].trim());
//                cv.put("frequency", colums[1].trim());
//                db.insert("wordFrequency", null, cv);
//                test = colums[0] + " - " + colums[1];
////                Log.d("insert", test);
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.d("MARK", test);
////        db.setTransactionSuccessful();
////        db.endTransaction();
//        db.close();
//    }

    public String[] getTwoMostLikelyWords(String search) {
//        db = this.getReadableDatabase();
        String[] result = new String[2];
        result[0] = "";
        result[1] = "";
        String query = "SELECT * FROM en_freq"
                + " WHERE  words LIKE \"%" + search + "%\" ORDER BY frequency DESC LIMIT 2";
        Cursor resultSet;
        resultSet = myDataBase.rawQuery(query, null);
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