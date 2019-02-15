package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NestedBuilding implements Parcelable {

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
    @SerializedName("entrances")
    private ArrayList<Entrance> entrances;


    public NestedBuilding(String id, String number, double longitude, double latitude, String street, ArrayList<Entrance> entrances) {
        this.id = id;
        this.number = number;
        this.longitude = longitude;
        this.latitude = latitude;
        this.street = street;
        this.entrances = entrances;
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

    public ArrayList<Entrance> getEntrances() {
        return entrances;
    }


    public static final Parcelable.Creator<NestedBuilding> CREATOR = new Parcelable.Creator<NestedBuilding>() {
        @Override
        public NestedBuilding createFromParcel(Parcel source) {
            return new NestedBuilding(source);
        }

        @Override
        public NestedBuilding[] newArray(int size) {
            return new NestedBuilding[size];
        }
    };

    protected NestedBuilding(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.street = in.readString();
        this.entrances = new ArrayList<Entrance>();
        in.readList(this.entrances, Entrance.class.getClassLoader());
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
        dest.writeList(this.entrances);
    }
}
