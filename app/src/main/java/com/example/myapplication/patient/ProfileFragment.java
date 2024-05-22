package com.example.myapplication.patient;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView stepsCount, username, email;
    PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        stepsCount = view.findViewById(R.id.steps_count);
        username = view.findViewById(R.id.profile_username_display);
        email = view.findViewById(R.id.profile_email_display);
        pieChart = view.findViewById(R.id.piechart);

        ((HomeActivity) getContext()).getFirebaseFirestore().collection("users_patients")
                .document(((HomeActivity) getContext()).getFirebaseAuth().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                String steps = document.getString("steps_count");
                                String user = document.getString("username");
                                String userEmail = document.getString("email");

                                if (steps != null) {
                                    stepsCount.setText(steps);
                                }
                                if (user != null) {
                                    username.setText(user);
                                }
                                if (userEmail != null) {
                                    email.setText(userEmail);
                                }

                                pieChart.addPieSlice(new PieModel(
                                        "Medicines Taken",
                                        10,
                                        Color.parseColor("#66BB6A")));
                                pieChart.addPieSlice(new PieModel(
                                        "Medicines Neglected",
                                        4,
                                        Color.parseColor("#EF5350")));
                            } else {
                                Log.d("ProfileFragment", "No such document");
                            }
                        } else {
                            Log.d("ProfileFragment", "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ProfileFragment", "Error getting documents: ", e);
                    }
                });

        return view;
    }
}
