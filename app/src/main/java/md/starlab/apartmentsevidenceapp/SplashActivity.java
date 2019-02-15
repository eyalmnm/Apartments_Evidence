package md.starlab.apartmentsevidenceapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import butterknife.BindView;
import butterknife.ButterKnife;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.login.view.LoginActivity;
import md.starlab.apartmentsevidenceapp.utils.AppUtils;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    private static final int SPLASH_SCREEN_DURATION = 2500;

    // UI Components
    @BindView(R.id.version_number)
    public TextView versionNumber;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onCreate");
        context = this;
        ButterKnife.bind(this);
        FirebaseCrash.log("Activity created");

        // Calculate the current screen size. (Never remove)
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Dynamic.screenWidth = displaymetrics.widthPixels;
        Dynamic.screenHeight = displaymetrics.heightPixels;

        // Display the current build version and code
        versionNumber.setText(getString(R.string.app_version_format,
                AppUtils.getAppVersion(this), AppUtils.getAppVersionCode(this)));

        // Move to the next screen after display duration
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToNextScreen();
            }
        }, SPLASH_SCREEN_DURATION);
    }

    private void moveToNextScreen() {
        Intent intent = new Intent(context, LoginActivity.class);
//        Intent intent = new Intent(context, CitiesAndStreetsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out_none);
        finish();
    }
}
