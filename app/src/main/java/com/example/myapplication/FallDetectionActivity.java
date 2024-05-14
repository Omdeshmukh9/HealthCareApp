package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class FallDetectionActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private static final String TAG = "FallDetectionActivity";
    private static final float FALL_THRESHOLD = 30.0f; // Adjust threshold as needed
    private Handler handler;
    private boolean emergencySMSSent = false;
    private ToggleButton toggleButton;
    private EditText phoneNumberEditText;
    private Button addButton;
    private String phoneNumber;
    private LocationManager locationManager;
    private Location currentLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detection);

        // Initialize sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            // Get the accelerometer sensor
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        handler = new Handler();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        toggleButton = findViewById(R.id.toggleButton);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store the phone number from EditText
                phoneNumber = phoneNumberEditText.getText().toString().trim();
                Toast.makeText(FallDetectionActivity.this, "Phone number added: " + phoneNumber, Toast.LENGTH_SHORT).show();
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Enable fall detection service
                    registerSensorListener();
                } else {
                    // Disable fall detection service
                    unregisterSensorListener();
                }
            }
        });

        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void registerSensorListener() {
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(this, "Fall detection service enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Accelerometer sensor not available", Toast.LENGTH_SHORT).show();
            toggleButton.setChecked(false);
        }
    }

    private void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "Fall detection service disabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Request location updates when activity resumes
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Start listening for location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates when activity pauses
        locationManager.removeUpdates(this);
        unregisterSensorListener();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Detect fall based on accelerometer values
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double accelerationMagnitude = Math.sqrt(x * x + y * y + z * z);

            // Check for fall based on sudden change in acceleration
            if (accelerationMagnitude > FALL_THRESHOLD && !emergencySMSSent) {
                // Fall detected, trigger alert
                triggerFallAlert();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used in this example
    }

    private void triggerFallAlert() {
        // Implement logic to trigger alert (e.g., display dialog, play sound, send notification)
        // For example, you can display an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fall Detected");

        // Set the image inside the AlertDialog
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.fall_detected_image);
        builder.setView(imageView);

        builder.setMessage("Are you okay?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle user response (e.g., acknowledge the fall)
                dialog.dismiss();
                // Cancel sending emergency SMS if user responds before the timer expires
                handler.removeCallbacksAndMessages(null);
                emergencySMSSent = false;
            }
        });

        // Add a countdown timer inside the AlertDialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final int countdownSeconds = 5 * 60; // 5 minutes
        final Handler countdownHandler = new Handler();
        final Runnable countdownRunnable = new Runnable() {
            int secondsLeft = countdownSeconds;

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    alertDialog.setMessage("Are you okay? (" + secondsLeft + " seconds remaining)");
                    secondsLeft--;
                    countdownHandler.postDelayed(this, 1000); // Update every second
                } else {
                    // Time's up, send emergency SMS
                    alertDialog.dismiss();
                    sendEmergencySMS();
                    emergencySMSSent = true;
                }
            }
        };
        countdownHandler.post(countdownRunnable);

        // Vibrate the device
        vibrateDevice();
    }

    private void vibrateDevice() {
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            // Deprecated in API 26
            vibrator.vibrate(200);
        }
    }

    private void sendEmergencySMS() {
        // Retrieve phone number from EditText
        String message = "Fall detected! Need assistance.";

        // Check if phone number is valid
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Use Android's SMS Manager to send SMS
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            // Notify the user that the SMS has been sent
            Toast.makeText(this, "Emergency SMS sent to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            // Show error message or handle accordingly
            Toast.makeText(this, "Please add a phone number", Toast.LENGTH_SHORT).show();
        }
    }

    // LocationListener methods
    @Override
    public void onLocationChanged(Location location) {
        // Update current location when it changes
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not used in this example
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Not used in this example
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Not used in this example
    }
}
