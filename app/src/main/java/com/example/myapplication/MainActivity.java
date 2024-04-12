package com.example.myapplication;

import static com.example.myapplication.R.id.appointmentButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button login , register , appointment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        appointment = findViewById(R.id.appointmentButton);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            finish();
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }

        // Set OnClickListener to the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, Login.class);
                finish();
                // Start the LoginActivity
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AppointmentBooking.class);
                startActivity(intent);
            }
        });
    }
}
