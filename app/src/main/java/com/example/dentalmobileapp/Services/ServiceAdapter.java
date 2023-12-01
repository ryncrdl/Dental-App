package com.example.dentalmobileapp.Services;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalmobileapp.Home.ViewAnnouncement;
import com.example.dentalmobileapp.R;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceViewHolder> {
    private List<ServiceResponse> services;
    private Context context;

    public ServiceAdapter(Context context, List<ServiceResponse> services) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_services_list, parent, false);
        return new ServiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceResponse service = services.get(position);
        String imageData = service.getImage().getData();

        if (imageData != null) {
            byte[] imageDataBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            holder.serviceImage.setImageBitmap(imageBitmap);
        } else {
            holder.serviceImage.setImageResource(R.drawable.icon);
        }

        holder.serviceName.setText(service.getServiceName());
        holder.serviceDescription.setText(service.getServiceDescription());

        holder.serviceContainer.setOnClickListener(v -> {
            String getTitle = service.getServiceName();
            String getDes = service.getServiceDescription();
            String getPrice = service.getServicePrice();
            String getPayment = service.getServicePayment();
            view(imageData, getTitle, getDes, getPrice, getPayment );
        });
    }

    private void view(String serviceImage, String serviceTitle, String serviceDes, String servicePrice, String servicePayment) {
        Intent intent = new Intent(context, ViewService.class);
        intent.putExtra("service_image", serviceImage);
        intent.putExtra("service_name", serviceTitle);
        intent.putExtra("service_des", serviceDes);
        intent.putExtra("service_price", servicePrice);
        intent.putExtra("service_payment", servicePayment);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
