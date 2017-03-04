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
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;

import static android.content.Context.SENSOR_SERVICE;


/**
 * The main input method view layout. Holds everything together.
 */
public class MotionKeyKeyboardView extends RelativeLayout {

    public MotionKeyKeyboardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
