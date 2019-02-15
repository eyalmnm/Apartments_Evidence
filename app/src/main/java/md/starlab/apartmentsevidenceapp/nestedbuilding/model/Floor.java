package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Floor implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("order")
    private String order;
    @SerializedName("apartments")
    private ArrayList<Apartment> apartments;

    public Floor(String id, String number, String order, ArrayList<Apartment> apartments) {
        this.id = id;
        this.number = number;
        this.order = order;
        this.apartments = apartments;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getOrder() {
        return order;
    }

    public ArrayList<Apartment> getApartments() {
        return apartments;
    }

    public boolean hasApartments() {
        return (null != apartments) && (false == apartments.isEmpty());
    }


    public static final Parcelable.Creator<Floor> CREATOR = new Parcelable.Creator<Floor>() {
        @Override
        public Floor createFromParcel(Parcel source) {
            return new Floor(source);
        }

        @Override
        public Floor[] newArray(int size) {
            return new Floor[size];
        }
    };

    protected Floor(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.order = in.readString();
        this.apartments = in.createTypedArrayList(Apartment.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.number);
        dest.writeString(this.order);
        dest.writeTypedList(this.apartments);
    }
}
