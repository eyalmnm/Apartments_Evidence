package md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName = "questionnaire_tbl")
public class QuestionnaireEntity {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "questions")
    private ArrayList<QuestionEntity> questions;

    public QuestionnaireEntity(@NonNull String id, @NonNull String title, @NonNull ArrayList<QuestionEntity> questions) {
        this.id = id;
        this.title = title;
        this.questions = questions;
    }

    @Ignore
    public QuestionnaireEntity() {
        this.id = "";
        this.title = "";
        this.questions = null;
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
    public ArrayList<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(@NonNull ArrayList<QuestionEntity> questions) {
        this.questions = questions;
    }
}
