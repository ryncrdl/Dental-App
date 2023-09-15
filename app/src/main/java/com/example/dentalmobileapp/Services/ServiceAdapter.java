package com.example.dentalmobileapp.Services;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        // Retrieve the Base64-encoded image data from the ServiceResponse
        String imageData = service.getImage().getData();

        if (imageData != null) {
            // Decode the Base64 data and set it to the ImageView
            byte[] imageDataBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            holder.serviceImage.setImageBitmap(imageBitmap);
        } else {
            // Handle the case when there is no image data
            // You can set a placeholder or hide the ImageView
            holder.serviceImage.setImageResource(R.drawable.icon); // Replace with your placeholder image
        }

        holder.serviceName.setText(service.getServiceName());
        holder.serviceDescription.setText(service.getServiceDescription());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
