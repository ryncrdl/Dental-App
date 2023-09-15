package com.example.dentalmobileapp.Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

public class ServiceResponse implements Parcelable {

    private String _id;
    private String Title, Description, Price, Payment;
    private ImageData image;

    public static class ImageData implements Parcelable {
        private int Subtype;
        private String Data; // This field will store the Base64-encoded image data

        protected ImageData(Parcel in) {
            Subtype = in.readInt();
            Data = in.readString();
        }

        public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
            @Override
            public ImageData createFromParcel(Parcel in) {
                return new ImageData(in);
            }

            @Override
            public ImageData[] newArray(int size) {
                return new ImageData[size];
            }
        };

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(Subtype);
            dest.writeString(Data);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        // Getter and Setter for Data field
        public String getData() {
            return Data;
        }

        public void setData(String data) {
            Data = data;
        }
    }

    protected ServiceResponse(Parcel in) {
        _id = in.readString();
        image = in.readParcelable(ImageData.class.getClassLoader());
        Title = in.readString();
        Description = in.readString();
        Price = in.readString();
        Payment = in.readString();
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
        dest.writeParcelable(image, flags);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeString(Price);
        dest.writeString(Payment);
    }

    public ImageData getImage() {
        return image;
    }

    public String getServiceName() {
        return Title;
    }

    public String getServiceDescription() {
        return Description;
    }

    public String getServicePrice() {
        return Price;
    }

    public String getServicePayment() {
        return Payment;
    }

    public String get_id() {
        return _id;
    }


}
