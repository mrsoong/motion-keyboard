package com.example.motionkey;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.motionkey.utilities.NoiseFilter;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {
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
    private NoiseFilter mNoiseFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Disable Android's auto screen rotation
        Settings.System.putInt(
                getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION,
                0
        );

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorMagneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //initialize adjustment amount of orientation degrees to zero
        Arrays.fill(adjustmentAmount, 0);
        //noise filter to smooth cursor movement
        this.mNoiseFilter = new NoiseFilter(20, 0.75f, 3);


    }
}
