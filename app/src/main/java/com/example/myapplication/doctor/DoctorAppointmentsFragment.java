package com.example.myapplication.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorAppointmentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorAppointmentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    public DoctorAppointmentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DoctorAppointmentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorAppointmentsFragment newInstance() {
        DoctorAppointmentsFragment fragment = new DoctorAppointmentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private FirebaseFirestore db;
    List<DoctorAppointmentItemList> doctorAppointmentItemList = new ArrayList<>();
    RecyclerView recyclerView;
    DoctorAppointmentAdapter doctorAppointmentAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_appointments_doctor, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        Log.d("Appointments","Inside");
        db = ((DoctorHomeActivity)getContext()).getFirebaseFirestore();
        String doctorID = ((DoctorHomeActivity)getContext()).getFirebaseAuth().getCurrentUser().getUid();

        Log.d("Appointments",doctorID);
        List<String> doctorIds = new ArrayList<>();
        doctorIds.add(doctorID);
        db.collection("appointments")
                .whereIn("doctor_uid", doctorIds)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<DocumentSnapshot> documentSnapshotList = task.getResult().getDocuments();
                            for (DocumentSnapshot d:
                                 documentSnapshotList) {
                                DoctorAppointmentItemList doctor = new DoctorAppointmentItemList();
                                doctor.setAppointmentDate(d.get("appointment_date").toString());
                                doctor.setAppointmentMode(d.get("appointment_mode").toString());
                                doctor.setAppointmentTime(d.get("appointment_time").toString());
                                doctor.setAppointmentType(d.get("appointment_type").toString());
                                doctor.setPatientName(d.get("patient_name").toString());
                                doctorAppointmentItemList.add(doctor);


                            }

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            doctorAppointmentAdapter = new DoctorAppointmentAdapter(getContext(), doctorAppointmentItemList);
                            recyclerView.setAdapter(doctorAppointmentAdapter);
                            Log.d("Appointments", String.valueOf(documentSnapshotList.size()));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Appointments",e.toString());
                    }
                });

        return view;
    }
}