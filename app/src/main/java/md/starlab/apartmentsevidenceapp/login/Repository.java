package md.starlab.apartmentsevidenceapp.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import md.starlab.apartmentsevidenceapp.login.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.login.model.LoginCredentials;
import md.starlab.apartmentsevidenceapp.login.model.LoginServerResponse;
import md.starlab.apartmentsevidenceapp.login.remote.ApiController;
import md.starlab.apartmentsevidenceapp.login.remote.Login;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";

    public LiveData<DataWrapper<LoginServerResponse>> login(String userName, String password) {
        final MutableLiveData<DataWrapper<LoginServerResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<LoginServerResponse> dataWrapper = new DataWrapper<>();

        Login login = ApiController.createService(Login.class);
        login.loginWithCredentials(new LoginCredentials(userName, password)).enqueue(new Callback<LoginServerResponse>() {
            @Override
            public void onResponse(Call<LoginServerResponse> call, Response<LoginServerResponse> response) {
                Log.d(TAG, "login success");
                if (response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<LoginServerResponse> call, Throwable t) {
                Log.e(TAG, "login onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<String>> logout(String token) {
        final MutableLiveData<DataWrapper<String>> liveData = new MutableLiveData<>();
        final DataWrapper<String> dataWrapper = new DataWrapper<>();

        Login login = ApiController.createService(Login.class);
        String authHeader = "Bearer " + token;
        login.logout(authHeader).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.toString());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "logout onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }
}
