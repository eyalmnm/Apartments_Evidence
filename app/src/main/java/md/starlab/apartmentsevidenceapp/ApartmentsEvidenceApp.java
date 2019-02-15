package md.starlab.apartmentsevidenceapp;

import android.app.Application;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import md.starlab.apartmentsevidenceapp.recivers.network.NetworkChangeReceiver;
import md.starlab.apartmentsevidenceapp.view.controllers.NavigationController;

/**
 * Created by Sergey Ostrovsky
 * on 7/5/18
 */
public class ApartmentsEvidenceApp extends Application {

    // Hold instance
    private static ApartmentsEvidenceApp instance;
    private NavigationController navigationController;
    private Gson gson;

    public static ApartmentsEvidenceApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public Gson getGson() {
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gson = gsonBuilder.create();
        }
        return gson;
    }

    public NavigationController getNavigationController(FragmentManager fm) {
        if (null == navigationController) {
            navigationController = new NavigationController(fm);
        }
        return navigationController;
    }


    public void setConnectionListener(NetworkChangeReceiver.NetWorkChangedListener listener) {
        NetworkChangeReceiver.setListener(listener);
    }
}
