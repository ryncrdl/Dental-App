package com.example.dentalmobileapp.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.example.dentalmobileapp.Verification.VerifyContactNumber;

import androidx.appcompat.widget.AppCompatButton;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private AppCompatButton btnNextStep;
    private TextView btnSignIn;
    private EditText txtFullName, txtUsername, txtPassword, txtConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtFullName = findViewById(R.id.txt_fullname);
        txtUsername = findViewById(R.id.txt_username);
        txtPassword = findViewById(R.id.txt_password);
        txtConfirmPassword = findViewById(R.id.txt_confirmpassword);
        btnNextStep = findViewById(R.id.btn_nextstep);
        btnSignIn = findViewById(R.id.btn_signIn);

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                //Check valid email and password
                String getFullName = txtFullName.getText().toString().trim();
                String getUsername = txtUsername.getText().toString().trim();
                String getPassword = txtPassword.getText().toString().trim();
                String getConfirmPassword = txtConfirmPassword.getText().toString().trim();

                checkCredentials(getFullName, getUsername, getPassword, getConfirmPassword);


            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
            }
        });
    }

    private void checkCredentials(String fullname, String username, String password, String confirmPassword){
        if(fullname.isEmpty()){
            Toast.makeText(SignUp.this, "Please your full name", Toast.LENGTH_SHORT).show();
        }else if(password.length() <= 7){
            Toast.makeText(SignUp.this, "password should be minimum 8 characters", Toast.LENGTH_SHORT).show();
        }else if(username.length() <= 4){
            Toast.makeText(SignUp.this, "username should be minimum 5 characters", Toast.LENGTH_SHORT).show();
        }
        else if(username.isEmpty()){
            Toast.makeText(SignUp.this, "Please your username", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(SignUp.this, "Please your password", Toast.LENGTH_SHORT).show();
        }else if(!Objects.equals(confirmPassword, password)){
            Toast.makeText(SignUp.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
        }else {
            validateCredentials(fullname, username, password);
        }
    }

    private void validateCredentials(String fullname, String username, String password){
        Intent intent = new Intent(SignUp.this, VerifyContactNumber.class);
        intent.putExtra("fullName", fullname);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    }


}
