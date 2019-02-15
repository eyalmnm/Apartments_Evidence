package md.starlab.apartmentsevidenceapp.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import md.starlab.apartmentsevidenceapp.ApartmentsEvidenceApp;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.login.view.LoginActivity;
import md.starlab.apartmentsevidenceapp.view.controllers.NavigationController;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Sergey Ostrovsky
 * on 7/5/18
 */
public class MainActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    private static final String TAG = "MainActivity";

    private static final int RC_APP_PERMISSION = 123;

    // Activity properties
    private Context context;
    private NavigationController controller;

    // Application's permissions
    private String[] appPermissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Log.d(TAG, "onCreate");

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Dynamic.screenWidth = displaymetrics.widthPixels;
        Dynamic.screenHeight = displaymetrics.heightPixels;

        LoginActivity loginFragment = new LoginActivity();
        controller = ((ApartmentsEvidenceApp) getApplication()).getNavigationController(getSupportFragmentManager());
        //controller.showFragment(loginFragment);
    }


    // ******************************************   Permissions Handler Methods   ************************************
    @AfterPermissionGranted(RC_APP_PERMISSION)
    public void appPermissionsTask() {
        // TODO Check if it not happen when permissions have been pre granted
        if (true == hasAppPermissions()) {
            Log.d(TAG, "appPermissionsTask");
            // TODO Check if it not happen when permissions have been pre granted
            //moveToNextScreen();
        } else {
            // Ask for permissions again
            EasyPermissions.requestPermissions(this,
                    context.getString(R.string.must_garnt_permission),
                    RC_APP_PERMISSION,
                    appPermissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private boolean hasAppPermissions() {
        return EasyPermissions.hasPermissions(this, appPermissions);
    }


    // EasyPermissions.PermissionCallbacks implementation
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
        // TODO Check if it not happen when permissions have been pre granted
    }

    // EasyPermissions.PermissionCallbacks implementation
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasAppPermissions() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();

            // Check if it not happen when permissions have been pre granted
        }
    }

    // EasyPermissions.RationaleCallbacks
    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    //EasyPermissions.RationaleCallbacks
    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }
}
