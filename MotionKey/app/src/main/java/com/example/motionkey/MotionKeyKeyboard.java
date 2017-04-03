/*
 * Copyright (C) 2017 MotionKey
 *
 */


package com.example.motionkey;

import android.content.Intent;
import android.database.SQLException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.InputMethodService;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.TextView;

import com.example.motionkey.utilities.Cursor;
import com.example.motionkey.utilities.NoiseFilter;
import com.example.motionkey.utilities.WordPredict;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


/**
 * The main keyboard.
 * Observes the magnetic field and accelerometer sensors to obtain
 * device orientation and update cursor.
 */

public class MotionKeyKeyboard extends InputMethodService implements SensorEventListener {

    private MotionKeyKeyboardView mMotionKeyView;
    private SensorManager mSensorManager;
    private Sensor mSensorMagneticField;
    private Sensor mSensorAccelerometer;
    private float[] mGravityData;
    private float[] mGeomagneticData;
    //raw data from the sensors
    private float[] originalOrientation = new float[3];
    //reset orientation so the current orientation is the new 'default'
    private float[] adjustedOrientation = new float[3];
    private float[] adjustmentAmount = new float[3];
    private int mAngleLimit = 40;
    private String[] alphabet = new String[3];
    private NoiseFilter mNoiseFilter;
    // Flag that can be used to enable/disable prediction. Defaults to true
    private boolean predictionEnabled = false;

    String output;
    InputConnection ic;

    boolean isCap;

    Cursor cursor;

    WordPredict suggestions;
    String [] predictions;
    private Button[] mKeyboardSuggestions;

    @Override
    public View onCreateInputView() {
        // Disable Android's auto screen rotation
//        Settings.System.putInt(
//                getContentResolver(),
//                Settings.System.ACCELEROMETER_ROTATION,
//                0
//        );

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //initialize adjustment amount of orientation degrees to zero
        Arrays.fill(adjustmentAmount, 0);
        //noise filter to smooth cursor movement
        this.mNoiseFilter = new NoiseFilter(20, 3);
        //initialize xml layout of the keyboard


        //SWITCH THIS FROM circle_keyboard TO keyboard TO CHANGE BACK TO DEFAULT
        mMotionKeyView = (MotionKeyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

        //initialize cursor by finding it in the initialized xml above
        cursor = new Cursor((TextView) mMotionKeyView.findViewById(R.id.cursor));
        mKeyboardSuggestions = new Button[2];
        mKeyboardSuggestions[0] = (Button) mMotionKeyView.findViewById(R.id.row0button1);
        mKeyboardSuggestions[1] = (Button) mMotionKeyView.findViewById(R.id.row0button2);

        //begin listening to the sensors
        mSensorManager.registerListener(this, mSensorMagneticField, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorAccelerometer, mSensorManager.SENSOR_DELAY_FASTEST);

        output = "";
        // Suggestions initialization
        suggestions = new WordPredict(this); //Creates DB

        try {

            suggestions.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            suggestions.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }
        predictions = new String[2];

        return mMotionKeyView;
    }

    @Override
    public void onWindowHidden(){
        super.onWindowHidden();
        //stop listening to the sensors
        mSensorManager.unregisterListener(this);

        // Re-enable Android's auto screen rotation
//        Settings.System.putInt(
//                getContentResolver(),
//                Settings.System.ACCELEROMETER_ROTATION,
//                1
//        );
    }

    @Override
    public void onWindowShown(){
        super.onWindowShown();
        //begin listening to the sensors
        mSensorManager.registerListener(this, mSensorMagneticField, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorAccelerometer, mSensorManager.SENSOR_DELAY_FASTEST);

        ic = getCurrentInputConnection();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop listening to the sensors
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Get gravity data via the accelerometer sensor
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravityData = event.values;
            //Get the Geomagnetic data via the magnetic field sensor;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagneticData = event.values;
        }
        //callback to update cursor
        updateKeyboard();
    }

    public void updateKeyboard() {
        cursor.updateCursorPosition(this.mSensorManager, this.mGravityData, this.mGeomagneticData,
                                    this.originalOrientation, this.adjustedOrientation, this.adjustmentAmount,
                                    this.mNoiseFilter, this.mAngleLimit);

        //notify observer
        if (mMotionKeyView.isMotionKeyKeyboardElementsFound()) {
            int[] mCursorPosition = new int[2];
            mCursorPosition[0] = (mMotionKeyView.getWidth())/2-cursor.getPaddingRight()/2+cursor.getPaddingLeft()/2;
            mCursorPosition[1] = (mMotionKeyView.getHeight())/2-cursor.getPaddingBottom()/2+cursor.getPaddingTop()/2;
            String key = mMotionKeyView.getMotionKeyElements().updateCursorPosition(mCursorPosition);
//                    getKeyID(, mCursorPosition);

            if (key != null){
                switch (key) {
                    case "______________" :
                        output += " ";
                        ic.commitText(" ",1);
                        output= "";
                        break;
                    case "◀":
                        ic.deleteSurroundingText(1, 0);
                        if (output.length() > 0) {
                            output = output.substring(0, output.length()-1);
                        }
                        break;
                    case "reset" :
                        ic.deleteSurroundingText(ic.getTextBeforeCursor(1000000000, 0).length(), 0);
                        output = "";
                        break;
                    case "▲" :
                        loopViews(mMotionKeyView);
                        isCap=!isCap;
                        break;
                    case "Settings":
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    default:
                        output += key;
                        ic.commitText(key,1);
                }
                if (predictionEnabled) {
                    predictions = suggestions.getTwoMostLikelyWords(output);
                    this.mKeyboardSuggestions[0].setText(predictions[0]);
                    if (predictions[0].equals(predictions[1])) {
                        this.mKeyboardSuggestions[1].setText("");
                    } else {
                        this.mKeyboardSuggestions[1].setText(predictions[1]);
                    }
                }

            }
        }
    }
    private void loopViews(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View v = view.getChildAt(i);

            if (v instanceof Button) {
                if (isCap) {
                    ((Button) v).setAllCaps(false);

                } else {
                    ((Button) v).setAllCaps(true);
                }
            } else if (v instanceof ViewGroup) {

                this.loopViews((ViewGroup) v);
            }
        }
    }

    //Reset the cursor position to the center of the keyboard
    public void resetOrientation(View view) {
        //calculate the adjustment amount for the first time
        if (mGravityData != null && mGeomagneticData != null) {
            adjustmentAmount[0] = 0 - originalOrientation[0];
            adjustmentAmount[1] = 0 - originalOrientation[1];
            adjustmentAmount[2] = 0 - originalOrientation[2];
        }
        cursor.updatePadding(0,0,0,0);
    }

//    public void switchKeyboardView(View view) {
//        mMotionKeyView = (MotionKeyKeyboardView) getLayoutInflater().inflate(R.layout.circle_keyboard, null);
//        mCursor = (TextView) mMotionKeyView.findViewById(R.id.cursorCircle);
////        mMotionKeyView.resetMotionKeyKeyboardElementsFound();
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something if accuracy of sensor changes
        //Not needed at the moment
    }

    public void showSettings(View view) {
        Intent dialogIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }
}

