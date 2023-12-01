package com.example.dentalmobileapp.Home;
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
import com.example.dentalmobileapp.R;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementViewHolder> {
    private List<AnnouncementResponse> announcements;
    private Context context;

    public AnnouncementAdapter(Context context, List<AnnouncementResponse> announcements) {
        this.announcements = announcements;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_announcements_list, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        AnnouncementResponse announcement = announcements.get(position);
        String imageData = announcement.getImage().getData();

        if (imageData != null) {
            byte[] imageDataBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            holder.announcementImage.setImageBitmap(imageBitmap);
        } else {
            holder.announcementImage.setImageResource(R.drawable.icon);
        }
        holder.announcementName.setText(announcement.getAnnouncementName());
        holder.announcementContext.setText(announcement.getAnnouncementContext());

        holder.announcementContainer.setOnClickListener(v -> {
            String announcementTitle = announcement.getAnnouncementName();
            String announcementContext = announcement.getAnnouncementContext();
            view(imageData, announcementTitle, announcementContext );
        });
    }

    private void view(String announcementImage, String announcementTitle, String announcementContext) {
        Intent intent = new Intent(context, ViewAnnouncement.class);
        intent.putExtra("announcement_image", announcementImage);
        intent.putExtra("announcement_name", announcementTitle);
        intent.putExtra("announcement_context", announcementContext);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }
}
