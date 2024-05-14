package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {

    private FirebaseAuth mAuth; // Firebase Authentication instance
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView stepCountTextView;
    private int stepCount = 0;
    private static final String TAG = "StepCounterActivity";
    private static final int STEP_THRESHOLD = 10; // Adjust threshold as needed
    private long lastStepTime = 0;
    private ToggleButton toggleButton; // ToggleButton to start or stop the step counter service

    // Shared Preferences
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "StepCounterPrefs";
    private static final String KEY_STEP_COUNT = "stepCount";
    private static final String KEY_LAST_STEP_TIME = "lastStepTime";
    private static final String KEY_LAST_RESET_DATE = "lastResetDate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            // Get the accelerometer sensor
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Initialize TextView to display step count
        stepCountTextView = findViewById(R.id.stepCountTextView);

        // Initialize ToggleButton
        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Start step counter
                registerSensorListener();
            } else {
                // Stop step counter
                unregisterSensorListener();
            }
        });

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // Reset step counter if a new day has started
        resetStepCounterIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register sensor listener if ToggleButton is checked
        if (toggleButton.isChecked()) {
            registerSensorListener();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister sensor listener
        unregisterSensorListener();
        // Save step count and last step time to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_STEP_COUNT, stepCount);
        editor.putLong(KEY_LAST_STEP_TIME, lastStepTime);
        editor.apply();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update step count when accelerometer values indicate a step
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Calculate total acceleration magnitude
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double magnitude = Math.sqrt(x * x + y * y + z * z);

            // Check for step based on peak detection
            if (magnitude > STEP_THRESHOLD && isStepPossible()) {
                stepCount++;
                stepCountTextView.setText("Steps: " + stepCount);
                lastStepTime = System.currentTimeMillis();

                // Update Firestore with the new step count
                updateFirestoreStepCount(stepCount);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    private void updateFirestoreStepCount(int stepCount) {
        // Access the Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            // Use the userEmail for updating Firestore or any other operation
            Log.d(TAG, "User Email: " + userEmail);

            // Create StepCountData object with current step count and date
            StepCountData data = new StepCountData(stepCount, new Date());

            // Update Firestore with the new step count and date
            db.collection("stepCounts").document(userEmail)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Step count updated successfully");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating step count", e);
                        }
                    });
        }
    }

    private boolean isStepPossible() {
        long currentTime = System.currentTimeMillis();
        // To prevent counting multiple steps within a short time window, we enforce a minimum time between steps
        return currentTime - lastStepTime > 500; // Adjust time window as needed
    }

    private void resetStepCounterIfNeeded() {
        // Get the last reset date from shared preferences
        long lastResetDate = sharedPreferences.getLong(KEY_LAST_RESET_DATE, 0);
        // Get the current date
        long currentDate = Calendar.getInstance().getTimeInMillis();
        // If the current date is different from the last reset date, reset the step counter
        if (currentDate > lastResetDate) {
            // Reset step count and last step time
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_STEP_COUNT, 0);
            editor.putLong(KEY_LAST_STEP_TIME, 0);
            // Update the last reset date to the current date
            editor.putLong(KEY_LAST_RESET_DATE, currentDate);
            editor.apply();
            // Reset displayed step count
            stepCount = 0;
            stepCountTextView.setText("Steps: " + stepCount);
        }
    }

    private void registerSensorListener() {
        if (sensorManager != null && accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void unregisterSensorListener() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}
