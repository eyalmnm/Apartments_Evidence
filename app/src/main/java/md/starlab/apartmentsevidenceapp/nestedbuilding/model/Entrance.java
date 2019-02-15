package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Entrance implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("order")
    private String order;
    @SerializedName("floors")
    private ArrayList<Floor> floors;

    public Entrance(String id, String number, String order, ArrayList<Floor> floors) {
        this.id = id;
        this.number = number;
        this.order = order;
        this.floors = floors;
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

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public boolean hasApartments() {
        if(null != floors) {
            for(Floor floor: floors) {
                if(floor.hasApartments()) {
                    return true;
                }
            }
        }
        return false;
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
        dest.writeTypedList(this.floors);
    }

    protected Entrance(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.order = in.readString();
        this.floors = in.createTypedArrayList(Floor.CREATOR);
    }

    public static final Parcelable.Creator<Entrance> CREATOR = new Parcelable.Creator<Entrance>() {
        @Override
        public Entrance createFromParcel(Parcel source) {
            return new Entrance(source);
        }

        @Override
        public Entrance[] newArray(int size) {
            return new Entrance[size];
        }
    };
}
