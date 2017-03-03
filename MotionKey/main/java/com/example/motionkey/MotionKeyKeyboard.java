/*
 * Copyright (C) 2017 MotionKey
 *
 */


package com.example.motionkey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;


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

    //raw data from the sensors
    float[] originalOrientation = new float[3];

    //reset orientation so the current orientation is the new 'default'
    float[] adjustedOrientation = new float[3];
    float[] adjustmentAmount = new float[3];


    @Override
    public View onCreateInputView() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //initialize adjustment amount of orientation degrees to zero
        Arrays.fill(adjustmentAmount, 0);

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

        return mMotionKeyView;
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

                //adjustedOrientation's index 2 is top bottom position. Positive is bottom.
                //index 1 is left right position. Positive is right
                adjustedOrientation[0] = (float) (Math.toDegrees(orientationMatrix[0])
                        + adjustmentAmount[0]);
                adjustedOrientation[1] = (float) (Math.toDegrees(orientationMatrix[1])
                        + adjustmentAmount[1]);
                adjustedOrientation[2] = (float) (Math.toDegrees(orientationMatrix[2])
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
                        mCursor.setPadding((Math.abs(Math.round((adjustedOrientation[1] / 40)
                                * curCursorWidth))), (Math.abs(Math.round((adjustedOrientation[2]
                                    / 40) * curCursorHeight))), curCursorPaddingRight,
                                        curCursorPaddingBottom);
                    //device tilting left horizontally
                    } else {
                        mCursor.setPadding(curCursorPaddingLeft,
                                (Math.abs(Math.round((adjustedOrientation[2] / 40)
                                    * curCursorHeight))), (Math.abs(Math.round((adjustedOrientation[1]
                                        / 40) * curCursorWidth))), curCursorPaddingBottom);
                    }

                //device tilting top vertically
                } else if (adjustedOrientation[2] <= 0) {
                    //device tilting right horizontally
                    if (adjustedOrientation[1] > 0) {
                        mCursor.setPadding((Math.abs(Math.round((adjustedOrientation[1] / 40)
                                * curCursorWidth))), curCursorPaddingTop, curCursorPaddingRight,
                                    (Math.abs(Math.round((adjustedOrientation[2] / 40)
                                        * curCursorHeight))));
                    //device tilting left horizontally
                    } else {
                        mCursor.setPadding(curCursorPaddingLeft, curCursorPaddingTop,
                                (Math.abs(Math.round((adjustedOrientation[1] / 40)
                                    * curCursorWidth))), (Math.abs(Math.round((adjustedOrientation[2]
                                        / 40) * curCursorHeight))));
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do something if accuracy of sensor changes
        //Not needed at the moment
    }
}
