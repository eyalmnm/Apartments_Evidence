package md.starlab.apartmentsevidenceapp.history.apartment.model;

public class ApartmentTransactionAnswers {

    private String id;
    private String title;
    private String color; // "#FFFFFF"
    private String created_at; // "2018-08-15T13:31:00.765Z",
    private String has_answers;

    public ApartmentTransactionAnswers(String id, String title, String color, String created_at, String has_answers) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.created_at = created_at;
        this.has_answers = has_answers;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getColor() {
        return color;
    }

    public String getCreated_at() {
        int tIdx = created_at.indexOf('T');
        if (-1 < tIdx) {
            return created_at.substring(0, tIdx);
        }
        return created_at;
    }

    public String getHas_answers() {
        return has_answers;
    }
}
