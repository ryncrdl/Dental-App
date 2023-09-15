package com.example.dentalmobileapp.Api;

import com.example.dentalmobileapp.Doctors.DoctorResponse;
import com.example.dentalmobileapp.Services.ServiceResponse;
import com.example.dentalmobileapp.Verification.CreateClient;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface ApiEndpoints {

    @POST("app/dental-pbjlw/endpoint/signUp")
    Call<CreateClient> CreateClient(@Body RequestBody requestBody);

    @POST("app/dental-pbjlw/endpoint/signIn")
    Call<CreateClient> signinClient(@Body RequestBody requestBody);

    //Services
    @GET("app/dental-pbjlw/endpoint/services")
    Call<List<ServiceResponse>> getServices();

    //Doctors
    @GET("app/dental-pbjlw/endpoint/doctors")
    Call<List<DoctorResponse>> getDoctors();
}
