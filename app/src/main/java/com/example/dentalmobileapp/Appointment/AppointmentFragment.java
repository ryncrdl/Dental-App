package com.example.dentalmobileapp.Appointment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.dentalmobileapp.Api.ApiEndpoints;
import com.example.dentalmobileapp.CustomSpinnerAdapter;
import com.example.dentalmobileapp.Doctors.DoctorResponse;
import com.example.dentalmobileapp.R;
import com.example.dentalmobileapp.Services.ServiceResponse;
import com.example.dentalmobileapp.SignIn.SignIn;
import com.example.dentalmobileapp.Verification.CreateClient;
import com.example.dentalmobileapp.Verification.VerifyContactNumber;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppointmentFragment extends Fragment {

    private Spinner date, service, doctor;
    private String[] dateListArray = new String[10];
    private Uri selectedImageUri = null;
    private AppCompatButton attachImageButton, btnSubmitAppointment;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RelativeLayout requiredContainer;
    private Boolean isRequiredPayment = false;
    private ProgressBar loadingService;
    private TextView downPayment;
    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance() {
        return new AppointmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        date = view.findViewById(R.id.txt_date);
        service = view.findViewById(R.id.txt_service);
        doctor = view.findViewById(R.id.txt_doctor);
        attachImageButton = view.findViewById(R.id.btn_attach_image);
        requiredContainer = view.findViewById(R.id.required_container);
        loadingService = view.findViewById(R.id.progress_bar);
        downPayment = view.findViewById(R.id.txt_down_payment);
        btnSubmitAppointment = view.findViewById(R.id.btn_submit_appointment);

        btnSubmitAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAppointmentIsReady();
            }
        });

        service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedService = (String) parentView.getItemAtPosition(position);
                checkRequiredPaymentService(selectedService);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing if nothing is selected
            }
        });

        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Create a list of date strings
        String[] dateList = generateDateList();
        CustomSpinnerAdapter dateAdapter = new CustomSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, dateList, "Select Date");

        // Set the adapter for the Spinner
        loadServices();
        loadDoctors();
        date.setAdapter(dateAdapter);

        return view;
    }

    private void checkRequiredPaymentService(String selectedService) {
        loadingService.setVisibility(View.VISIBLE);
        requiredContainer.setVisibility(View.GONE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        Call<List<ServiceResponse>> call = apiEndpoints.getServices();
        call.enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {
                loadingService.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    List<ServiceResponse> services = response.body();
                    ServiceResponse selectedServiceResponse = findServiceByName(services, selectedService);

                    if (selectedServiceResponse != null) {
                        String requiresPayment = selectedServiceResponse.getServicePayment();
                        String getPrice = selectedServiceResponse.getServicePrice();
                        double price = Double.parseDouble(getPrice.replace(",", ""));
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        double priceDownPayment = price * 0.05;
                        String formattedPrice = decimalFormat.format(price);
                        String formattedDownPaymentPrice = decimalFormat.format(priceDownPayment);

                        if (requiresPayment.equals("Required Payment")) {
                            isRequiredPayment = true;
                            requiredContainer.setVisibility(View.VISIBLE);
                            downPayment.setText("Total Payment: " + formattedPrice + "\nDownpayment atleast "+ formattedDownPaymentPrice +" Pesos");
                        } else {
                            requiredContainer.setVisibility(View.GONE);
                            isRequiredPayment = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {
                loadingService.setVisibility(View.GONE);
            }
        });
    }

    private void checkIfAppointmentIsReady() {
        String selectedDate = date.getSelectedItem().toString();
        String selectedService = service.getSelectedItem().toString();
        String selectedDoctor = doctor.getSelectedItem().toString();

        // Check if the user has selected a date, service, doctor, and attached payment
        if (!selectedDate.equals("Select Date") &&
                !selectedService.equals("Select Service") &&
                !selectedDoctor.equals("Select Doctor")) {

            if(isRequiredPayment){
                if(selectedImageUri != null){
                    createAppointment();
                }else {
                    Toast.makeText(requireContext(), "Attach proof of Payment!", Toast.LENGTH_SHORT).show();
                }
            }else {
                createAppointment();
            }

        } else {
            // The appointment is not complete
            Toast.makeText(requireContext(), "Please fill in all the required information.", Toast.LENGTH_SHORT).show();
        }
    }

    private void createAppointment() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String getUserId = sharedPreferences.getString("userId", "");
        String getFullName = sharedPreferences.getString("full_name", "");
        String getContactNumber = sharedPreferences.getString("contact_number", "");
        String getDate = date.getSelectedItem().toString();
        String getService = service.getSelectedItem().toString();
        String getDoctor = doctor.getSelectedItem().toString();
        byte[] imageBytes = null;
        String base64Image = "";
        if(selectedImageUri != null){
            imageBytes = uriToByteArray(selectedImageUri);
            base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientId", getUserId);
        jsonObject.addProperty("fullName", getFullName);
        jsonObject.addProperty("contactNumber", getContactNumber);
        jsonObject.addProperty("Date", getDate);
        jsonObject.addProperty("Service", getService);
        jsonObject.addProperty("Requested Doctor", getDoctor);
        jsonObject.addProperty("Proof of Payment", base64Image);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

        Call<AppointmentResponse> call = apiEndpoints.submitAppointment(requestBody);
        call.enqueue(new Callback<AppointmentResponse>() {
            @Override
            public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Appointment is sent, wait for SMS Text", Toast.LENGTH_SHORT).show();

                    // Clear Spinners and attached image
                    date.setSelection(0);
                    service.setSelection(0);
                    doctor.setSelection(0);
                    attachImageButton.setText("Attach Proof Payment Here");
                    selectedImageUri = null;

                    // Navigate back to the home fragment
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Appointment creation failed.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppointmentResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Appointment creation failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] uriToByteArray(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private ServiceResponse findServiceByName(List<ServiceResponse> services, String serviceName) {
        for (ServiceResponse serviceResponse : services) {
            if (serviceName.equals(serviceResponse.getServiceName())) {
                return serviceResponse;
            }
        }
        return null; // Service not found
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            attachImageButton.setText(getFileNameFromUri(selectedImageUri));
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String[] generateDateList() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

        List<String> dateListWithSelect = new ArrayList<>();
        dateListWithSelect.add("Select Date");

        for (int i = 0; i < dateListArray.length; i++) {
            Date currentDate = calendar.getTime();
            String formattedDate = dateFormat.format(currentDate);
            dateListArray[i] = formattedDate;
            dateListWithSelect.add(formattedDate);
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Add one day to the date
        }

        return dateListWithSelect.toArray(new String[0]);
    }

    private void loadServices() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        Call<List<ServiceResponse>> call = apiEndpoints.getServices();
        call.enqueue(new Callback<List<ServiceResponse>>() {
            @Override
            public void onResponse(Call<List<ServiceResponse>> call, Response<List<ServiceResponse>> response) {

                if (response.isSuccessful()) {
                    List<ServiceResponse> services = response.body();
                    List<String> serviceNames = new ArrayList<>();
                    serviceNames.add("Select Service");

                    for (ServiceResponse service : services) {
                        serviceNames.add(service.getServiceName());
                    }

                    setServiceSpinner(serviceNames); // Set up the custom spinner for services
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<ServiceResponse>> call, Throwable t) {

            }
        });
    }

    private void loadDoctors() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-east-1.aws.data.mongodb-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoints apiEndpoints = retrofit.create(ApiEndpoints.class);

        Call<List<DoctorResponse>> call = apiEndpoints.getDoctors();
        call.enqueue(new Callback<List<DoctorResponse>>() {
            @Override
            public void onResponse(Call<List<DoctorResponse>> call, Response<List<DoctorResponse>> response) {
                if (response.isSuccessful()) {
                    List<DoctorResponse> doctors = response.body();
                    List<String> doctorNames = new ArrayList<>();
                    doctorNames.add("Select Doctor");

                    for (DoctorResponse doctor : doctors) {
                        doctorNames.add("Doc. " + doctor.getDoctorFirstName() + " " + doctor.getDoctorLastName());
                    }

                    setDoctorSpinner(doctorNames); // Set up the custom spinner for doctors
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<List<DoctorResponse>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void setServiceSpinner(List<String> serviceNames) {
        String[] serviceListArray = serviceNames.toArray(new String[0]);
        CustomSpinnerAdapter serviceAdapter = new CustomSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceListArray, "Select Service");
        service.setAdapter(serviceAdapter);
    }

    private void setDoctorSpinner(List<String> doctorNames) {
        String[] doctorListArray = doctorNames.toArray(new String[0]);
        CustomSpinnerAdapter serviceAdapter = new CustomSpinnerAdapter(requireContext(), android.R.layout.simple_spinner_item, doctorListArray, "Select Doctor");
        doctor.setAdapter(serviceAdapter);
    }
}
