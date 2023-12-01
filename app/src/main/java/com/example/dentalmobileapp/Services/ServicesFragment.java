package com.example.dentalmobileapp.Services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dentalmobileapp.Api.ApiClient;
import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesFragment extends Fragment {

    private List<ServiceResponse> services = new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    private ProgressBar progressBar;

    public ServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_services, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        RecyclerView serviceRecyclerView = view.findViewById(R.id.view_services);
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        serviceAdapter = new ServiceAdapter(getContext(), services);
        serviceRecyclerView.setAdapter(serviceAdapter);

        fetchServices();

        return view;
    }

    public void fetchServices() {
        showLoading();

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();
        Call<List<ServiceResponse>> call = apiService.getServices();

        call.enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<ServiceResponse> fetchedServices = response.body();
                    if (fetchedServices != null && !fetchedServices.isEmpty()) {
                        services.clear();
                        services.addAll(fetchedServices);
                        serviceAdapter.notifyDataSetChanged();
                    } else {
                        // Handle empty or null response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {
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
