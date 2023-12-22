package com.example.dentalmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isUserLoggedIn()) {
            // Redirect to the dashboard
            startActivity(new Intent(this, Dashboard.class));
            finish();
        }

        btnGetStarted = findViewById(R.id.btn_getStarted);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this, Dashboard.class));
        }
    }

    private boolean isUserLoggedIn() {
       SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

}