package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class NotificationView extends AppCompatActivity {

    TextView title,desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_view);

        title = findViewById(R.id.notification_title);
        desc = findViewById(R.id.notification_description);

        Log.d("Notification", Objects.requireNonNull(getIntent().getStringExtra("title")));
        String message=getIntent().getStringExtra("title");
        title.setText(message);
    }
}