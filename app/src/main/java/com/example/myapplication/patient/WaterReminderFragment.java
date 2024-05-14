package com.example.myapplication.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

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

    String[] item = {"1 min","1 Hour","2 Hours"};

    Spinner dropdown;

    ArrayAdapter<String> adapterItems;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water_reminder, container, false);
        setAlarm = view.findViewById(R.id.button_set_alarm);
        dropdown = view.findViewById(R.id.hours);

        adapterItems = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,item);



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

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getContext()).addNotification(6000);
            }
        });

        return view;
    }
}