package com.example.dentalmobileapp.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class SendOTP {
    private static final String API_KEY = "33b283b28c56220d64ef0f6cdc99dbe1";
    private static String PHONE_NUMBER;
    private static String OTP_MESSAGE;
    private static Context context;
    private static String OTP_CODE;

    public void requestOTPCode(String number, Context context){
        this.context = context;
        this.PHONE_NUMBER = number;
        String OTP = OTPGenerator.generateOTP();
        this.OTP_CODE = OTP;
        String message = "Your OTP Code: " + OTP;
        this.OTP_MESSAGE = message;
        new SendOtpTask().execute();
    }

    public String getContactNumber(){
        return PHONE_NUMBER;
    }

    public String getOTP(){
        return OTP_CODE;
    }

    private class SendOtpTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return SmsApiHelper.sendOtp(API_KEY, PHONE_NUMBER, OTP_MESSAGE);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            LoadingScreen.showLoadingModal(context);
            if (result != null) {
                LoadingScreen.hideLoadingModal();
                Toast.makeText(context, "Please wait for OTP Code", Toast.LENGTH_SHORT).show();
            } else {
                LoadingScreen.hideLoadingModal();
                Toast.makeText(context, "Failed to send OTP Code, Check credits", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
