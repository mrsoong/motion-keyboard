package com.example.motionkey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.motionkey.utilities.Cursor;
import com.example.motionkey.utilities.NoiseFilter;

import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
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

    Cursor cursor;

    ToggleButton low_btn;
    ToggleButton medium_btn;
    ToggleButton high_btn;
    ToggleButton ok_btn;
    ToggleButton cancel_btn;
    ToggleButton default_btn;

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

        //initialize cursor by finding it in the initialized xml above
        cursor = new Cursor((TextView) findViewById(R.id.settings_cursor));

        //begin listening to the sensors
        mSensorManager.registerListener(this, mSensorMagneticField, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorAccelerometer, mSensorManager.SENSOR_DELAY_FASTEST);

        // Attach listeners to buttons
        low_btn = (ToggleButton) findViewById(R.id.settings_sensitivity_low);
        medium_btn = (ToggleButton) findViewById(R.id.settings_sensitivity_medium);
        high_btn = (ToggleButton) findViewById(R.id.settings_sensitivity_high);
        ok_btn = (ToggleButton) findViewById(R.id.settings_ok);
        cancel_btn = (ToggleButton) findViewById(R.id.settings_cancel);
        default_btn = (ToggleButton) findViewById(R.id.settings_default);

        low_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(0.50f);
                low_btn.setChecked(true);
                medium_btn.setChecked(false);
                high_btn.setChecked(false);
            }
        });
        medium_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(0.50f);
                low_btn.setChecked(false);
                medium_btn.setChecked(true);
                high_btn.setChecked(false);
            }
        });
        high_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(0.75f);
                low_btn.setChecked(false);
                medium_btn.setChecked(false);
                high_btn.setChecked(true);
            }
        });

        high_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(0.75f);
                low_btn.setChecked(false);
                medium_btn.setChecked(false);
                high_btn.setChecked(true);
            }
        });


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
        updateCursor();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not needed at the moment
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stop listening to the sensors
        mSensorManager.unregisterListener(this);
    }

    public void updateCursor() {
        cursor.updateCursorPosition(this.mSensorManager, this.mGravityData, this.mGeomagneticData,
                this.originalOrientation, this.adjustedOrientation, this.adjustmentAmount,
                this.mNoiseFilter, this.mAngleLimit);


    }
}
