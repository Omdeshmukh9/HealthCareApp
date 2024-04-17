package com.example.myapplication.patient;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Chat.ChatActivity;
import com.example.myapplication.HomeFragment;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NotificationView;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    Button chatgpt;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        setSupportActionBar(findViewById(R.id.toolbar));
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

//        chatgpt = findViewById(R.id.button_skin_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



//        chatgpt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(HomeActivity.this,ChatActivity.class);
//            }
//        });



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_logout) {
                    FirebaseAuth.getInstance().signOut();
                    finish();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                } else {
                    selectDrawerItem(menuItem);
                }
                return false;
            }
        });

        navigationView.getMenu().getItem(0).setChecked(true);

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addNotification();

    }

    public void selectDrawerItem(MenuItem menuItem) {
        Class fragmentClass;
        Fragment fragment = null;
        if (menuItem.getItemId() == R.id.nav_appointment)
            fragmentClass = AppointmentBookingFragment.class;
        else if (menuItem.getItemId() == R.id.nav_reminder)
            fragmentClass = ReminderFragment.class;
        else if (menuItem.getItemId() == R.id.nav_home)
            fragmentClass = HomeFragment.class;
        else {
            fragmentClass = HomeFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeFragment(fragment);
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    private void createNotificationChannel() {
        CharSequence name = "Water Reminder";
        String description = "Used to show water reminders.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.water_notification_channel_id), name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private void addNotification() {
        createNotificationChannel();
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.appointment_icon)
                        .setContentTitle("Water Reminder")
                        .setContentText("Drink a glass of water.")
                        .setChannelId(getResources().getString(R.string.water_notification_channel_id))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(this, NotificationView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("title", "This is a notification Title");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
