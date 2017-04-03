package com.example.motionkey;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.motionkey.utilities.Cursor;
import com.example.motionkey.utilities.NoiseFilter;

import java.util.Arrays;

import static android.view.MotionEvent.ACTION_DOWN;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {
    private SettingsView mSettingsView;
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
    private float startingSensitivity;

    Cursor cursor;

    LinearLayout settingsArea;
    View view;

    Button low_btn;
    Button medium_btn;
    Button high_btn;
    Button ok_btn;
    Button cancel_btn;
    Button default_btn;

    // Values/constants for different sensitivity settings
    private final float low_sens = 0.40f;
    private final float med_sens = 0.50f;
    private final float high_sens = 0.75f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        LayoutInflater inflater = getLayoutInflater();
//        mSettingsView = (SettingsView) getLayoutInflater().inflate(R.layout.activity_settings, null);
        mSettingsView = (SettingsView) findViewById(R.id.settings_parent_layout);
//        mSettingsView test = new SettingsView(inflater.getContext(),
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
        mNoiseFilter = new NoiseFilter(20, 3);
        startingSensitivity = mNoiseFilter.getSensitivity();

        settingsArea = (LinearLayout) findViewById(R.id.settings_area);

        //initialize cursor by finding it in the initialized xml above
        TextView cursorView = (TextView) findViewById(R.id.settings_cursor);
        cursor = new Cursor(cursorView);

        //begin listening to the sensors
        mSensorManager.registerListener(this, mSensorMagneticField, mSensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mSensorAccelerometer, mSensorManager.SENSOR_DELAY_FASTEST);

        // Attach listeners to buttons
        low_btn = (Button) findViewById(R.id.settings_sensitivity_low);
        medium_btn = (Button) findViewById(R.id.settings_sensitivity_medium);
        high_btn = (Button) findViewById(R.id.settings_sensitivity_high);
        ok_btn = (Button) findViewById(R.id.settings_ok);
        cancel_btn = (Button) findViewById(R.id.settings_cancel);
        default_btn = (Button) findViewById(R.id.settings_default);

        ok_btn.setText("Ok");
        cancel_btn.setText("Cancel");
        default_btn.setText("Default");

        low_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(low_sens);
//                low_btn.setChecked(true);
//                medium_btn.setChecked(false);
//                high_btn.setChecked(false);
            }
        });
        medium_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(med_sens);
//                low_btn.setChecked(false);
//                medium_btn.setChecked(true);
//                high_btn.setChecked(false);
            }
        });
        high_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NoiseFilter.setSensitivity(high_sens);
//                low_btn.setChecked(false);
//                medium_btn.setChecked(false);
//                high_btn.setChecked(true);
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mNoiseFilter.setSensitivity(startingSensitivity);
                onBackPressed();
            }
        });

        default_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mNoiseFilter.setSensitivity(med_sens);
                onBackPressed();
            }
        });
//        MotionKeyInteractiveElement mMotionKeyView = new MotionKeyInteractiveElement(view);
//        String key = mMotionKeyView.getMotionKeyElements().updateCursorPosition(mCursorPosition);
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
    protected void onResume()
    {
        super.onResume();

        ok_btn.setText("Ok");
        cancel_btn.setText("Cancel");
        default_btn.setText("Default");

        if (mNoiseFilter.getSensitivity() == low_sens) {
//            low_btn.setChecked(true);
        }
        else if (mNoiseFilter.getSensitivity() == med_sens) {
//            medium_btn.setChecked(true);
        }
        else {
//            high_btn.setChecked(true);
        }
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
        //notify observer
        //Note: Still WIP
        if (mSettingsView.isMotionKeyKeyboardElementsFound()) {
            int[] mCursorPosition = new int[2];
            mCursorPosition[0] = (mSettingsView.getWidth()) / 2 - cursor.getPaddingRight() / 2 + cursor.getPaddingLeft() / 2;
            mCursorPosition[1] = (mSettingsView.getHeight()) / 2 - cursor.getPaddingBottom() / 2 + cursor.getPaddingTop() / 2;
            String key = mSettingsView.getMotionKeyElements().updateCursorPosition(mCursorPosition);
            Log.d("Mark setting", key + "");
            if (key != null) {
                switch (key) {
                    case "Low":
                        NoiseFilter.setSensitivity(low_sens);
                        break;
                    case "Medium":
                        NoiseFilter.setSensitivity(med_sens);
                        break;
                    case "High":
                        NoiseFilter.setSensitivity(med_sens);
                        break;
                    case "Ok":
                        onBackPressed();
                        break;
                    case "Cancel":
                        mNoiseFilter.setSensitivity(startingSensitivity);
                        onBackPressed();
                        break;
                    case "Default":
                        mNoiseFilter.setSensitivity(med_sens);
                        onBackPressed();
                        break;
                }
            }

        }

    }
}
