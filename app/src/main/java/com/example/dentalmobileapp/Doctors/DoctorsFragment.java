package com.example.dentalmobileapp.Doctors;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.dentalmobileapp.Api.ApiClient;
import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServiceAdapter;
import com.example.dentalmobileapp.Services.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<DoctorResponse> doctors = new ArrayList<>();
    private DoctorAdapter doctorAdapter;
    private ProgressBar progressBar;

    public DoctorsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        RecyclerView doctorRecyclerView = view.findViewById(R.id.view_doctors);
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        doctorAdapter = new DoctorAdapter(getContext(), doctors);
        doctorRecyclerView.setAdapter(doctorAdapter);

        fetchDoctors();

        return view;
    }

    public void fetchDoctors() {
        showLoading();

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();
        Call<List<DoctorResponse>> call = apiService.getDoctors();

        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    List<DoctorResponse> fetchedDoctors = response.body();
                    if (fetchedDoctors != null && !fetchedDoctors.isEmpty()) {
                        doctors.clear();
                        doctors.addAll(fetchedDoctors);
                        doctorAdapter.notifyDataSetChanged();
                    } else {
                        // Handle empty or null response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                hideLoading();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}