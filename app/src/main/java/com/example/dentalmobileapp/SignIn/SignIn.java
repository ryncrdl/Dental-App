package com.example.dentalmobileapp.SignIn;

import static com.example.dentalmobileapp.Utils.Hashing.verifyPassword;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.SignUp.SignUp;
import com.example.dentalmobileapp.Utils.LoadingScreen;
import com.example.dentalmobileapp.Verification.CreateClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity {

    private EditText username, password;
    private TextView signUp;
    private AppCompatButton signIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        if (isUserLoggedIn()) {
            // Redirect to the dashboard
            startActivity(new Intent(this, Dashboard.class));
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.txt_username);
        password = findViewById(R.id.txt_password);
        signIn = findViewById(R.id.btn_signIn);
        signUp = findViewById(R.id.btn_signUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //Check valid username and password
                String getUsername = username.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                checkUsernameAndPassword(getUsername, getPassword);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }


    private void checkUsernameAndPassword(String username, String password){
        if(username.isEmpty()){
            Toast.makeText(SignIn.this, "Please your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(SignIn.this, "Please your password", Toast.LENGTH_SHORT).show();
        }else if(!username.isEmpty() && !password.isEmpty()){
            validateCredentials(username, password);
        }
    }

    private void validateCredentials(String username, String password){
        LoadingScreen.showLoadingModal(this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
//        jsonObject.addProperty("password", password);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<ClientResponse> call = apiEndpoints.signinClient(requestBody);
        call.enqueue(new Callback<ClientResponse>() {
            @Override
            public void onResponse(Call<ClientResponse> call, Response<ClientResponse> response) {
                LoadingScreen.hideLoadingModal();
                ClientResponse clientResponse = response.body();
                if (response.code() == 200) {
                    String userId = clientResponse.getId();
                    String fullName = clientResponse.getFullName();
                    String username = clientResponse.getUsername();
                    String contactNumber = clientResponse.getContactNumber();
                    String getHashedPassword = clientResponse.getPassword();
                    Boolean validate = verifyPassword(password, getHashedPassword);
                    if(validate){
                        setLoggedInStatus(true);
                        storeUserData(userId, fullName, username, contactNumber);
                        Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();

                        // Finish the current SignIn activity
                        finish();
                        startActivity(new Intent(SignIn.this, Dashboard.class));
                    }else{
                        Toast.makeText(SignIn.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setLoggedInStatus(false);
                    Toast.makeText(SignIn.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ClientResponse> call, Throwable t) {
                LoadingScreen.hideLoadingModal();
                Toast.makeText(SignIn.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserData(String userId, String fullName, String username, String contactNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", userId);
        editor.putString("full_name", fullName);
        editor.putString("username", username);
        editor.putString("contact_number", contactNumber);

        editor.apply();
    }

    private void setLoggedInStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }


}