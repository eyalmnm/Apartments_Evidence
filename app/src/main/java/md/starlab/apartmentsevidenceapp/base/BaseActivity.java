package md.starlab.apartmentsevidenceapp.base;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.settings.view.SettingsPreferences;

public class BaseActivity extends AppCompatActivity {

    private String activityTag;

    public void setActivityTag(String tag){
        activityTag = tag;
    }

    public void checkForBackStep(){
        if(false == Dynamic.goBackTag.isEmpty() &&
                false == Dynamic.goBackTag.equals(activityTag)){
            finish();
        }
        else{
            Dynamic.goBackTag = "";
        }
    }

    public void goBackToActivityTag(String tag){
        Dynamic.goBackTag = tag;
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showSetting();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSetting() {
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsPreferences())
                .commit();
    }
}
