package com.example.myapplication.medicine;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.NotificationView;
import com.example.myapplication.R;
import com.example.myapplication.medicine.PopupActivity;

import timber.log.Timber;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Notification","Inside AlarmReceiver");

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.appointment_icon)
                        .setContentTitle("Water Reminder")
                        .setContentText("Drink a glass of water.")
                        .setChannelId(context.getResources().getString(R.string.water_notification_channel_id))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(context, NotificationView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("title", "This is a notification Title");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());


//        String medicineName = intent.getStringExtra("medicine_name");

        // Show popup screen
//        Intent popupIntent = new Intent(context, PopupActivity.class);
//        popupIntent.putExtra("medicine_name", medicineName);
//        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(popupIntent);
//
//        // Send notification
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (notificationManager != null) {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id")
//                    .setSmallIcon(R.drawable.notification_icon)
//                    .setContentTitle("Take Your Medicine")
//                    .setContentText("It's time to take " + medicineName)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .setAutoCancel(true);
//
//            notificationManager.notify(NOTIFICATION_ID, builder.build());
//        }
    }
}
