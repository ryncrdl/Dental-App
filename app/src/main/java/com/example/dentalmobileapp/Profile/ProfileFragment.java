package com.example.dentalmobileapp.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServiceResponse;
import com.example.dentalmobileapp.SignIn.ClientResponse;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.example.dentalmobileapp.SignUp.SignUp;
import com.example.dentalmobileapp.Verification.CreateClient;
import com.example.dentalmobileapp.Verification.VerifyContactNumber;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String userId;
    private TextView fullName, username, contactNumber, clientPoints;
    private EditText password, confirmPassword;
    private AppCompatButton btnLogout, btnChangePassword;
    private FirebaseAuth firebaseAuth;
    private String getPoints;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btn_logout);
        fullName = view.findViewById(R.id.txt_currentClient);
        username = view.findViewById(R.id.txt_currentClientUsername);
        clientPoints = view.findViewById(R.id.txt_currentClientPoints);
        password = view.findViewById(R.id.txt_new_password);
        confirmPassword = view.findViewById(R.id.txt_confirm_password);
        contactNumber = view.findViewById(R.id.txt_currentClientContactNumber);
        btnChangePassword = view.findViewById(R.id.btn_change_password);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user
                firebaseAuth.signOut();
                logoutUser();
                // Redirect to the login or sign-in screen
                Intent intent = new Intent(getActivity(), SignIn.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getPassword = password.getText().toString().trim();
                String getConfirmPassword = confirmPassword.getText().toString().trim();
                checkCredentials(getPassword, getConfirmPassword);
            }
        });

        getUserDataFromSharedPreferences();

        return view;
    }

    private void checkCredentials(String newPassword, String confirmPassword){
        if (newPassword.isEmpty()){
            Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        }else if(newPassword.length() <= 7){
            Toast.makeText(getContext(), "password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
        }else if(!Objects.equals(confirmPassword, newPassword)){
            Toast.makeText(getContext(), "Password doesn't match!", Toast.LENGTH_SHORT).show();
        }else {
            String getUserId = userId;
            UpdatePassword(getUserId, newPassword);
        }
    }


    private void UpdatePassword(String userId, String newPassword){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);
        jsonObject.addProperty("password", newPassword);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreateClient> call = apiEndpoints.updatePassword(requestBody);
        call.enqueue(new Callback<CreateClient>() {
            @Override
            public void onResponse(Call<CreateClient> call, Response<CreateClient> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                } else {
                    Toast.makeText(getContext(), "Password updating failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateClient> call, Throwable t) {
                Toast.makeText(getContext(), "Error: password updating failed.", Toast.LENGTH_SHORT).show();
            }
        });
    };

    private void getUserDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getUserId = sharedPreferences.getString("userId", "");
        String getFullName = sharedPreferences.getString("full_name", "");
        String getUsername = sharedPreferences.getString("username", "");
        String getContactNumber = sharedPreferences.getString("contact_number", "");

        // Now you can use fullName, username, and contactNumber to display the user's information
        if (!getUserId.isEmpty() &&!getFullName.isEmpty() && !getUsername.isEmpty() && !getContactNumber.isEmpty()) {
            userId = getUserId;
            fullName.setText(getFullName);
            username.setText(getUsername);
            contactNumber.setText(getContactNumber);
            GetClientPoints(getUserId);
        } else {
            // Handle the case where the data is not available
        }
    }

    private void logoutUser() {
        // Clear user data from SharedPreferences
        clearUserData();

        // Set the "isLoggedIn" status to false
        setLoggedInStatus(false);

        // Redirect to the sign-in activity or any other appropriate screen
        startActivity(new Intent(getContext(), SignIn.class));
        getActivity().finish(); // Finish the current activity
    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Remove user-specific data
        editor.remove("userId");
        editor.remove("full_name");
        editor.remove("username");
        editor.remove("contact_number");

        editor.apply();
    }

    private void setLoggedInStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private void GetClientPoints(String userId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", userId);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<ClientResponse> call = apiEndpoints.GetClientPoints(requestBody);
        call.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                ClientResponse points = response.body();
                String getPoints = points.getPoints();
                if (response.isSuccessful()) {
                    clientPoints.setText("Reward Points: " + getPoints);
                }
            }

            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

}
