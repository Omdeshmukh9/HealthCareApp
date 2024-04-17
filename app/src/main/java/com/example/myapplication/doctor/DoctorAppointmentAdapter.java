package com.example.myapplication.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder>{
    private List<DoctorAppointmentItemList> mData;
    private LayoutInflater mInflater;

    DoctorAppointmentAdapter(Context context, List<DoctorAppointmentItemList> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @NonNull
    @Override
    public DoctorAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.doctor_appointment_item, parent, false);
        return new ViewHolder(view);
    }

    TextView date,time,mode,type,name;
    public class ViewHolder extends RecyclerView.ViewHolder{
        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.appointment_date);
            time = itemView.findViewById(R.id.appointment_time);
            mode = itemView.findViewById(R.id.appointment_mode);
            type = itemView.findViewById(R.id.appointment_type);
            name = itemView.findViewById(R.id.appointment_patient_name);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorAppointmentAdapter.ViewHolder holder, int position) {
        setData(mData.get(position));
    }

    void setData(DoctorAppointmentItemList data){
        name.setText("Patient Name : "+data.patientName);
        date.setText("Date : "+data.appointmentDate);
        time.setText("Time : "+data.appointmentTime);
        mode.setText("Mode : "+data.appointmentMode);
        type.setText("Type : "+data.appointmentType);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
