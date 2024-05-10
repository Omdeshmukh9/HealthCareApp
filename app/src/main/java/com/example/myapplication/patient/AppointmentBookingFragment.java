package com.example.myapplication.patient;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.HomeFragment;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppointmentBookingFragment extends Fragment {

    FirebaseFirestore db;

    Button submit;
    EditText time;

    TextView dateView;
    RadioGroup appointment_type, appointment_mode;

    DatePicker date;
    final Calendar myCalendar= Calendar.getInstance();
    View view;
    Spinner doctorDropDown;

    ArrayAdapter<String> doctorArrayAdapter;

    Map<String,String> doctorMap;

    public AppointmentBookingFragment() {
        // Required empty public constructor
    }

    public static AppointmentBookingFragment newInstance() {
        AppointmentBookingFragment fragment = new AppointmentBookingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // handle arguments if any
        }

        db = FirebaseFirestore.getInstance();
        doctorMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_booking, container, false);
        date = view.findViewById(R.id.pickdate);
        submit = view.findViewById(R.id.submitButton);
        time = view.findViewById(R.id.picktime);
        dateView = view.findViewById(R.id.dateSelect);
        doctorDropDown = view.findViewById(R.id.select_doctor_dropdown);
        appointment_type = view.findViewById(R.id.rd1);
        appointment_mode = view.findViewById(R.id.rd2);

        date.setMinDate(myCalendar.getTimeInMillis());

        date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateView.setText(dayOfMonth + "/" +(monthOfYear + 1) + "/" + year);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        getDoctorList();

        return view;
    }

    void getDoctorList(){
        List<String> items = new ArrayList<>();
        db.collection("users_doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                            doctorMap.clear();
                            for (DocumentSnapshot d: documentSnapshotList) {
                                doctorMap.put("Dr. "+d.get("username").toString(),d.getId());
                                items.add("Dr. "+d.get("username").toString());
                            }
                            doctorArrayAdapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,items);
                            doctorDropDown.setAdapter(doctorArrayAdapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    void uploadData(){
        String type = ((RadioButton)view.findViewById(appointment_type.getCheckedRadioButtonId())).getText().toString();
        String mode = ((RadioButton)view.findViewById(appointment_mode.getCheckedRadioButtonId())).getText().toString();
        String date = dateView.getText().toString();
        String t = time.getText().toString();
        String doctor = doctorMap.get(doctorDropDown.getSelectedItem().toString());
        String patientId = ((HomeActivity)getContext()).getFirebaseAuth().getUid();

        // Get logged-in user's email
        String patientEmail = ((HomeActivity) getContext()).getFirebaseAuth().getCurrentUser().getEmail();

        Map<String,String> map = new HashMap<>();
        map.put("appointment_type",type);
        map.put("appointment_mode",mode);
        map.put("appointment_date",date);
        map.put("appointment_time",t);
        map.put("doctor_uid",doctor);
        map.put("patient_email", patientEmail);

        db.collection("users_patients")
                .document(Objects.requireNonNull(patientId))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            map.put("patient_name",task.getResult().get("username").toString());
                            db.collection("appointments")
                                    .add(map)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            Class fragmentClass;
                                            if(task.isSuccessful()){
                                                Toast.makeText(getActivity(), "Appointment is Booked", Toast.LENGTH_SHORT).show();
                                                ((HomeActivity)getContext()).changeFragment(HomeFragment.newInstance());
                                            }else{
                                                Toast.makeText(getActivity(), "Appointment Booking Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Appointment",e.toString());
                                        }
                                    });
                        }
                    }
                });
    }
}
