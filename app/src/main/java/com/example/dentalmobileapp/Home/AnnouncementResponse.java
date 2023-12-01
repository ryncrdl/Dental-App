package com.example.dentalmobileapp.Home;

import android.os.Parcel;
import android.os.Parcelable;

public class AnnouncementResponse implements Parcelable {

    private String _id;
    private String Title, Context;
    private ImageData image;

    public static class ImageData implements Parcelable {
        private int Subtype;
        private String Data;

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

    protected AnnouncementResponse(Parcel in) {
        _id = in.readString();
        image = in.readParcelable(ImageData.class.getClassLoader());
        Title = in.readString();
        Context = in.readString();

    }
    public static final Creator<AnnouncementResponse> CREATOR = new Creator<AnnouncementResponse>() {
        @Override
        public AnnouncementResponse createFromParcel(Parcel in) {
            return new AnnouncementResponse(in);
        }

        @Override
        public AnnouncementResponse[] newArray(int size) {
            return new AnnouncementResponse[size];
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
        dest.writeString(Context);
    }

    public ImageData getImage() {
        return image;
    }

    public String getAnnouncementName() {
        return Title;
    }

    public String getAnnouncementContext() {
        return Context;
    }


    public String get_id() {
        return _id;
    }


}
