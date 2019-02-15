package md.starlab.apartmentsevidenceapp.nestedbuilding.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import md.starlab.apartmentsevidenceapp.nestedbuilding.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.MobileApartmentModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NestedBuilding;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequestNoOrder;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.remote.BuildingScheme;
import md.starlab.apartmentsevidenceapp.nestedbuilding.remote.NestedBuildingApiController;
import md.starlab.apartmentsevidenceapp.utils.RetrofitUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";

    public LiveData<DataWrapper<NestedBuilding>> getBuilding(String buildingId, String token) {
        final MutableLiveData<DataWrapper<NestedBuilding>> liveData = new MutableLiveData<>();
        final DataWrapper<NestedBuilding> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.getCitiesAndStreets(buildingId, authHeader).enqueue(new Callback<NestedBuilding>() {
            @Override
            public void onResponse(Call<NestedBuilding> call, Response<NestedBuilding> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<NestedBuilding> call, Throwable t) {
                Log.e(TAG, "getBuilding onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<NewFloorResponse>> addNewFloor(final NewFloorRequest request, String token) {
        final MutableLiveData<DataWrapper<NewFloorResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<NewFloorResponse> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;

        buildingScheme.addNewFloor(request, authHeader).enqueue(new Callback<NewFloorResponse>() {
            @Override
            public void onResponse(Call<NewFloorResponse> call, Response<NewFloorResponse> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    //dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                    dataWrapper.setThrowable(new Throwable(request.getEntrance()));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<NewFloorResponse> call, Throwable t) {
                Log.e(TAG, "addNewFloor onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<NewEntranceResponse>> addNewEntrance(NewEntranceRequest request, String token) {
        final MutableLiveData<DataWrapper<NewEntranceResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<NewEntranceResponse> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.addNewEntrance(request, authHeader).enqueue(new Callback<NewEntranceResponse>() {
            @Override
            public void onResponse(Call<NewEntranceResponse> call, Response<NewEntranceResponse> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<NewEntranceResponse> call, Throwable t) {
                Log.e(TAG, "addNewEntrance onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<NewEntranceResponse>> addNewEntranceNoOrder(NewEntranceRequestNoOrder request, String token) {
        final MutableLiveData<DataWrapper<NewEntranceResponse>> liveData = new MutableLiveData<>();
        final DataWrapper<NewEntranceResponse> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        //buildingScheme.addNewEntrance(request, authHeader).enqueue(new Callback<NewEntranceResponse>() {
        buildingScheme.addNewEntranceNoOrder(request, authHeader).enqueue(new Callback<NewEntranceResponse>() {
            @Override
            public void onResponse(Call<NewEntranceResponse> call, Response<NewEntranceResponse> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<NewEntranceResponse> call, Throwable t) {
                Log.e(TAG, "addNewEntrance onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<String>> removeApartment(String apartmentId, String token) {
        final MutableLiveData<DataWrapper<String>> liveData = new MutableLiveData<>();
        final DataWrapper<String> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.removeApartment(apartmentId, authHeader).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "removeApartment onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<String>> removeFloor(String floorId, String token) {
        final MutableLiveData<DataWrapper<String>> liveData = new MutableLiveData<>();
        final DataWrapper<String> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.removeFloor(floorId, authHeader).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "removeFloor onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<String>> removeEntrance(String entranceId, String token) {
        final MutableLiveData<DataWrapper<String>> liveData = new MutableLiveData<>();
        final DataWrapper<String> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.removeEntrance(entranceId, authHeader).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "removeEntrance onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<MobileApartmentModel>> addApartment(MobileApartmentModel request, String token) {
        final MutableLiveData<DataWrapper<MobileApartmentModel>> liveData = new MutableLiveData<>();
        final DataWrapper<MobileApartmentModel> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.addApartment(request, authHeader).enqueue(new Callback<MobileApartmentModel>() {
            @Override
            public void onResponse(Call<MobileApartmentModel> call, Response<MobileApartmentModel> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<MobileApartmentModel> call, Throwable t) {
                Log.e(TAG, "addApartment onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }

    public LiveData<DataWrapper<MobileApartmentModel>> moveApartment(MobileApartmentModel request, String token) {
        final MutableLiveData<DataWrapper<MobileApartmentModel>> liveData = new MutableLiveData<>();
        final DataWrapper<MobileApartmentModel> dataWrapper = new DataWrapper<>();

        BuildingScheme buildingScheme = NestedBuildingApiController.createService(BuildingScheme.class);
        String authHeader = "Bearer " + token;
        buildingScheme.moveApartment(request.getId(), request, authHeader).enqueue(new Callback<MobileApartmentModel>() {
            @Override
            public void onResponse(Call<MobileApartmentModel> call, Response<MobileApartmentModel> response) {
                if (true == response.isSuccessful()) {
                    dataWrapper.setData(response.body());
                } else {
                    dataWrapper.setThrowable(new Throwable(RetrofitUtils.handleErrorResponse(response)));
                }
                liveData.setValue(dataWrapper);
            }

            @Override
            public void onFailure(Call<MobileApartmentModel> call, Throwable t) {
                Log.e(TAG, "moveApartment onFailure", t);
                dataWrapper.setThrowable(t);
                liveData.setValue(dataWrapper);
            }
        });

        return liveData;
    }
}
