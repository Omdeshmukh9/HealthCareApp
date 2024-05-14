package com.example.myapplication.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaterReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaterReminderFragment extends Fragment {

    public WaterReminderFragment() {
        // Required empty public constructor
    }

    public static WaterReminderFragment newInstance() {
        WaterReminderFragment fragment = new WaterReminderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button setAlarm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_reminder, container, false);
        setAlarm = view.findViewById(R.id.button_set_alarm);

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getContext()).addNotification(6000);
            }
        });

        return view;
    }
}