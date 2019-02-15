package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

public class NewFloorResponse extends NewFloorRequest {

    @SerializedName("id")
    private String id;

    public NewFloorResponse(String number, int order, String entrance, String id) {
        super(number, order, entrance);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
