package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class NewEntranceRequestNoOrder {

    @SerializedName("number")
    private String number;
    @SerializedName("building")
    private String building;
    @SerializedName("number_of_floors")
    private int number_of_floors;

    public NewEntranceRequestNoOrder(String number, String building, int number_of_floors) {
        this.number = number;
        this.building = building;
        this.number_of_floors = number_of_floors;
    }

    public String getNumber() {
        return number;
    }

    public String getBuilding() {
        return building;
    }

    public int getNumber_of_floors() {
        return number_of_floors;
    }
}
