package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Apartment implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("color")
    private String status;

    public Apartment(String id, String number, String status) {
        this.id = id;
        this.number = number;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }


    public static final Parcelable.Creator<Apartment> CREATOR = new Parcelable.Creator<Apartment>() {
        @Override
        public Apartment createFromParcel(Parcel source) {
            return new Apartment(source);
        }

        @Override
        public Apartment[] newArray(int size) {
            return new Apartment[size];
        }
    };

    protected Apartment(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.number);
        dest.writeString(this.status);
    }
}
