package com.example.dentalmobileapp.Services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dentalmobileapp.R;
import java.util.List;

public class ServiceAdapter  extends RecyclerView.Adapter<ServiceViewHolder>
{
    List<ServiceResponse> services;
    Context context;

    public ServiceAdapter(Context context, List<ServiceResponse> services) {
        this.services = services;
        this.context = context;
    }


    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_services_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceResponse service = services.get(position);
        // TODO: SERVICE IMAGE
        holder.serviceName.setText(services.get(position).getServiceName());
        holder.serviceDescription.setText(services.get(position).getServiceDescription());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
