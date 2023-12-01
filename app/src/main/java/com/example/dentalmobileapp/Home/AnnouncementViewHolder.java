package com.example.dentalmobileapp.Home;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalmobileapp.R;

public class AnnouncementViewHolder extends RecyclerView.ViewHolder
{
    public ImageView announcementImage;
    TextView announcementName, announcementContext;
    RelativeLayout announcementContainer;
    public AnnouncementViewHolder(@NonNull View announcementView){
        super(announcementView);
        announcementImage = announcementView.findViewById(R.id.announcement_image);
        announcementName = announcementView.findViewById(R.id.announcement_name);
        announcementContext = announcementView.findViewById(R.id.announcement_context);
        announcementContainer = announcementView.findViewById(R.id.announcements_container);
    }
}
