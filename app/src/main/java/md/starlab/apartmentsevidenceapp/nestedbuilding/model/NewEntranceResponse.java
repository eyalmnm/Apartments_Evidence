package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

public class NewEntranceResponse extends NewEntranceRequest {

    @SerializedName("id")
    private String id;

    public NewEntranceResponse(String number, String order, String building, int number_of_floors, String id) {
        super(number, order, building, number_of_floors);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
