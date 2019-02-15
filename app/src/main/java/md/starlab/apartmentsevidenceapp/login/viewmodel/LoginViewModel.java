package md.starlab.apartmentsevidenceapp.login.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import md.starlab.apartmentsevidenceapp.login.Repository;
import md.starlab.apartmentsevidenceapp.login.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.login.model.LoginServerResponse;

public class LoginViewModel extends AndroidViewModel {

    private Repository repository;


    public LoginViewModel(Application application) {
        super(application);
        repository = new Repository();
    }


    public LiveData<DataWrapper<LoginServerResponse>> doLogin(String userName, String password) {
        return repository.login(userName, password);
    }

    public LiveData<DataWrapper<String>> logout(String token) {
        return repository.logout(token);
    }
}
