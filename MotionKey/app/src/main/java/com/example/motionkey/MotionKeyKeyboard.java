/*
 * Copyright (C) 2017 MotionKey
 *
 */


package com.example.motionkey;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


/**
 * The main keyboard.
 * Observes the magnetic field and accelerometer sensors to obtain
 * device orientation and update cursor.
 */

public class MotionKeyKeyboard extends InputMethodService implements SensorEventListener {

    MotionKeyKeyboardView mMotionKeyView;

    private SensorManager mSensorManager;
    private Sensor mSensorMagneticField;
    private Sensor mSensorAccelerometer;

    private float[] mGravityData;
    private float[] mGeomagneticData;

    //this is the view that will be used as the cursor
    private TextView mCursor;

    // Determines how sensitive the keyboard is
    float sensitivity = 0.5f;

    //raw data from the sensors
    int historyLength = 20;
    int oldestHistoryIndex = 0;
    float[] orientationHistoryAzimuth = new float[historyLength];
    float[] orientationHistoryPitch = new float[historyLength];
    float[] orientationHistoryRoll = new float[historyLength];
    float[] originalOrientation = new float[3];

    //reset orientation so the current orientation is the new 'default'
    float[] adjustedOrientation = new float[3];
    float[] adjustmentAmount = new float[3];

    int mANGLE_LIMIT = 40;
    HashMap<int[], Integer> keyLocation = new HashMap<int[], Integer>();
    private String[] alphabet = new String[3];




    @Override
    public View onCreateInputView() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //initialize adjustment amount of orientation degrees to zero
        Arrays.fill(adjustmentAmount, 0);

        // Initialize the orientation histories
        Arrays.fill(orientationHistoryAzimuth, 0);
        Arrays.fill(orientationHistoryPitch, 0);
        Arrays.fill(orientationHistoryRoll, 0);

        //initialize xml layout of the keyboard
        mMotionKeyView = (MotionKeyKeyboardView) getLayoutInflater().inflate(R.layout.keyboard,
                null);

        //initialize cursor by finding it in the initialized xml above
        mCursor = (TextView) mMotionKeyView.findViewById(R.id.cursor);

        //begin listening to the sensors
        mSensorManager.registerListener(this, mSensorMagneticField,
                mSensorManager.SENSOR_DELAY_FASTEST);
        
        mSensorManager.registerListener(this, mSensorAccelerometer,
                mSensorManager.SENSOR_DELAY_FASTEST);

