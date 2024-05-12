package com.example.myapplication.medicine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PopupActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        String medicineName = getIntent().getStringExtra("medicine_name");

        // Assume you have two buttons in your popup layout
        Button buttonTaken = findViewById(R.id.button_taken);
        Button buttonIgnored = findViewById(R.id.button_ignored);

        // Set click listeners for buttons
        buttonTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle when user takes medicine
                markMedicineAsTaken(medicineName);
            }
        });

        buttonIgnored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle when user ignores medicine
                finish(); // Close the popup activity
            }
        });
    }

    private void markMedicineAsTaken(String medicineName) {
        // Create a new document with a generated ID
        Map<String, Object> medicine = new HashMap<>();
        medicine.put("name", medicineName);
        medicine.put("status", "taken");

        // Add a new document with a generated ID
        db.collection("medicines")
                .add(medicine)
                .addOnSuccessListener(documentReference -> {
                    finish(); // Close the popup activity
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                });
    }
}
