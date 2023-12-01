package com.example.dentalmobileapp.Services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.Home.HomeFragment;
import com.example.dentalmobileapp.R;

public class ViewService extends AppCompatActivity {

    AppCompatButton backHome;
    ImageView serviceImage;
    TextView title, description, price, payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_service);

        backHome = findViewById(R.id.backHome);
        serviceImage = findViewById(R.id.viewServiceImage);
        title = findViewById(R.id.viewServiceTitle);
        description = findViewById(R.id.viewDescriptionTitle);
        price = findViewById(R.id.viewPriceTitle);
        payment = findViewById(R.id.viewPaymentTitle);

        backHome.setOnClickListener(view -> {
            Intent intent = new Intent(ViewService.this, Dashboard.class);
            startActivity(intent);
        });


        String getImage = getIntent().getStringExtra("service_image");
        String getTitle = getIntent().getStringExtra("service_name");
        String getDes = getIntent().getStringExtra("service_des");
        String getPrice = getIntent().getStringExtra("service_price");
        String getPayment = getIntent().getStringExtra("service_payment");

        if (getImage != null) {
            byte[] imageDataBytes = Base64.decode(getImage, Base64.DEFAULT);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.length);
            serviceImage.setImageBitmap(imageBitmap);
        } else {
            serviceImage.setImageResource(R.drawable.icon);
        }

        title.setText(getTitle);
        description.setText(getDes);
        price.setText("Price: "+ getPrice);
        payment.setText("Payment: "+getPayment);
    }

}
