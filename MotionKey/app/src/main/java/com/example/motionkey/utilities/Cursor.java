package com.example.motionkey.utilities;

import android.content.Intent;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.motionkey.SettingsActivity;

/**
 * Created by Steven Song on 3/31/2017.
 */

public class Cursor {

    private int curCursorPaddingTop;
    private int curCursorPaddingRight;
    private int curCursorPaddingBottom;
    private int curCursorPaddingLeft;
    private int curCursorWidth;
    private int curCursorHeight;

    private TextView mCursor;

    public Cursor(TextView initialCursor) {
        this.mCursor = initialCursor;
    }

    public void updateCursorPosition(SensorManager mSensorManager, float[] mGravityData, float[] mGeomagneticData,
                                     float[] originalOrientation, float[] adjustedOrientation, float[] adjustmentAmount,
                                     NoiseFilter mNoiseFilter, int mAngleLimit) {
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
                // updateKeyboard() was called
                float[] orientationDelta = new float[3];
                orientationDelta[0] = originalOrientation[0] - mNoiseFilter.getOldestMeasurement()[0];
                orientationDelta[1] = originalOrientation[1] - mNoiseFilter.getOldestMeasurement()[1];
                orientationDelta[2] = originalOrientation[2] - mNoiseFilter.getOldestMeasurement()[2];
                if (Math.sqrt((Math.pow(orientationDelta[0], 2) + Math.pow(orientationDelta[1], 2) + Math.pow(orientationDelta[2], 2))) > 5.0) {
                    // Update the history of orientations
                    mNoiseFilter.setOldestMeasurement(originalOrientation);
                } else {
                    // Nothing changes
                }

                //adjustedOrientation's index 2 is top bottom position. Positive is bottom.
                //index 1 is left right position. Positive is right
                float[] smoothedOrientation = mNoiseFilter.getFilteredMeasurement();
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
            }
        }

    }

    public void logCursor () {
        Log.d("keyboard", "cursor: "+"padding left: "+curCursorPaddingLeft);
        Log.d("keyboard", "cursor: "+"padding right: "+curCursorPaddingRight);
        Log.d("keyboard", "cursor: "+"padding top: "+curCursorPaddingTop);
        Log.d("keyboard", "cursor: "+"padding bottom: "+curCursorPaddingBottom);
    }

    public void updatePadding(int left, int top, int right, int bottom) {
        this.mCursor.setPadding(left, top, right, bottom);
    }

    public int getPaddingLeft() {
        return this.curCursorPaddingLeft;
    }

    public int getPaddingRight() {
        return this.curCursorPaddingRight;
    }

    public int getPaddingBottom() {
        return this.curCursorPaddingBottom;
    }

    public int getPaddingTop() {
        return this.curCursorPaddingTop;
    }
}
