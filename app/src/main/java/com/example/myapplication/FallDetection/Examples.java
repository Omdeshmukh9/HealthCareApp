package com.example.myapplication.FallDetection;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;

import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class Examples extends AppCompatActivity implements LocationListener {
    TextView values, timetest, title, alert_msg;
    private LocationManager locationManager;
    private String latitude;
    private String longitude;
    private String msg;
    Button alertbutton;
    CountDownTimer timer;
    private AudioManager myAudioManager;
    TextToSpeech t1;
    Ringtone r;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String sms_number, ringtone_value;
    long response_mode, resp_time;
    boolean isPlaying;

    int timer_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isPlaying = true;
        timer_enabled = 0;
        alert_msg = findViewById(R.id.txt_alertHeading);
        title = findViewById(R.id.example_values);
        Typeface tf = Typeface.createFromAsset(getAssets(), "Myfont.ttf");
        title.setTypeface(tf);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sms_number = sharedpreferences.getString("Contact_number", "");
        ringtone_value = sharedpreferences.getString("Ringtone", "");
        response_mode = sharedpreferences.getLong("Response", 00);
        resp_time = response_mode / 60000;
        if ((sms_number == null) || (ringtone_value == null) || (response_mode == 0.0f)) {
            Toast.makeText(this, "Error in reading", Toast.LENGTH_LONG).show();
        } else {
            alert_msg.setText("Click here to Cancel the SMS alert in " + resp_time + " min");
            Uri uri = Uri.parse(ringtone_value);
            r = RingtoneManager.getRingtone(this, uri);
            r.play();

            new Handler().postDelayed(() -> {
                r.stop();
                t1 = new TextToSpeech(getApplicationContext(), status -> {
                    if (status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.UK);
                        speakout();
                    }
                });
            }, 10000);

            alertbutton = findViewById(R.id.btn_sms);
            timer = new CountDownTimer(response_mode, 1000) {
                public void onTick(long millisUntilFinished) {
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    alertbutton.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                    alertbutton.setOnClickListener(v -> {
                        timer.cancel();
                        alertbutton.setText("SMS Alert Cancelled");
                        timer_enabled = 1;
                    });
                }

                public void onFinish() {
                    alertbutton.setText("SMS Alert Activated");
                    String sms_msg = "The sender of this message may have been fallen, it happened in this location" + " " + "http://maps.google.com/maps/@" + latitude + "," + longitude + "/";
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(sms_number, null, sms_msg, null, null);
                        Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    initialize_phone();
                }
            }.start();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, this);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show());
        }
    }

    public void speakout() {
        Log.e("Speech", "In speech module");
        t1.speak("A fall have been detected. Please cancel the SMS alert if you are OK", TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "New Latitude: " + location.getLatitude() + "New Longitude: " + location.getLongitude();
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        latitude = String.valueOf(lat);
        longitude = String.valueOf(longi);
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public String getLatitude() {
        msg = "https://www.google.com/maps/@" + latitude + "," + longitude;
        return latitude;
    }

    public void sms_send(View view) {
        String phone_num = "4166249722";
        String sms_msg = "Please check this" + "http://maps.google.com/maps/@" + latitude + "," + longitude + "/";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone_num, null, sms_msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void initialize_phone() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(getApplicationContext(), "Phone Is Riging", Toast.LENGTH_LONG).show();
                }
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Toast.makeText(getApplicationContext(), "Phone is Currently in A call", Toast.LENGTH_LONG).show();
                    myAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    myAudioManager.setMode(AudioManager.MODE_IN_CALL);
                    myAudioManager.setSpeakerphoneOn(true);
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    Toast.makeText(getApplicationContext(), "phone is neither ringing nor in a call", Toast.LENGTH_LONG).show();
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onBackPressed() {
        if (timer_enabled == 1) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Please cancel the SMS alertbutton to return to main menu", Toast.LENGTH_LONG).show();
        }
    }
}
