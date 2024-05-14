package com.example.myapplication.reminders;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import com.example.myapplication.R;
import com.example.myapplication.reminders.AlarmReceiver;

public class Medicine extends AppCompatActivity {
    TimePicker alarmTimePicker;
    EditText medicineNameEditText;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmTimePicker = findViewById(R.id.timePicker);
        medicineNameEditText = findViewById(R.id.medicineNameEditText);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(Medicine.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("medicineName", medicineNameEditText.getText().toString());
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            findViewById(R.id.alarmCardView).setVisibility(View.VISIBLE); // Show CardView
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(Medicine.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
            findViewById(R.id.alarmCardView).setVisibility(View.GONE); // Hide CardView
        }
    }
}
