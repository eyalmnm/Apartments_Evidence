package md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName = "question_tbl")
public class QuestionEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "answers")
    private ArrayList<AnswerEntity> answers;

    public QuestionEntity(@NonNull String id, @NonNull String title, @NonNull String type, ArrayList<AnswerEntity> answers) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.answers = answers;
    }

    @Ignore
    public QuestionEntity() {
        this.id = "";
        this.title = "";
        this.type = "";
        this.answers = null;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public ArrayList<AnswerEntity> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<AnswerEntity> answers) {
        this.answers = answers;
    }
}
