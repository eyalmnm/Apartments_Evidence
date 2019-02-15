package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

public class NewFloorRequest {

    @SerializedName("number")
    private String number;
    @SerializedName("order")
    private int order;
    @SerializedName("entrance")
    private String entrance;

    public NewFloorRequest(String number, int order, String entrance) {
        this.number = number;
        this.order = order;
        this.entrance = entrance;
    }

    public String getNumber() {
        return number;
    }

    public int getOrder() {
        return order;
    }

    public String getEntrance() {
        return entrance;
    }
}
