package md.starlab.apartmentsevidenceapp.buildings.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import md.starlab.apartmentsevidenceapp.R;

public class BuildingsData implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("street")
    private String street;
    @SerializedName("urgency")
    private int urgency;

    public BuildingsData(String id, String number, double longitude, double latitude, String street, int urgency) {
        this.id = id;
        this.number = number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.urgency = urgency;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getStreet() {
        return street;
    }

    public int getUrgency() {
        return urgency;
    }

    public int getColorId() {
        if (1 == urgency) {
            return R.color.buildings_red;
        } else if (2 == urgency) {
            return R.color.buildings_yellow;
        } else if (3 == urgency) {
            return R.color.buildings_green;
        }
        return R.color.colorWhite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.number);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.street);
        dest.writeInt(this.urgency);
    }

    protected BuildingsData(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.street = in.readString();
        this.urgency = in.readInt();
    }

    public static final Creator<BuildingsData> CREATOR = new Creator<BuildingsData>() {
        @Override
        public BuildingsData createFromParcel(Parcel source) {
            return new BuildingsData(source);
        }

        @Override
        public BuildingsData[] newArray(int size) {
            return new BuildingsData[size];
        }
    };
}
