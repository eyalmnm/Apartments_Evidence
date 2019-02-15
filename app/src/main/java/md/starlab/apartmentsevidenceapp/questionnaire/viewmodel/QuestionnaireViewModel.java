package md.starlab.apartmentsevidenceapp.questionnaire.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import md.starlab.apartmentsevidenceapp.questionnaire.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.repositories.QuestionnaireRepository;
import okhttp3.ResponseBody;

// Ref: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#0

public class QuestionnaireViewModel extends AndroidViewModel {

    private QuestionnaireRepository repository;

    public QuestionnaireViewModel(@NonNull Application application) {
        super(application);
        repository = new QuestionnaireRepository();
    }

    /**
     * Query Questionnaire by apartment Id
     *
     * @param id    apartment Id
     * @param token login token
     * @return Questionnaire as List of questionss. If title contains the word "ERROR:" and/or question list is null, it means network error.
     */
    public LiveData<DataWrapper<List<QuestionModel>>> getQuestionnaireByID(String id, String token) {
        return repository.getQuestionnaire(id, token);
    }


    public LiveData<DataWrapper<TransactionAnswerModel>> submitAnswer(double longitude, double latitude, String apartmentId, String transactionId, HashMap<String, Object> answers, String token) {
        return repository.submitAnswer(longitude, latitude, apartmentId, transactionId, answers, token);
    }

    public LiveData<DataWrapper<ResponseBody>> uploadAttachment(Context context, String transactionId, String token, Uri fileUri, String description) {
        return repository.uploadAttachment(context, transactionId, token, fileUri, description);
    }
}