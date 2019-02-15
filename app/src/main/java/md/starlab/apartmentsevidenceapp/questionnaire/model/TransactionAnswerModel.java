package md.starlab.apartmentsevidenceapp.questionnaire.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionAnswerModel {
    @SerializedName("id")
    private String id;
    @SerializedName("apartment")
    private String apartmentId;
    @SerializedName("transaction")
    private String transactionId;
    @SerializedName("answers")
    private AnswerModel answer;

    public TransactionAnswerModel(String apartmentId, String transactionId, AnswerModel answer) {
        this.apartmentId = apartmentId;
        this.transactionId = transactionId;
        this.answer = answer;
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public AnswerModel getAnswer() {
        return answer;
    }

    public String getId() {
        return id;
    }
}
