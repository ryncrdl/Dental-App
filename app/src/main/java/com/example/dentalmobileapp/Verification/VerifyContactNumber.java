package com.example.dentalmobileapp.Verification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.Dashboard.Dashboard;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonObject;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyContactNumber extends AppCompatActivity {

    private String otp;

    private int randomNumber;
    private String fullName, username, password;
    private EditText contactNumber, verificationCode;
    private TextView btnSendCode, btnSignIn;
    private AppCompatButton btnSignUp;

    FirebaseAuth auth;
    String verificationId;
    CountDownTimer countdownTimer;
    private static final long ENABLE_DELAY_MS = 60000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_contact_number);

        auth = FirebaseAuth.getInstance();

        contactNumber = findViewById(R.id.txt_contactNumber);
        verificationCode = findViewById(R.id.txt_verificationCode);
        btnSendCode = findViewById(R.id.btn_sendCode);
        btnSignUp = findViewById(R.id.btn_signUp);
        btnSignIn = findViewById(R.id.btn_signIn);

        fullName = getIntent().getStringExtra("fullName");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        contactNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().replaceAll("[^\\d]", ""); // Remove non-digits
                if (input.length() == 11) { // Check if input has at least 10 digits
                    contactNumber.removeTextChangedListener(this);
                    contactNumber.setText(formatContactNumber(input));
                    contactNumber.setSelection(contactNumber.getText().length());
                    contactNumber.addTextChangedListener(this);
                    btnSendCode.setEnabled(true);
                } else {
                    btnSendCode.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = contactNumber.getText().toString().trim();

                if(phoneNumber.isEmpty()){
                    Toast.makeText(VerifyContactNumber.this, "Enter valid phone number!", Toast.LENGTH_SHORT).show();
                }else {
                    checkContactNumberExists(phoneNumber);
                }


            }


        });

        verificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check the length and restrict it to 6 characters
                if (s.length() > 6) {
                    verificationCode.setText(s.subSequence(0, 6)); // Trim to 6 characters
                    verificationCode.setSelection(6); // Move cursor to the end
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(TextUtils.isEmpty(verificationCode.getText().toString())){
                    Toast.makeText(VerifyContactNumber.this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                }else{
                    verifyCode(verificationCode.getText().toString());
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
              startActivity(new Intent(VerifyContactNumber.this, SignIn.class));
            }
        });
    }

    private void checkContactNumberExists(String phoneNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("contactNumber", phoneNumber);


        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreateClient> call = apiEndpoints.CheckContactNumberExists(requestBody);
        call.enqueue(new Callback<CreateClient>() {
            @Override
            public void onResponse(Call<CreateClient> call, Response<CreateClient> response) {
                    if(response.code() == 403){
                        Toast.makeText(VerifyContactNumber.this, "Contact Number is already used.", Toast.LENGTH_SHORT).show();
                    }else if(response.code() == 200) {
                        sendVerificationCode(phoneNumber);
                    }
            }

            @Override
            public void onFailure(Call<CreateClient> call, Throwable t) {
                Toast.makeText(VerifyContactNumber.this, "Server Failure.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        btnSendCode.setEnabled(false);

        // Cancel any ongoing countdown timer
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        countdownTimer = new CountDownTimer(ENABLE_DELAY_MS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnSendCode.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                btnSendCode.setEnabled(true);
                btnSendCode.setText("Send Code");
            }
        }.start();


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+63" + phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }
        }

       public void onVerificationFailed(@NonNull FirebaseException e) {
           Toast.makeText(VerifyContactNumber.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        public void onCodeSent(@NonNull String  s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
           super.onCodeSent(s, token);
           verificationId = s;
        }
    };

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VerifyContactNumber.this, "Created Successful", Toast.LENGTH_SHORT).show();
                            createClient(fullName, username, password, contactNumber.getText().toString());
                            contactNumber.setText("");
                            verificationCode.setText("");
                            startActivity(new Intent(VerifyContactNumber.this, SignIn.class));
                        }
                    }
                });
    }

    private void createClient(String fullName, String username, String password, String contactNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fullName", fullName);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("contactNumber", contactNumber);
        jsonObject.addProperty("rfidNumber", "");
        jsonObject.addProperty("points", 0);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<CreateClient> call = apiEndpoints.CreateClient(requestBody);
        call.enqueue(new Callback<CreateClient>() {
            @Override
            public void onResponse(Call<CreateClient> call, Response<CreateClient> response) {
                if (response.isSuccessful()) {
                    if(response.code() == 200){
                        startActivity(new Intent(VerifyContactNumber.this, SignIn.class));
                    }else if(response.code() == 400) {
                        Toast.makeText(VerifyContactNumber.this, response.message(), Toast.LENGTH_SHORT);
                    }
                } else {
                    Toast.makeText(VerifyContactNumber.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreateClient> call, Throwable t) {
                Toast.makeText(VerifyContactNumber.this, "Client creation failed.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String formatContactNumber(String input) {
        // Format input as "09123456789"
        return input.replaceFirst("(\\d{2})(\\d{3})(\\d{4})", "$1$2$3");
    }


    public static String generateOTP() {
        String digits = "0123456789";
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(digits.length());
            otpBuilder.append(digits.charAt(index));
        }

        return otpBuilder.toString();
    }
}
