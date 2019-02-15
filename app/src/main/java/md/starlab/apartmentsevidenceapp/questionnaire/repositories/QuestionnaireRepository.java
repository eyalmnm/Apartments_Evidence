package md.starlab.apartmentsevidenceapp.questionnaire.repositories;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bikomobile.multipart.Multipart;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import md.starlab.apartmentsevidenceapp.BuildConfig;
import md.starlab.apartmentsevidenceapp.questionnaire.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModelRequest;
import md.starlab.apartmentsevidenceapp.questionnaire.remote.QuestionnaireApiConnection;
import md.starlab.apartmentsevidenceapp.questionnaire.remote.QuestionnaireRetrofit;
import md.starlab.apartmentsevidenceapp.utils.FileUtils;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Ref: https://stackoverflow.com/questions/37049346/upload-file-in-retrofit-2

public class QuestionnaireRepository {
    private static final String TAG = "QuestionnaireRepository";

    public LiveData<DataWrapper<List<QuestionModel>>> getQuestionnaire(String transactionId, String token) {
        final MutableLiveData<DataWrapper<List<QuestionModel>>> liveData = new MutableLiveData<>();
        final DataWrapper<List<QuestionModel>> dataWrapper = new DataWrapper<>();

        QuestionnaireApiConnection apiConnection = QuestionnaireRetrofit.createService(QuestionnaireApiConnection.class);
        String authHeader = "Bearer " + token;
        apiConnection.doGetQuestionnaire(transactionId, authHeader).enqueue(new Callback<List<QuestionModel>>() {
            @Override
            public void onResponse(Call<List<QuestionModel>> call, Response<List<QuestionModel>> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<List<QuestionModel>> call, Throwable t) {
                Log.e(TAG, "getQuestionnaire onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<TransactionAnswerModel>> submitAnswer(double longitude, double latitude, String apartmentId, String transactionId, HashMap<String, Object> answers, String token) {
        final MutableLiveData<DataWrapper<TransactionAnswerModel>> liveData = new MutableLiveData<>();
        final DataWrapper<TransactionAnswerModel> dataWrapper = new DataWrapper<>();

        TransactionAnswerModelRequest answerModel = new TransactionAnswerModelRequest(longitude, latitude, apartmentId, transactionId, answers);
        QuestionnaireApiConnection apiConnection = QuestionnaireRetrofit.createService(QuestionnaireApiConnection.class);
        String authHeader = "Bearer " + token;

        apiConnection.postAnswer(answerModel, authHeader).enqueue(new Callback<TransactionAnswerModel>() {
            @Override
            public void onResponse(Call<TransactionAnswerModel> call, Response<TransactionAnswerModel> response) {
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

    public LiveData<DataWrapper<ResponseBody>> uploadAttachment(Context context, final String transactionId, String token, Uri fileUri, String description) {
        final MutableLiveData<DataWrapper<ResponseBody>> liveData = new MutableLiveData<>();
        final DataWrapper<ResponseBody> dataWrapper = new DataWrapper<>();

        final String authHeader = "Bearer " + token;
        String serverApi = BuildConfig.SERVER_URL + "v1/mobile/transactions/" + transactionId + "/attachments";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authHeader);


        Multipart multipart = new Multipart(context);

        String name = "attachment";
        if (fileUri != null) {
            String filePath = FileUtils.getPathFromUri(context, fileUri);
            int lastFSInx = filePath.lastIndexOf(File.separator);
            String fileName = filePath.substring(lastFSInx + 1);
            multipart.addFile("image/jpeg", name, fileName, fileUri);
            multipart.addParam("description", description);
            multipart.launchRequest(serverApi, headers, new com.android.volley.Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    dataWrapper.setData(ResponseBody.create(MediaType.parse("image/*"), response.data));
                    liveData.setValue(dataWrapper);
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dataWrapper.setThrowable(error);
                    liveData.setValue(dataWrapper);
                }
            });
        }
        return liveData;
    }

}
