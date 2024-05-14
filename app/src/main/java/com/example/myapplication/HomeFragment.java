package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.myapplication.VideoChat.callActivity;

import com.example.myapplication.VideoChat.callActivity;

import com.example.myapplication.patient.AppointmentBookingFragment;
import com.example.myapplication.patient.HomeActivity;
import com.example.myapplication.patient.ProfileFragment;
import com.example.myapplication.patient.ReminderFragment;
import com.example.myapplication.patient.SkinActivity;
import com.example.myapplication.patient.WaterReminderFragment;
import com.example.myapplication.reminders.Medicine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    CardView userCard , appointmentBooking, medicineReminder,waterReminder, skinActivity, chat, nearHospital , video_call,  falldetection;

    View view;
    TextView homeUsernameDisplay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);
        appointmentBooking = view.findViewById(R.id.appointmentBooking);
        medicineReminder = view.findViewById(R.id.medicineReminder);
        waterReminder = view.findViewById(R.id.waterReminder);
        skinActivity = view.findViewById(R.id.skinActivity);
        chat = view.findViewById(R.id.chatgpt);
        nearHospital = view.findViewById(R.id.nearHospital);
        video_call = view.findViewById(R.id.video_call);
        homeUsernameDisplay = view.findViewById(R.id.home_username_display);
        falldetection =view.findViewById(R.id.fall);
        userCard = view.findViewById(R.id.user);
        FirebaseUser user = ((HomeActivity)getContext()).getFirebaseAuth().getCurrentUser();
        user.reload();

        homeUsernameDisplay.setText ("Welcome "+user.getDisplayName());


        appointmentBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getContext()).changeFragment(AppointmentBookingFragment.newInstance());

            }
        });
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getContext()).changeFragment(ProfileFragment.newInstance());

            }
        });


        nearHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=hospitals");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Payment.class);
                startActivity(intent);
            }
        });

        video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), callActivity.class);
                startActivity(intent);
            }
        });
        medicineReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Medicine.class);
                startActivity(intent);
            }
        });
        falldetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FallDetectionActivity.class);
                startActivity(intent);
            }
        });

        waterReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getContext()).changeFragment(WaterReminderFragment.newInstance());
            }
        });

        skinActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SkinActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
