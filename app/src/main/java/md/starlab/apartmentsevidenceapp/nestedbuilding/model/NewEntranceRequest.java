package md.starlab.apartmentsevidenceapp.nestedbuilding.model;

import com.google.gson.annotations.SerializedName;

import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class NewEntranceRequest extends NewEntranceRequestNoOrder {

    @SerializedName("order")
    private int order;

    public NewEntranceRequest(String number, String order, String building, int number_of_floors) {
        super(number, building, number_of_floors);
        if (false == StringUtils.isNullOrEmpty(order)) {
            this.order = Integer.parseInt(order);
        }
    }

    public int getOrder() {
        return order;
    }
}
