package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppointmentBooking extends AppCompatActivity {

    Button back;
    EditText time;

    TextView dateView;
    DatePicker date;
    final Calendar myCalendar= Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.appointment_booking);

        back = findViewById(R.id.backButton);
        date = findViewById(R.id.pickdate);
        time = findViewById(R.id.picktime);
        dateView = findViewById(R.id.dateSelect);

        DatePickerDialog.OnDateSetListener datePicker =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();



            }
        };

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
               mTimePicker = new TimePickerDialog(AppointmentBooking.this, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       time.setText( selectedHour + ":" + selectedMinute);
                   }
               }, hour, minute, true);//Yes 24 hour time
               mTimePicker.setTitle("Select Time");
               mTimePicker.show();
           }
       });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppointmentBooking.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateView.setText(dateFormat.format(myCalendar.getTime()));

    }
}