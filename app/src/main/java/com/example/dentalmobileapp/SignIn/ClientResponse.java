package com.example.dentalmobileapp.SignIn;

import com.google.gson.annotations.SerializedName;

public class ClientResponse {

    @SerializedName("_id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("isValid")
    private boolean isValid;

    // Constructors, getters, and setters (if needed) go here

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public boolean isValid() {
        return isValid;
    }
}
