package md.starlab.apartmentsevidenceapp.cities_streets.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.cities_streets.remote.ApiController;
import md.starlab.apartmentsevidenceapp.cities_streets.remote.CitiesAndStreets;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";

    public LiveData<DataWrapper<List<CityData>>> getCitieList(String token) {
        final MutableLiveData<DataWrapper<List<CityData>>> liveData = new MutableLiveData<>();
        final DataWrapper<List<CityData>> dataWrapper = new DataWrapper<>();

        CitiesAndStreets citiesAndStreets = ApiController.createService(CitiesAndStreets.class);
        String authHeader = "Bearer " + token;
        citiesAndStreets.getCitiesAndStreets(authHeader).enqueue(new Callback<List<CityData>>() {
            @Override
            public void onResponse(Call<List<CityData>> call, Response<List<CityData>> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<List<CityData>> call, Throwable t) {
                Log.e(TAG, "Cities onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }
}
