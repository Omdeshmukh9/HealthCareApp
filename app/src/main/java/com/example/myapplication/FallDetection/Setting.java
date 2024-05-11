package com.example.myapplication.FallDetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Setting extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String contactNumber, chosenRingtone, responseMode;
    long responseTime;
    TextView sampleText, settingHeading;
    final long[] spinnerValues = {180000, 120000, 60000};
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingHeading = findViewById(R.id.Settings_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Myfont.ttf");
        settingHeading.setTypeface(typeface);

        Spinner spinner = findViewById(R.id.response_spinner);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        categories.add("Low (3 minutes)");
        categories.add("Medium (2 minutes)");
        categories.add("High (1 minute)");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void selectRingtone(View view) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handling onActivityResult
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveSettings(View view) {
        if (chosenRingtone == null || contactNumber == null || responseMode == null) {
            Toast.makeText(getApplicationContext(), "Please select all setting options", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Contact_number", contactNumber);
            editor.putString("Ringtone", chosenRingtone);
            editor.putLong("Response", responseTime);
            boolean flag = editor.commit();
            if (flag) {
                Toast.makeText(getApplicationContext(), "Settings Added", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        responseMode = parent.getItemAtPosition(position).toString();
        responseTime = spinnerValues[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public void selectPhoneNumber(View view) {
        Uri uri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 4);
    }
}
