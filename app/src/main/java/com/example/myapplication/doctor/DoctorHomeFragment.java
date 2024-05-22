package com.example.myapplication.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Chat.chat;
import com.example.myapplication.R;
import com.example.myapplication.VideoChat.VideoActivity;
import com.example.myapplication.patient.HomeActivity;
import com.example.myapplication.patient.SkinActivity;
import com.example.myapplication.patient.WaterReminderFragment;
import com.example.myapplication.reminders.Medicine;
import com.google.ai.client.generativeai.Chat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public DoctorHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DoctorHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorHomeFragment newInstance() {
        DoctorHomeFragment fragment = new DoctorHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }


    CardView appointments, videoCall,pactientReviews , waterReminder ,chatbot , skinActivity;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_home, container, false);
        appointments = view.findViewById(R.id.appointments);
        videoCall = view.findViewById(R.id.videocall);
        pactientReviews = view.findViewById(R.id.patientReviews);
        waterReminder = view.findViewById(R.id.waterReminder);
        chatbot = view.findViewById(R.id.chatgpt);
        skinActivity = view.findViewById(R.id.skinActivity);


        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DoctorHomeActivity) getContext()).changeFragment(DoctorAppointmentsFragment.newInstance());
            }
        });

        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VideoActivity.class);
                startActivity(intent);
            }
        });

        skinActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SkinActivity.class);
                startActivity(intent);
            }
        });

        pactientReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Medicine.class);
                startActivity(intent);
            }
        });

        waterReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DoctorHomeActivity) getContext()).changeFragment(WaterReminderFragment.newInstance());
            }
        });

        chatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), chat.class);
                startActivity(intent);
            }
        });



        return view;
        }
    }