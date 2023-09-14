package com.example.dentalmobileapp.Dashboard;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dentalmobileapp.Appointment.AppointmentFragment;
import com.example.dentalmobileapp.Doctors.DoctorsFragment;
import com.example.dentalmobileapp.Home.HomeFragment;
import com.example.dentalmobileapp.Profile.ProfileFragment;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServicesFragment;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.example.dentalmobileapp.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth auth;

    ActivityDashboardBinding binding;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNavigationView;

        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int selectedItemId = item.getItemId();

            if (selectedItemId == R.id.home) {
                replaceFragment(new HomeFragment());
            }else if (selectedItemId == R.id.appointment){
                replaceFragment(new AppointmentFragment());
            }else if (selectedItemId == R.id.services){
                replaceFragment(new ServicesFragment());
            }else if (selectedItemId == R.id.doctors){
                replaceFragment(new DoctorsFragment());
            }else if (selectedItemId == R.id.profile){
                replaceFragment(new ProfileFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void handleLogout(){
        auth.signOut();
        startActivity(new Intent(Dashboard.this, SignIn.class));
        finish();
    }
}
