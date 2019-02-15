package md.starlab.apartmentsevidenceapp.history.apartment.model;

import com.google.gson.annotations.SerializedName;

public class TransactionAnswerModel {
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("id")
    private String id;
    @SerializedName("apartment")
    private String apartmentId;
    @SerializedName("transaction")
    private String transactionId;
//    @SerializedName("answers")
//    private List answers;


    public TransactionAnswerModel(double longitude, double latitude, String id, String apartmentId, String transactionId) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
        this.apartmentId = apartmentId;
        this.transactionId = transactionId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getId() {
        return id;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
