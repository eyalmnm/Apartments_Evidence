package md.starlab.apartmentsevidenceapp.history.apartment.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import md.starlab.apartmentsevidenceapp.history.apartment.model.ApartmentTransactionAnswers;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.history.apartment.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.history.apartment.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.history.apartment.repository.Repository;

public class ApartmentHistoryViewModel extends AndroidViewModel {

    private Repository repository;

    public ApartmentHistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public LiveData<DataWrapper<List<BuildingAssignedTransaction>>> getTransaction(String apartmentId, String token) {
        return repository.getTransaction(apartmentId, token);
    }

    public LiveData<DataWrapper<List<ApartmentTransactionAnswers>>> getHistory(String apartmentId, String token) {
        return repository.getHistory(apartmentId, token);
    }

    public LiveData<DataWrapper<TransactionAnswerModel>> submitAnswer(double longitude, double latitude, String apartmentId, String transactionId, String token) {
        return repository.submitAnswer(longitude, latitude, apartmentId, transactionId, token);
    }
}
