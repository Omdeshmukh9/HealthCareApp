package com.example.myapplication.FallDetection;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class FallTest extends AppCompatActivity implements SensorEventListener {
    Sensor sensor;
    SensorManager sensorManager;
    CountDownTimer timer;
    double sum;
    int count;
    int runCount, repeatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        runCount = 1;
        repeatCount = 0;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        runCount++;
        if (runCount == 2) {
            timer = new CountDownTimer(30000, 1000) {

                public void onTick(long millisUntilFinished) {
                    sum = Math.round(Math.sqrt(Math.pow(event.values[0], 2)
                            + Math.pow(event.values[1], 2)
                            + Math.pow(event.values[2], 2)));
                    repeatCount++;
                    Log.e("Check sum", String.valueOf(sum));
                    if ((sum >= 9.80) && (sum <= 11.0)) {
                        count++;
                    }
                }

                public void onFinish() {
                    String c = String.valueOf(count);
                    String rep = String.valueOf(repeatCount);
                    Log.e("Count is", c);
                    Log.e("Loop count", rep);
                    Intent intent = new Intent();
                    intent.putExtra("count_value", count);
                    setResult(2, intent);
                    finish();
                }
            }.start();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
