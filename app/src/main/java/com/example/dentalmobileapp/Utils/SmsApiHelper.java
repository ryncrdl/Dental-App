package com.example.dentalmobileapp.Utils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SmsApiHelper {
    private static final String SEMAPHORE_API_URL = "https://api.semaphore.co/api/v4/messages";
    public static String sendOtp(String apiKey, String phoneNumber, String message) {
        try {
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("apikey", apiKey)
                    .add("number", phoneNumber)
                    .add("message", message)
                    .build();

            Request request = new Request.Builder()
                    .url(SEMAPHORE_API_URL)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
