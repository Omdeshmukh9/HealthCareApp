package com.example.myapplication.medicine;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.medicine.PopupActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicineName = intent.getStringExtra("medicine_name");

        // Show popup screen
        Intent popupIntent = new Intent(context, PopupActivity.class);
        popupIntent.putExtra("medicine_name", medicineName);
        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(popupIntent);

        // Send notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Take Your Medicine")
                    .setContentText("It's time to take " + medicineName)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
