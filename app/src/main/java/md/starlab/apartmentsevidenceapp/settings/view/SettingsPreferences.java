package md.starlab.apartmentsevidenceapp.settings.view;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import md.starlab.apartmentsevidenceapp.R;

public class SettingsPreferences extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.app_preference, rootKey);
    }
}
