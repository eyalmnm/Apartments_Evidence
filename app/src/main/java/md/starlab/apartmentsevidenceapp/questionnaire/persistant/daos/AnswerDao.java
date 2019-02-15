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

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.AnswerEntity;

@Dao
public interface AnswerDao {

    @Query("SELECT * FROM answer_tbl")
    LiveData<List<AnswerEntity>> getAllAnswers();

    @Query("SELECT * FROM answer_tbl WHERE id = :answerId")
    LiveData<AnswerEntity> getAnswerById(String answerId);

    @Query("SELECT * FROM answer_tbl WHERE id IN (:ids)")
    LiveData<List<AnswerEntity>> queryAnswers(List<String> ids);

    @Update
    void updateAnswer(AnswerEntity answerEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAnswer(AnswerEntity answerEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    void insertAnswers(List<AnswerEntity> answersEntities);

    @Delete
    void deleteAnswer(AnswerEntity answerEntity);

    @Query("DELETE FROM answer_tbl")
    void deleteAll();
}
