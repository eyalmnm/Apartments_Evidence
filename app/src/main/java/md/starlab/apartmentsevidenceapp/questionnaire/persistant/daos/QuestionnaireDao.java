package md.starlab.apartmentsevidenceapp.questionnaire.persistant.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.WorkerThread;

import java.util.List;

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionnaireEntity;

@Dao
public interface QuestionnaireDao {

    @Query("SELECT * FROM questionnaire_tbl")
    LiveData<List<QuestionnaireEntity>> getAllQuestionnaires();

    @Query("SELECT * FROM questionnaire_tbl WHERE id = :questionnaireId")
    LiveData<QuestionnaireEntity> getQuestionnairesById(String questionnaireId);

    @Query("SELECT * FROM questionnaire_tbl WHERE id = :questionnaireId")
    QuestionnaireEntity findQuestionnairesById(String questionnaireId);

    @Query("SELECT * FROM questionnaire_tbl WHERE id IN (:ids)")
    LiveData<List<QuestionnaireEntity>> queryQuestionnaires(List<String> ids);

    @Update
    void updateQuestionnaire(QuestionnaireEntity answerEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestionnaire(QuestionnaireEntity questionnaireEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    void insertQuestionnaires(List<QuestionnaireEntity> questionnaireEntities);

    @Delete
    void deleteQuestionnaire(QuestionnaireEntity questionnaireEntity);

    @Query("DELETE FROM questionnaire_tbl")
    void deleteAll();
}
