package com.example.dentalmobileapp.SignIn;

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

        if (sessionExists()) {
            // Redirect to the login screen
            Intent intent = new Intent(SignIn.this, Dashboard.class);
            startActivity(intent);
            finish(); // Optional, prevents going back to MainActivity
        }

        firebaseAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.txt_username);
        password = findViewById(R.id.txt_password);
        signIn = findViewById(R.id.btn_signIn);
        signUp = findViewById(R.id.btn_signUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Check valid email and password
                String getUsername = username.getText().toString().trim();
                String getPassword = password.getText().toString().trim();
                checkEmailAndPassword(getUsername, getPassword);


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

    private boolean sessionExists() {
        // Check if the user's session exists in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("username", null) != null;
    }

    private void checkEmailAndPassword(String username, String password){
        if(username.isEmpty()){
            Toast.makeText(SignIn.this, "Please your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(SignIn.this, "Please your password", Toast.LENGTH_SHORT).show();
        }else if(!username.isEmpty() && !password.isEmpty()){
            validateCredentials(username, password);
        }
    }

    private void validateCredentials(String username, String password){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreateClient> call = apiEndpoints.signinClient(requestBody);
        call.enqueue(new Callback<CreateClient>() {
            @Override
            public void onResponse(Call<CreateClient> call, Response<CreateClient> response) {

                    if(response.code() == 200){
                        Toast.makeText(SignIn.this, "Successfully login", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this, Dashboard.class));
                        saveSession(username, password);
                    }else {
                        Toast.makeText(SignIn.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
            }
            @Override
            public void onFailure(Call<CreateClient> call, Throwable t) {
                Toast.makeText(SignIn.this, "Server failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSession(String username, String password) {
        // Store the user's session information, such as username, in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }


}