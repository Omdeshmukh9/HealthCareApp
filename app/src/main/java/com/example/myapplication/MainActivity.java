package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.myapplication.VideoChat.callActivity;
import com.example.myapplication.doctor.DoctorHomeActivity;
import com.example.myapplication.patient.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MainActivity extends AppCompatActivity {

    Button login , register ;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mInstance == null) {
            mInstance = getApplicationContext();
        }
        firebaseAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginButton);
        register = findViewById(R.id.registerButton);
        if(firebaseAuth.getCurrentUser()!=null){
            FirebaseFirestore.getInstance().collection("users_doctors")
                            .whereEqualTo("doctor_uid",firebaseAuth.getCurrentUser().getUid().toString())
                                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        if(task.getResult().getDocuments().size()>0){
                                                            finish();
                                                            startActivity(new Intent(MainActivity.this, DoctorHomeActivity.class));
                                                        }else{
                                                            finish();
                                                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                        }
                                                    }
                                                }
                                            });
        }

        // Set OnClickListener to the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, Login.class);
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

    }

    private static Context mInstance;


    public static Context getInstance() {
        return mInstance;
    }
}
