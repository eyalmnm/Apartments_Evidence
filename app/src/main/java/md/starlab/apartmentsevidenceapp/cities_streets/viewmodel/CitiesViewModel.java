package md.starlab.apartmentsevidenceapp.cities_streets.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.cities_streets.repository.Repository;

public class CitiesViewModel extends AndroidViewModel {

    private Repository repository;


    public CitiesViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public LiveData<DataWrapper<List<CityData>>> getCitiesAndStreets(String token) {
        return repository.getCitieList(token);
    }
}
