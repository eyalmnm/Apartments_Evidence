package md.starlab.apartmentsevidenceapp.questionnaire.model;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class TransactionAnswerModelRequest {
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("apartment")
    private String apartmentId;
    @SerializedName("transaction")
    private String transactionId;
    @SerializedName("answers")
    private HashMap<String, Object> answer;

    public TransactionAnswerModelRequest(double longitude, double latitude, String apartmentId, String transactionId, HashMap<String, Object> answer) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.apartmentId = apartmentId;
        this.transactionId = transactionId;
        this.answer = answer;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public HashMap<String, Object> getAnswer() {
        return answer;
    }
}
