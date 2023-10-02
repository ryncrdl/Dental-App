package com.example.dentalmobileapp.SignIn;

import com.google.gson.annotations.SerializedName;

public class ClientResponse {

    @SerializedName("_id")
    private String id;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("points")
    private String points;

    @SerializedName("isValid")
    private boolean isValid;

    // Constructors, getters, and setters (if needed) go here

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
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

    public String getPoints() {
        return points;
    }

    public boolean isValid() {
        return isValid;
    }
}
