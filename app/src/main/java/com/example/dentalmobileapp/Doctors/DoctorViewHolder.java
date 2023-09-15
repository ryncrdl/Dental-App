package com.example.dentalmobileapp.Doctors;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalmobileapp.R;

public class DoctorViewHolder extends RecyclerView.ViewHolder
{
    TextView doctorName, doctorContact, doctorEmail, doctorAddress;
    public DoctorViewHolder(@NonNull View doctorView){
        super(doctorView);
        doctorName = doctorView.findViewById(R.id.doctor_name);
        doctorContact = doctorView.findViewById(R.id.doctor_contact);
        doctorEmail = doctorView.findViewById(R.id.doctor_email);
        doctorAddress = doctorView.findViewById(R.id.doctor_address);
    }
}
