package md.starlab.apartmentsevidenceapp.history.apartment.model;

public class AnswerModel {

    public String questionId;
    public Object answer;

    public AnswerModel(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    public String getId() {
        return questionId;
    }

    public Object getAnswer() {
        return answer;
    }
}
