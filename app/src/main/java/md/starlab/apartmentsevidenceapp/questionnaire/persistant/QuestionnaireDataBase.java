package md.starlab.apartmentsevidenceapp.questionnaire.persistant;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import md.starlab.apartmentsevidenceapp.questionnaire.persistant.converters.AnswersConverter;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.converters.QuestionsConverter;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.daos.AnswerDao;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.daos.QuestionDao;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.daos.QuestionnaireDao;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.AnswerEntity;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionEntity;
import md.starlab.apartmentsevidenceapp.questionnaire.persistant.entities.QuestionnaireEntity;


/**
 * Notice: Change version number when changing the database components (DB, DAO, Entity)
 */
@Database(entities = {QuestionnaireEntity.class, QuestionEntity.class, AnswerEntity.class}, version = 1, exportSchema = false)
@TypeConverters({AnswersConverter.class, QuestionsConverter.class})
public abstract class QuestionnaireDataBase extends RoomDatabase {
    private static final String TAG = "QuestionnaireDataBase";

    // Properties initialization
    private static QuestionnaireDataBase instance;
    private static Context ctx;
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //Toast.makeText(ctx, "*****  Going to clean the DB in QuestionnaireDataBase  *****", Toast.LENGTH_LONG).show();
            Log.w(TAG, "*****  Going to clean the DB  *****");
            instance.questionnaireDao().deleteAll();
            instance.questionDao().deleteAll();
            instance.answerDao().deleteAll();
        }
    };

    public static QuestionnaireDataBase getInstance(final Context context) {
        if (null == instance) {
            synchronized (QuestionnaireDataBase.class) {
                if (null == instance) {
                    ctx = context;
                    instance = Room.databaseBuilder(context, QuestionnaireDataBase.class, "QuestionnaireDataBase")
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return instance;
    }

    // DAOs' implementation
    public abstract QuestionnaireDao questionnaireDao();

    public abstract QuestionDao questionDao();

    public abstract AnswerDao answerDao();
}
