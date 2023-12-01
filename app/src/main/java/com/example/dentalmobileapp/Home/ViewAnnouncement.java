package com.example.dentalmobileapp.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ViewAnnouncement  extends AppCompatActivity {

    AppCompatButton backHome;
    ImageView announcementImage;
    TextView title, context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announcement);

        backHome = findViewById(R.id.backHome);
        announcementImage = findViewById(R.id.viewImage);
        title = findViewById(R.id.viewAnnouncementTitle);
        context = findViewById(R.id.viewContextTitle);

        backHome.setOnClickListener(view -> {
            Intent intent = new Intent(ViewAnnouncement.this, Dashboard.class);
            startActivity(intent);
        });

        String getTitle = getIntent().getStringExtra("announcement_name");
        String getImage = getIntent().getStringExtra("announcement_image");
        String getContext = getIntent().getStringExtra("announcement_context");


        if (getImage != null) {
            byte[] imageDataBytes = Base64.decode(getImage, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            announcementImage.setImageBitmap(imageBitmap);
        } else {
            announcementImage.setImageResource(R.drawable.icon);
        }

        title.setText(getTitle);
        context.setText(getContext);
    }

}
