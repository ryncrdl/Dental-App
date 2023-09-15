package com.example.dentalmobileapp.Doctors;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorResponse implements Parcelable {

    private String _id;
    private String FirstName, LastName, Contact, Email, Address;

    protected DoctorResponse(Parcel in) {
        _id = in.readString();
        FirstName = in.readString();
        LastName = in.readString();
        Contact = in.readString();
        Email = in.readString();
        Address = in.readString();
    }

    public static final Creator<DoctorResponse> CREATOR = new Creator<DoctorResponse>() {
        @Override
        public DoctorResponse createFromParcel(Parcel in) {
            return new DoctorResponse(in);
        }

        @Override
        public DoctorResponse[] newArray(int size) {
            return new DoctorResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(Contact);
        dest.writeString(Email);
        dest.writeString(Address);
    }


    public String getDoctorFirstName() {
        return FirstName;
    }
    public String getDoctorLastName() {
        return LastName;
    }
    public String getDoctorContact() {
        return Contact;
    }
    public String getDoctorEmail() {
        return Email;
    }
    public String getDoctorAddress() {
        return Address;
    }

    public String get_id() {
        return _id;
    }


}
