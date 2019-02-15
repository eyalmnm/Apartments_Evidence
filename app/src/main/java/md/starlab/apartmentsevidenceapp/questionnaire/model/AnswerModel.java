package md.starlab.apartmentsevidenceapp.questionnaire.model;

import com.google.gson.annotations.SerializedName;

public class AnswerModel {

    @SerializedName("id")
    public String key;
    @SerializedName("title")
    public String value;

    public AnswerModel(String key, String value) {
        this.key = key;
        this.value = value;
    }

//    public AnswerModel(String key) {
//        this.key = key;
//    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}
