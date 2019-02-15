package md.starlab.apartmentsevidenceapp.questionnaire.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionnaireModel {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("questions")
    public ArrayList<QuestionModel> questions;

    public QuestionnaireModel(String id, String title, ArrayList<QuestionModel> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }
}
