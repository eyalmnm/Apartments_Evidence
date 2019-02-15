package md.starlab.apartmentsevidenceapp.cities_streets.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CityData implements Parcelable {


    public static final Creator<CityData> CREATOR = new Creator<CityData>() {
        @Override
        public CityData createFromParcel(Parcel source) {
            return new CityData(source);
        }

        @Override
        public CityData[] newArray(int size) {
            return new CityData[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String name;
    @SerializedName("streets")
    private ArrayList<StreetData> streets;

    public CityData(String id, String name, ArrayList<StreetData> streets) {
        this.id = id;
        this.name = name;
        this.streets = streets;
    }

    protected CityData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.streets = in.createTypedArrayList(StreetData.CREATOR);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // **************************   Parcelable Methods   *******************************************

    public ArrayList<StreetData> getStreets() {
        return streets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.streets);
    }
}
