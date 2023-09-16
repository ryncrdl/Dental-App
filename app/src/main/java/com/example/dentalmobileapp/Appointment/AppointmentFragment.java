package com.example.dentalmobileapp.Appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.Doctors.DoctorResponse;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServiceResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppointmentFragment extends Fragment {

    private Spinner date, service, doctor;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance() {
        return new AppointmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        date = view.findViewById(R.id.txt_date);
        service = view.findViewById(R.id.txt_service);
        doctor = view.findViewById(R.id.txt_doctor);

        // Create a list of date strings
        List<String> dateList = generateDateList();

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                dateList
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        loadServices();
        loadDoctors();
        date.setAdapter(adapter);

        // Set an item selected listener for the Spinner
        date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected date
                String selectedDate = dateList.get(position);
                Toast.makeText(requireContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        return view;
    }

    private List<String> generateDateList() {
        List<String> dateList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        dateList.add("Select Date");

        for (int i = 0; i < 10; i++) {
            Date currentDate = calendar.getTime();
            String formattedDate = dateFormat.format(currentDate);
            dateList.add(formattedDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day to the date
        }

        return dateList;
    }


    private void loadServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        Call<List<ServiceResponse>> call = apiEndpoints.getServices();
        call.enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                if (response.isSuccessful()) {
                    List<ServiceResponse> services = response.body();
                    List<String> serviceNames = new ArrayList<>();
                    serviceNames.add("Select Service");

                    for (ServiceResponse service : services) {
                        serviceNames.add(service.getServiceName());
                    }

                    serviceList(serviceNames);
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void loadDoctors() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        Call<List<DoctorResponse>> call = apiEndpoints.getDoctors();
        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                if (response.isSuccessful()) {
                    List<DoctorResponse> doctors = response.body();
                    List<String> doctorNames = new ArrayList<>();
                    doctorNames.add("Select Doctor");

                    for (DoctorResponse service : doctors) {
                        doctorNames.add("Doc. " + service.getDoctorFirstName() + " " + service.getDoctorLastName());
                    }

                    doctorList(doctorNames);
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                // Handle failure
            }
        });
    }


    private void serviceList(List<String> serviceNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, serviceNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        service.setAdapter(adapter);
    }

    private void doctorList(List<String> doctorNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, doctorNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        doctor.setAdapter(adapter);
    }

}
