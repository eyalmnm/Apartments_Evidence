package md.starlab.apartmentsevidenceapp.buildings.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;
import md.starlab.apartmentsevidenceapp.buildings.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.buildings.repository.BuildingsRepository;

public class BuildingsViewModel extends AndroidViewModel {

    private BuildingsRepository repository;

    public BuildingsViewModel(@NonNull Application application) {
        super(application);
        repository = new BuildingsRepository();
    }

    public LiveData<DataWrapper<List<BuildingsData>>> getBuildingsByStreetId(String streetId, String token) {
        return repository.getBuildingsByStreet(streetId, token);
    }
}
