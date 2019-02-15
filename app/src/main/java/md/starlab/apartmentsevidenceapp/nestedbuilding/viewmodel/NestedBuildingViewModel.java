package md.starlab.apartmentsevidenceapp.nestedbuilding.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import md.starlab.apartmentsevidenceapp.nestedbuilding.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.MobileApartmentModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NestedBuilding;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceRequestNoOrder;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorRequest;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.repository.Repository;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class NestedBuildingViewModel extends AndroidViewModel {

    private Repository repository;


    public NestedBuildingViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public LiveData<DataWrapper<NestedBuilding>> getBuilding(String buildingId, String token) {
        return repository.getBuilding(buildingId, token);
    }

    public LiveData<DataWrapper<NewFloorResponse>> addNewFloor(String title, int order, String entranceId, String token) {
        NewFloorRequest request = new NewFloorRequest(title, order, entranceId);
        return repository.addNewFloor(request, token);
    }

    public LiveData<DataWrapper<NewEntranceResponse>> addNewEntrance(String title, String order, String buildingId, int numOfFloors, String token) {
        if (StringUtils.isNullOrEmpty(order)) {
        //if (null == order) {
            NewEntranceRequestNoOrder request = new NewEntranceRequestNoOrder(title, buildingId, numOfFloors);
            return repository.addNewEntranceNoOrder(request, token);
        } else {
            NewEntranceRequest request = new NewEntranceRequest(title, order, buildingId, numOfFloors);
            return repository.addNewEntrance(request, token);
        }
    }

    public LiveData<DataWrapper<String>> removeApartment(String apartmentId, String token) {
        return repository.removeApartment(apartmentId, token);
    }

    public LiveData<DataWrapper<String>> removeFloor(String floorId, String token) {
        return repository.removeFloor(floorId, token);
    }

    public LiveData<DataWrapper<String>> removeEntrance(String entranceId, String token) {
        return repository.removeEntrance(entranceId, token);
    }

    public LiveData<DataWrapper<MobileApartmentModel>> addApartment(String id, String number, String floor, String token) {
        MobileApartmentModel request = new MobileApartmentModel(id, number, floor);
        return repository.addApartment(request, token);
    }

    public LiveData<DataWrapper<MobileApartmentModel>> moveApartment(String apartmentId, String apartmentNumber, String floorId, String token) {
        MobileApartmentModel request = new MobileApartmentModel(apartmentId, apartmentNumber, floorId);
        return repository.moveApartment(request, token);
    }
}
