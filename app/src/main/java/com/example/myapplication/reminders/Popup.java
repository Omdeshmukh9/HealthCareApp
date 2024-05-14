package com.example.myapplication.reminders;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.patient.HomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class Popup extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_popup);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Called when the user clicks the "Yes" button
    public void onYesButtonClicked(View view) {
        // Retrieve medicine name from intent extras
        String medicineName = getIntent().getStringExtra("medicineName");

        // Get current date
        Date currentDate = Calendar.getInstance().getTime();

        // Save data to Firestore
        saveDataToFirestore(medicineName, currentDate);

        stopRingtone();

        // Redirect user to home screen
        redirectToHome();
    }

    private void stopRingtone(){
        Ringtone ringtone = RingtoneManager.getRingtone(this, getIntent().getData());
        ringtone.stop();
    }

    // Called when the user clicks the "No" button
    public void onNoButtonClicked(View view) {
        // Save "No medicine taken" to Firestore
        saveDataToFirestore("No medicine taken", null);

        stopRingtone();

        // Redirect user to home screen
        redirectToHome();
    }

    // Save data to Firestore
    private void saveDataToFirestore(String medicineName, Date currentDate) {
        // Create a new document with a generated ID
        db.collection("medicines")
                .add(new Medication(medicineName, currentDate))
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Redirect user to home screen
    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Finish the current activity
    }
}
