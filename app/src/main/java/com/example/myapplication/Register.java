package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.doctor.DoctorHomeActivity;
import com.example.myapplication.patient.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextUsername ,editTextRePassword;
    private Button buttonRegister ,login;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String[] item = {"Select type","Doctor", "Patient"};

    String[] itemGender = {"Gender","Male","Female"};



    Spinner dropdown,genderDrop;

    ArrayAdapter<String> adapterItems,adapterGenderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        editTextUsername = findViewById(R.id.editTextUsername);
        buttonRegister = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.buttonLogin);

        genderDrop = findViewById(R.id.gender);

        dropdown = findViewById(R.id.userCategory);
        adapterItems = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,item);
        adapterGenderItems = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,itemGender);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        dropdown.setAdapter(adapterItems);
        genderDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        genderDrop.setAdapter(adapterGenderItems);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid;
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String rePassword = editTextRePassword.getText().toString().trim();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Input Correct Email");
                    valid = false;
                } else {
                    editTextEmail.setError(null);
                    valid = true;
                }

                if(password.length() >= 8) {
                    valid = true;
                    editTextPassword.setError(null);
                } else {
                    valid = false;
                    editTextPassword.setError("Please enter 8 digit password.");
                }

                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    editTextEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    editTextPassword.requestFocus();
                    return;
                }

                if (!rePassword.equals(password)) {
                    editTextRePassword.setError(" Confirm Password is not same");
                    editTextRePassword.requestFocus();
                    return;
                }

                if (editTextUsername.getText().toString().isEmpty()) {
                    editTextUsername.setError("Username is required");
                    editTextUsername.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String,String> map = new HashMap<>();
                                    String username = editTextUsername.getText().toString().trim();
                                    map.put("username",username);
                                    map.put("email",email);
                                    map.put("doctor_uid",mAuth.getUid());
                                    map.put("gender",genderDrop.getSelectedItem().toString());
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                                    mAuth.getCurrentUser().updateProfile(userProfileChangeRequest);
                                    uploadData(map);
                                } else {
                                    Toast.makeText(Register.this, "Failed to register! Please try again later", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }

    private void uploadData(Map<String,String> object){
        CollectionReference userReference;
        Boolean isPatient = dropdown.getSelectedItem().toString().equals("Patient");
        if(isPatient){
            userReference = db.collection("users_patients");
        }else{
            userReference = db.collection("users_doctors");
        }
        userReference
                .document(Objects.requireNonNull(mAuth.getUid()))
                .set(object)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Class resultClass;
                            if(isPatient){
                                resultClass= HomeActivity.class;
                            }else{
                                resultClass= DoctorHomeActivity.class;
                            }
                            startActivity(new Intent(Register.this, resultClass));
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


}
