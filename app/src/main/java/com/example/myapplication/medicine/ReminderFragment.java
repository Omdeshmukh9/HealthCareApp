package com.example.myapplication.medicine;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Calendar;

public class ReminderFragment extends Fragment {
    EditText editTextMedName, editTextDoseQuantity;
    Spinner spinnerDoseUnits;
    CheckBox everyDayCheckbox;
    CheckBox[] dayCheckBoxes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Initialize UI components
        editTextMedName = rootView.findViewById(R.id.edit_med_name);

        everyDayCheckbox = rootView.findViewById(R.id.every_day);
        dayCheckBoxes = new CheckBox[]{
                rootView.findViewById(R.id.dv_sunday),
                rootView.findViewById(R.id.dv_monday),
                rootView.findViewById(R.id.dv_tuesday),
                rootView.findViewById(R.id.dv_wednesday),
                rootView.findViewById(R.id.dv_thursday),
                rootView.findViewById(R.id.dv_friday),
                rootView.findViewById(R.id.dv_saturday)
        };

        // Set onClickListener for setting alarm
        Button setAlarmButton = rootView.findViewById(R.id.button_set_alarm);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        return rootView;
    }

    private void setAlarm() {
        // Get medicine details from input fields
        String medicineName = editTextMedName.getText().toString();
        // Other details like doseQuantity, selectedDoseUnit...

        // Get the days selected by the user
        boolean[] selectedDays = new boolean[7]; // Array to store selected days (Sunday to Saturday)
        for (int i = 0; i < dayCheckBoxes.length; i++) {
            selectedDays[i] = dayCheckBoxes[i].isChecked();
        }

        // Get the repeat interval for the alarm
        int repeatInterval = everyDayCheckbox.isChecked() ? (int) AlarmManager.INTERVAL_DAY : 0;

        // Get current time
        Calendar calendar = Calendar.getInstance();
        long currentTimeMillis = calendar.getTimeInMillis();

        // Get AlarmManager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Create an intent for the alarm receiver
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        intent.putExtra("medicine_name", medicineName); // Pass medicine name to receiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set alarms for selected days
        for (int i = 0; i < selectedDays.length; i++) {
            if (selectedDays[i]) {
                // Set the alarm for the selected day
                calendar.set(Calendar.DAY_OF_WEEK, i + 1); // i+1 corresponds to Calendar.SUNDAY to Calendar.SATURDAY
                long alarmTimeMillis = calendar.getTimeInMillis();

                // Check if the alarm time is in the past, if so, move it to next week
                if (alarmTimeMillis < currentTimeMillis) {
                    alarmTimeMillis += AlarmManager.INTERVAL_DAY * 7; // Add a week
                }

                // Set the alarm
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTimeMillis, repeatInterval, pendingIntent);
                Toast.makeText(getActivity(), "Alarm set for " + medicineName + " on " + getDayOfWeek(i), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDayOfWeek(int day) {
        String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return daysOfWeek[day];
    }
}
