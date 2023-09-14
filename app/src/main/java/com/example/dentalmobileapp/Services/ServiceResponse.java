package com.example.dentalmobileapp.Services;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

public class ServiceResponse implements Parcelable {

    private String _id;

    private ImageView serviceImage;
    private String serviceName, serviceDescription;

    protected ServiceResponse(Parcel in) {
        _id = in.readString();
        //serviceImage = in.readString();  fix this to image
        serviceName = in.readString();
        serviceDescription = in.readString();
    }


    public static final Creator<ServiceResponse> CREATOR = new Creator<ServiceResponse>() {
        @Override
        public ServiceResponse createFromParcel(Parcel in) {
            return new ServiceResponse(in);
        }

        @Override
        public ServiceResponse[] newArray(int size) {
            return new ServiceResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        //dest.writeString(serviceImage);
        dest.writeString(serviceName);
        dest.writeString(serviceDescription);
    }

    public ImageView getServiceImage() {
        return serviceImage;
    }
    public String getServiceName() {
        return serviceName;
    }
    public String getServiceDescription() {
        return serviceDescription;
    }


    public String get_id() {
        return _id;
    }

}