        alphabet[0] = "zxcvbnm";
        alphabet[1] = "asdfghjkl";
        alphabet[2] = "qwertyuiop";

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
        mSensorManager.registerListener(this, mSensorMagneticField,
                mSensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(this, mSensorAccelerometer,
                mSensorManager.SENSOR_DELAY_FASTEST);
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

        //callback
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
                orientationDelta[0] = originalOrientation[0] - orientationHistoryAzimuth[oldestHistoryIndex];
                orientationDelta[1] = originalOrientation[1] - orientationHistoryPitch[oldestHistoryIndex];
                orientationDelta[2] = originalOrientation[2] - orientationHistoryRoll[oldestHistoryIndex];
                if (Math.sqrt((Math.pow(orientationDelta[0], 2) + Math.pow(orientationDelta[1], 2) + Math.pow(orientationDelta[2], 2))) > 5.0) {
                    // Update the history of orientations
                    orientationHistoryAzimuth[oldestHistoryIndex] = originalOrientation[0];
                    orientationHistoryPitch[oldestHistoryIndex] = originalOrientation[1];
                    orientationHistoryRoll[oldestHistoryIndex] = originalOrientation[2];
                }
                else {
                    // Nothing changes
                }

                float[] smoothedOrientation = new float[3];
                Arrays.fill(smoothedOrientation, 0);
                for (int i = 0; i < historyLength; i++) {
                    smoothedOrientation[0] += orientationHistoryAzimuth[i];
                }
                for (int i = 0; i < historyLength; i++) {
                    smoothedOrientation[1] += orientationHistoryPitch[i];
                }
                for (int i = 0; i < historyLength; i++) {
                    smoothedOrientation[2] += orientationHistoryRoll[i];
                }
                smoothedOrientation[0] /= (float) historyLength;
                smoothedOrientation[1] /= (float) historyLength;
                smoothedOrientation[2] /= (float) historyLength;
                oldestHistoryIndex++;
                oldestHistoryIndex = oldestHistoryIndex % historyLength;


                //adjustedOrientation's index 2 is top bottom position. Positive is bottom.
                //index 1 is left right position. Positive is right
                adjustedOrientation[0] = (float) (smoothedOrientation[0] * sensitivity
                        + adjustmentAmount[0]);
                adjustedOrientation[1] = (float) (smoothedOrientation[1] * sensitivity
                        + adjustmentAmount[1]);
                adjustedOrientation[2] = (float) (smoothedOrientation[2] * sensitivity * 1.5
                        + adjustmentAmount[2]);

                //store current cursor information
                int curCursorPaddingTop = mCursor.getPaddingTop();
                int curCursorPaddingRight = mCursor.getPaddingRight();
                int curCursorPaddingBottom = mCursor.getPaddingBottom();
                int curCursorPaddingLeft = mCursor.getPaddingLeft();
                int curCursorWidth = mCursor.getWidth();
                int curCursorHeight = mCursor.getHeight();


                //device tilting bottom vertically
                if (adjustedOrientation[2] > 0) {
                    //device tilting right horizontally
                    if (adjustedOrientation[1] > 0) {
                        mCursor.setPadding(
                                (Math.abs(Math.round((adjustedOrientation[1] / mANGLE_LIMIT) * curCursorWidth))),
                                (Math.abs(Math.round((adjustedOrientation[2] / mANGLE_LIMIT) * curCursorHeight))),
                                curCursorPaddingRight,
                                curCursorPaddingBottom);
                    //device tilting left horizontally
                    } else {
                        mCursor.setPadding(
                                curCursorPaddingLeft,
                                (Math.abs(Math.round((adjustedOrientation[2] / mANGLE_LIMIT) * curCursorHeight))),
                                (Math.abs(Math.round((adjustedOrientation[1] / mANGLE_LIMIT) * curCursorWidth))),
                                curCursorPaddingBottom);
                    }

                //device tilting top vertically
                } else if (adjustedOrientation[2] <= 0) {
                    //device tilting right horizontally
                    if (adjustedOrientation[1] > 0) {
                        mCursor.setPadding(
                                (Math.abs(Math.round((adjustedOrientation[1] / mANGLE_LIMIT) * curCursorWidth))),
                                curCursorPaddingTop,
                                curCursorPaddingRight,
                                (Math.abs(Math.round((adjustedOrientation[2] / mANGLE_LIMIT) * curCursorHeight))));
                    //device tilting left horizontally
                    } else {
                        mCursor.setPadding(
                                curCursorPaddingLeft,
                                curCursorPaddingTop,
                                (Math.abs(Math.round((adjustedOrientation[1] / mANGLE_LIMIT) * curCursorWidth))),
                                (Math.abs(Math.round((adjustedOrientation[2] / mANGLE_LIMIT) * curCursorHeight))));
                    }
                }

//                Log.d("keyboard", "cursor: "+"padding left: "+curCursorPaddingLeft);
//                Log.d("keyboard", "cursor: "+"padding right: "+curCursorPaddingRight);
//                Log.d("keyboard", "cursor: "+"padding top: "+curCursorPaddingTop);
//                Log.d("keyboard", "cursor: "+"padding bottom: "+curCursorPaddingBottom);
//                Log.d("keyboard", "view: "+"height: "+mMotionKeyView.getHeight());
//                Log.d("keyboard", "view: "+"width: "+mMotionKeyView.getWidth());

                //notify observer
                if (mMotionKeyView.isMotionKeyKeyboardElementsFound()) {
                    int[] mCursorPosition = new int[2];
                    mCursorPosition[0] = (mMotionKeyView.getWidth())/2-curCursorPaddingRight/2+curCursorPaddingLeft/2;
                    mCursorPosition[1] = (mMotionKeyView.getHeight())/2-curCursorPaddingBottom/2+curCursorPaddingTop/2;
                    String key = mMotionKeyView.getMotionKeyElements().updateCursorPosition(mCursorPosition);
//                    getKeyID(, mCursorPosition);
                    if (key != null){
                        InputConnection ic = getCurrentInputConnection();
                        ic.commitText(key,1);
                    }
                }

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

//      Log.d("mainactivity", "0: " + Float.toString(adjustmentAmount[0]));
//      Log.d("mainactivity", "1: " + Float.toString(adjustmentAmount[1]));
//      Log.d("mainactivity", "2: " + Float.toString(adjustmentAmount[2]));
    }

//    public void keyHighlight() {
//        //get the current location of the cursor in the keyboard view
//        int[] mCursorPosition = new int[2];
////        mCursor.getLocationOnScreen(mCursorPosition);
//        mCursorPosition[0] = (curCursorWidth)-curCursorPaddingRight+curCursorPaddingLeft;
//        mCursorPosition[1] = (curCursorHeight)-curCursorPaddingBottom+curCursorPaddingTop;
////        Log.d("keyboard", "cursor: "+" x: "+mCursorPosition[0] + " y: "+mCursorPosition[1]);
//
//        int elem_to_highlight = mMotionKeyView.getMotionKeyElements().getElementAtPosition(mCursorPosition);
//
//        //element not found or hashmap not ready
//        if (elem_to_highlight == -1 ) {
//            return;
//        }
//
//        //first time setting element as highlighted
//        if (last_elem_highlighted == -1) {
//            last_elem_highlighted = elem_to_highlight;
////            last_elem_highlighted_color = mMotionKeyView.findViewById(elem_to_highlight).getba
//        } else {
//            //different from previous, unhighlight last and highlight new element
//            if (last_elem_highlighted != elem_to_highlight) {
//                mMotionKeyView.findViewById(last_elem_highlighted).setBackgroundColor(0x80ddff);
//            }
//        }
//
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something if accuracy of sensor changes
        //Not needed at the moment
    }


}
