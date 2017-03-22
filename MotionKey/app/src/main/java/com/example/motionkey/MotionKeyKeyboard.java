/*
 * Copyright (C) 2017 MotionKey
 *
 */


package com.example.motionkey;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motionkey.utilities.NoiseFilter;
import com.example.motionkey.utilities.WordPredict;

import java.io.IOException;
import java.io.InputStream;
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
    //this is the view that will be used as the cursor
    private TextView mCursor;
    //raw data from the sensors
    private float[] originalOrientation = new float[3];
    //reset orientation so the current orientation is the new 'default'
    private float[] adjustedOrientation = new float[3];
    private float[] adjustmentAmount = new float[3];
    private int mAngleLimit = 40;
    private HashMap<int[], Integer> keyLocation = new HashMap<int[], Integer>();
    private String[] alphabet = new String[3];
    private NoiseFilter mNoiseFilter;

    int curCursorPaddingTop;
    int curCursorPaddingRight;
    int curCursorPaddingBottom;
    int curCursorPaddingLeft;
    int curCursorWidth;
    int curCursorHeight;

    String output;
    InputConnection ic;

    boolean isCap;

    WordPredict suggestions;
    String [] predictions;
    private Button[] mKeyboardSuggestions;

    @Override
    public View onCreateInputView() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //initialize adjustment amount of orientation degrees to zero
        Arrays.fill(adjustmentAmount, 0);
        //noise filter to smooth cursor movement
        this.mNoiseFilter = new NoiseFilter(20, 0.75f, 3);
        //initialize xml layout of the keyboard


        //SWITCH THIS FROM circle_keyboard TO keyboard TO CHANGE BACK TO DEFAULT
        mMotionKeyView = (MotionKeyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

        //initialize cursor by finding it in the initialized xml above
        mCursor = (TextView) mMotionKeyView.findViewById(R.id.cursor);
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
            AssetManager assetManager = getBaseContext().getAssets();
            InputStream databaseInputStream = assetManager.open("en_freq.csv");

            suggestions.importData(databaseInputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
        predictions = new String[2];

        return mMotionKeyView;
    }

    @Override
    public void onWindowHidden(){
        super.onWindowHidden();
        //stop listening to the sensors
        mSensorManager.unregisterListener(this);
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
        updateCursorPosition();
    }

    public void updateCursorPosition() {
        float[] orientationMatrix = new float[3];

        //don't do anything until we have both sensor's data and the cursor view object
        if (mGravityData != null && mGeomagneticData != null && mCursor != null) {

            //Get the rotation matrix
            float[] rMatrix = new float[9];
            float[] remapMatrix = new float[9];

            //rotation matrix generated
            if (mSensorManager.getRotationMatrix(rMatrix, null, mGravityData, mGeomagneticData)) {

                //remap to device's own coordinate system
                mSensorManager.remapCoordinateSystem(rMatrix, mSensorManager.AXIS_Y,
                        mSensorManager.AXIS_MINUS_X, remapMatrix);

                //get remapped orientation
                mSensorManager.getOrientation(remapMatrix, orientationMatrix);

                originalOrientation[0] = (float) (Math.toDegrees(orientationMatrix[0]));
                originalOrientation[1] = (float) (Math.toDegrees(orientationMatrix[1]));
                originalOrientation[2] = (float) (Math.toDegrees(orientationMatrix[2]));

                // Check to see how much orientation has changed since the last time
                // updateCursorPosition() was called
                float[] orientationDelta = new float[3];
                orientationDelta[0] = originalOrientation[0] - this.mNoiseFilter.getOldestMeasurement()[0];
                orientationDelta[1] = originalOrientation[1] - this.mNoiseFilter.getOldestMeasurement()[1];
                orientationDelta[2] = originalOrientation[2] - this.mNoiseFilter.getOldestMeasurement()[2];
                if (Math.sqrt((Math.pow(orientationDelta[0], 2) + Math.pow(orientationDelta[1], 2) + Math.pow(orientationDelta[2], 2))) > 5.0) {
                    // Update the history of orientations
                    this.mNoiseFilter.setOldestMeasurement(originalOrientation);
                }
                else {
                    // Nothing changes
                }

                //adjustedOrientation's index 2 is top bottom position. Positive is bottom.
                //index 1 is left right position. Positive is right
                float[] smoothedOrientation = this.mNoiseFilter.getFilteredMeasurement();
                adjustedOrientation[0] = (float) (smoothedOrientation[0]
                        + adjustmentAmount[0]);
                adjustedOrientation[1] = (float) (smoothedOrientation[1]
                        + adjustmentAmount[1]);
                adjustedOrientation[2] = (float) (smoothedOrientation[2] * 1.5
                        + adjustmentAmount[2]);

                //store current cursor information
                curCursorPaddingTop = mCursor.getPaddingTop();
                curCursorPaddingRight = mCursor.getPaddingRight();
                curCursorPaddingBottom = mCursor.getPaddingBottom();
                curCursorPaddingLeft = mCursor.getPaddingLeft();
                curCursorWidth = mCursor.getWidth();
                curCursorHeight = mCursor.getHeight();


                //device tilting bottom vertically
                if (adjustedOrientation[2] > 0) {
                    //device tilting right horizontally
                    if (adjustedOrientation[1] > 0) {
                        mCursor.setPadding(
                                (Math.abs(Math.round((adjustedOrientation[1] / mAngleLimit) * curCursorWidth))),
                                (Math.abs(Math.round((adjustedOrientation[2] / mAngleLimit) * curCursorHeight))),
                                curCursorPaddingRight,
                                curCursorPaddingBottom);
                        //device tilting left horizontally
                    } else {
                        mCursor.setPadding(
                                curCursorPaddingLeft,
                                (Math.abs(Math.round((adjustedOrientation[2] / mAngleLimit) * curCursorHeight))),
                                (Math.abs(Math.round((adjustedOrientation[1] / mAngleLimit) * curCursorWidth))),
                                curCursorPaddingBottom);
                    }

                    //device tilting top vertically
                } else if (adjustedOrientation[2] <= 0) {
                    //device tilting right horizontally
                    if (adjustedOrientation[1] > 0) {
                        mCursor.setPadding(
                                (Math.abs(Math.round((adjustedOrientation[1] / mAngleLimit) * curCursorWidth))),
                                curCursorPaddingTop,
                                curCursorPaddingRight,
                                (Math.abs(Math.round((adjustedOrientation[2] / mAngleLimit) * curCursorHeight))));
                    //device tilting left horizontally
                    } else {
                        mCursor.setPadding(
                                curCursorPaddingLeft,
                                curCursorPaddingTop,
                                (Math.abs(Math.round((adjustedOrientation[1] / mAngleLimit) * curCursorWidth))),
                                (Math.abs(Math.round((adjustedOrientation[2] / mAngleLimit) * curCursorHeight))));
                    }
                }

                //notify observer
                if (mMotionKeyView.isMotionKeyKeyboardElementsFound()) {
                    int[] mCursorPosition = new int[2];
                    mCursorPosition[0] = (mMotionKeyView.getWidth())/2-curCursorPaddingRight/2+curCursorPaddingLeft/2;
                    mCursorPosition[1] = (mMotionKeyView.getHeight())/2-curCursorPaddingBottom/2+curCursorPaddingTop/2;
                    String key = mMotionKeyView.getMotionKeyElements().updateCursorPosition(mCursorPosition);
//                    getKeyID(, mCursorPosition);


                    if (key != null){
                        switch (key) {
                            case "______________" :
                                output += " ";
                                ic.commitText(" ",1);
//                                ic.finishComposingText();
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
                            default:
                                output += key;
                                ic.commitText(key,1);
                        }
                        Log.d("Text", output);
                        predictions = suggestions.getTwoMostLikelyWords(output);
                        Log.d("Suggestion", predictions[0] + " " + predictions[1]);
                        this.mKeyboardSuggestions[0].setText(predictions[0]);
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
        mCursor.setPadding(0,0,0,0);
    }

    public void logCursor(View view) {
        Log.d("keyboard", "cursor: "+"padding left: "+curCursorPaddingLeft);
        Log.d("keyboard", "cursor: "+"padding right: "+curCursorPaddingRight);
        Log.d("keyboard", "cursor: "+"padding top: "+curCursorPaddingTop);
        Log.d("keyboard", "cursor: "+"padding bottom: "+curCursorPaddingBottom);
        Log.d("keyboard", "view: "+"height: "+mMotionKeyView.getHeight());
        Log.d("keyboard", "view: "+"width: "+mMotionKeyView.getWidth());
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
      
}

