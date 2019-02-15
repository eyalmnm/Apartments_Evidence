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

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionEntity;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM question_tbl")
    LiveData<List<QuestionEntity>> getAllQuestions();

    @Query("SELECT * FROM question_tbl WHERE id = :questionId")
    LiveData<QuestionEntity> getQuestionById(String questionId);

    @Query("SELECT * FROM question_tbl WHERE id IN (:ids)")
    LiveData<List<QuestionEntity>> queryQuestions(List<String> ids);

    @Update
    void updateQuestion(QuestionEntity questionEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(QuestionEntity questionEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    void insertQuestions(List<QuestionEntity> questionsEntities);

    @Delete
    void deleteQuestion(QuestionEntity questionEntity);

    @Query("DELETE FROM question_tbl")
    void deleteAll();
}
