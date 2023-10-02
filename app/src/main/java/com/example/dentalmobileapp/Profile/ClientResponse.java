package com.example.dentalmobileapp.Profile;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientResponse implements Parcelable {

    private String _id;
    private String points;

    protected ClientResponse(Parcel in) {
        _id = in.readString();
        points = in.readString();
    }

    public static final Creator<ClientResponse> CREATOR = new Creator<ClientResponse>() {
        @Override
        public ClientResponse createFromParcel(Parcel in) {
            return new ClientResponse(in);
        }

        @Override
        public ClientResponse[] newArray(int size) {
            return new ClientResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(points);
    }

    public String getClientPoints() {
        return points;
    }

    public String get_id() {
        return _id;
    }


}
