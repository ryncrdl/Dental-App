package com.example.dentalmobileapp.Doctors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServiceResponse;
import com.example.dentalmobileapp.Services.ServiceViewHolder;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorViewHolder> {
    private List<DoctorResponse> doctors;
    private Context context;

    public DoctorAdapter(Context context, List<DoctorResponse> doctors) {
        this.doctors = doctors;
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctors_list, parent, false);
        return new DoctorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        DoctorResponse doctor = doctors.get(position);
        holder.doctorName.setText("Doc. " + doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName());
        holder.doctorContact.setText(doctor.getDoctorContact());
        holder.doctorEmail.setText(doctor.getDoctorEmail());
        holder.doctorAddress.setText(doctor.getDoctorAddress());
    }
    @Override
    public int getItemCount() {
        return doctors.size();
    }
}
