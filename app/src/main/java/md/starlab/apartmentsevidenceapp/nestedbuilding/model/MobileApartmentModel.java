package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sergey Ostrovsky
 * on 9/12/18
 */
public class MobileApartmentModel {
    @SerializedName("id")
    private String id;
    @SerializedName("number")
    private String number;
    @SerializedName("floor")
    private String floor;

    public MobileApartmentModel(String id, String number, String floor) {
        this.id = id;
        this.number = number;
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getFloor() {
        return floor;
    }
}