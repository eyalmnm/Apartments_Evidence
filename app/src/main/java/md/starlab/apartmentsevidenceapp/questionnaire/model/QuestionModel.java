package md.starlab.apartmentsevidenceapp.questionnaire.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class QuestionModel {

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("type")
    public String type;

    @SerializedName("answers")
    public ArrayList<AnswerModel> answers;

    public QuestionModel(String id, String title, String type, ArrayList<AnswerModel> answers) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public ArrayList<AnswerModel> getAnswers() {
        return answers;
    }
}
