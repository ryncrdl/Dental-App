package com.example.dentalmobileapp.Services;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dentalmobileapp.R;

public class ServiceViewHolder extends RecyclerView.ViewHolder
{
    public ImageView serviceImage;
    TextView serviceName, serviceDescription;
    public ServiceViewHolder(@NonNull View serviceView){
        super(serviceView);
        serviceImage = serviceView.findViewById(R.id.services_image);
        serviceName = serviceView.findViewById(R.id.service_name);
        serviceDescription = serviceView.findViewById(R.id.service_description);
    }
}
