package com.example.dentalmobileapp.Home;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private List<AnnouncementResponse> announcements = new ArrayList<>();
    private AnnouncementAdapter announcementAdapter;
    private ProgressBar progressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = view.findViewById(R.id.progress_bar);
        RecyclerView announcementRecycleView = view.findViewById(R.id.view_announcements);
        announcementRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        announcementAdapter = new AnnouncementAdapter(getContext(), announcements);
        announcementRecycleView.setAdapter(announcementAdapter);

        fetchAnnouncements();

        return view;
    }

    public void fetchAnnouncements() {
        showLoading();

        ApiClient apiClient = new ApiClient();
        ApiEndpoints apiService = apiClient.getApiService();
        Call<List<AnnouncementResponse>> call = apiService.getAnnouncements();

        call.enqueue(new Callback<List<AnnouncementResponse>>() {
            @Override
            public void onResponse(Call<List<AnnouncementResponse>> call, Response<List<AnnouncementResponse>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    List<AnnouncementResponse> fetchedAnnouncements = response.body();
                    if (fetchedAnnouncements != null && !fetchedAnnouncements.isEmpty()) {
                        announcements.clear();
                        announcements.addAll(fetchedAnnouncements);
                        announcementAdapter.notifyDataSetChanged();
                    } else {
                        // Handle empty or null response
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<AnnouncementResponse>> call, Throwable t) {
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