package md.starlab.apartmentsevidenceapp.buildings.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.List;

import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;
import md.starlab.apartmentsevidenceapp.buildings.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.buildings.remote.BuildingApiController;
import md.starlab.apartmentsevidenceapp.buildings.remote.BuildingsList;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildingsRepository {
    private static final String TAG = "BuildingsRepository";

    public LiveData<DataWrapper<List<BuildingsData>>> getBuildingsByStreet(String streetId, String token) {
        final MutableLiveData<DataWrapper<List<BuildingsData>>> liveData = new MutableLiveData<>();
        final DataWrapper<List<BuildingsData>> dataWrapper = new DataWrapper<>();

        BuildingsList buildingsList = BuildingApiController.createService(BuildingsList.class);
        String authHeader = "Bearer " + token;
        buildingsList.getBuildingByStreetID(streetId, authHeader).enqueue(new Callback<List<BuildingsData>>() {
            @Override
            public void onResponse(Call<List<BuildingsData>> call, Response<List<BuildingsData>> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<List<BuildingsData>> call, Throwable t) {
                Log.e(TAG, "getBuildingsByStreet onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

}
