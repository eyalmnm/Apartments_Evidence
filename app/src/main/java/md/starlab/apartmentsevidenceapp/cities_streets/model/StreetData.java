package md.starlab.apartmentsevidenceapp.cities_streets.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StreetData implements Parcelable {

    public static final Creator<StreetData> CREATOR = new Creator<StreetData>() {
        @Override
        public StreetData createFromParcel(Parcel source) {
            return new StreetData(source);
        }

        @Override
        public StreetData[] newArray(int size) {
            return new StreetData[size];
        }
    };
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String name;

    public StreetData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected StreetData(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // **************************   Parcelable Methods   *******************************************
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
