package md.starlab.apartmentsevidenceapp.history.apartment.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import md.starlab.apartmentsevidenceapp.history.apartment.model.ApartmentTransactionAnswers;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.history.apartment.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.history.apartment.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.history.apartment.remote.ApartmentHistory;
import md.starlab.apartmentsevidenceapp.history.apartment.remote.ApartmentHistoryApiController;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";

    public LiveData<DataWrapper<List<BuildingAssignedTransaction>>> getTransaction(String apartmentId, String token) {
        final MutableLiveData<DataWrapper<List<BuildingAssignedTransaction>>> liveData = new MutableLiveData<>();
        final DataWrapper<List<BuildingAssignedTransaction>> dataWrapper = new DataWrapper<>();
        ApartmentHistory apartmentHistory = ApartmentHistoryApiController.createService(ApartmentHistory.class);
        String authHeader = "Bearer " + token;
        apartmentHistory.getTransaction(apartmentId, authHeader).enqueue(new Callback<List<BuildingAssignedTransaction>>() {
            @Override
            public void onResponse(Call<List<BuildingAssignedTransaction>> call, Response<List<BuildingAssignedTransaction>> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<List<BuildingAssignedTransaction>> call, Throwable t) {
                Log.e(TAG, "getTransaction onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<List<ApartmentTransactionAnswers>>> getHistory(String apartmentId, String token) {
        final MutableLiveData<DataWrapper<List<ApartmentTransactionAnswers>>> liveData = new MutableLiveData<>();
        final DataWrapper<List<ApartmentTransactionAnswers>> dataWrapper = new DataWrapper<>();
        ApartmentHistory apartmentHistory = ApartmentHistoryApiController.createService(ApartmentHistory.class);
        String authHeader = "Bearer " + token;
        apartmentHistory.getHistory(apartmentId, authHeader).enqueue(new Callback<List<ApartmentTransactionAnswers>>() {
            @Override
            public void onResponse(Call<List<ApartmentTransactionAnswers>> call, Response<List<ApartmentTransactionAnswers>> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<List<ApartmentTransactionAnswers>> call, Throwable t) {
                Log.e(TAG, "getHistory onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<TransactionAnswerModel>> submitAnswer(double longitude, double latitude, String apartmentId, String transactionId, String token) {
        final MutableLiveData<DataWrapper<TransactionAnswerModel>> liveData = new MutableLiveData<>();
        final DataWrapper<TransactionAnswerModel> dataWrapper = new DataWrapper<>();

        TransactionAnswerModel answerModel = new TransactionAnswerModel(longitude, latitude, "", apartmentId, transactionId);
        String authHeader = "Bearer " + token;

        ApartmentHistory apartmentHistory = ApartmentHistoryApiController.createService(ApartmentHistory.class);
        apartmentHistory.postAnswer(answerModel, authHeader).enqueue(new Callback<TransactionAnswerModel>() {
            @Override
            public void onResponse(Call<TransactionAnswerModel> call, Response<TransactionAnswerModel> response) {
//                if (response.body() != null) {
//                    String post = response.body().toString();
//                }
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<TransactionAnswerModel> call, Throwable t) {
                Log.e(TAG, "submitAnswer onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });
        return liveData;
    }

}
