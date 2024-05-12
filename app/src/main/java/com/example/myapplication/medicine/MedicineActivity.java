package com.example.myapplication.medicine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class MedicineActivity extends AppCompatActivity {
    EditText editTextMedicineName, editTextDosage, editTextFrequency;
    Button buttonAddMedicine, buttonTakeMedicine;
    Map<String, Medicine> medicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        editTextMedicineName = findViewById(R.id.editTextMedicineName);
        editTextDosage = findViewById(R.id.editTextDosage);
        editTextFrequency = findViewById(R.id.editTextFrequency);
        buttonAddMedicine = findViewById(R.id.buttonAddMedicine);
        buttonTakeMedicine = findViewById(R.id.buttonTakeMedicine);

        medicines = new HashMap<>();

        buttonAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextMedicineName.getText().toString();
                String dosage = editTextDosage.getText().toString();
                int frequency = Integer.parseInt(editTextFrequency.getText().toString());

                if (!name.isEmpty() && !dosage.isEmpty() && frequency > 0) {
                    addMedicine(name, dosage, frequency);
                } else {
                    Toast.makeText(MedicineActivity.this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show();

                }
            }
        });

        buttonTakeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextMedicineName.getText().toString();
                takeMedicine(name);
            }
        });
    }

    private void addMedicine(String name, String dosage, int frequency) {
        medicines.put(name, new Medicine(name, dosage, frequency));
        Toast.makeText(this, "Medicine added successfully.", Toast.LENGTH_SHORT).show();
    }

    private void takeMedicine(String name) {
        Medicine medicine = medicines.get(name);
        if (medicine != null) {
            medicine.setLastTaken(System.currentTimeMillis());
            Toast.makeText(this, name + " taken.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Medicine not found.", Toast.LENGTH_SHORT).show();
        }
    }
}
