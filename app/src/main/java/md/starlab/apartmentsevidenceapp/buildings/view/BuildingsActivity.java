package md.starlab.apartmentsevidenceapp.buildings.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.ApartmentsEvidenceApp;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.base.BaseActivity;
import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;
import md.starlab.apartmentsevidenceapp.buildings.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.buildings.viewmodel.BuildingsViewModel;
import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.StreetData;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.dialogs.AppExitDialog;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.NestedBuildingActivity;
import md.starlab.apartmentsevidenceapp.recivers.network.NetworkChangeReceiver;
import md.starlab.apartmentsevidenceapp.services.location.locationListenerService;
import md.starlab.apartmentsevidenceapp.ui_widgets.buildings.BuildingsGridRecyclerView;
import md.starlab.apartmentsevidenceapp.ui_widgets.buildings.RecyclerTouchListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BuildingsActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "BuildingsActivity";

    // Permissions components
    private static final int RC_APP_PERMISSION = 123;

    // UI Components
    @BindView(R.id.buildingsHeaderRootView)
    public View headerRootView;
    @BindView(R.id.buildingsUserNameTextView)
    public TextView userNameTextView;
    @BindView(R.id.buildingsCityNameTextView)
    public TextView cityNameTextView;
    @BindView(R.id.buildingsStreetNameTextView)
    public TextView streetNameTextView;
    @BindView(R.id.buildingsNoGpsWarningImageView)
    public ImageView noGpsImageView;
    @BindView(R.id.buildingsNoCellularWarningImageView)
    public ImageView noNetworkImageView;
    @BindView(R.id.buildingsGridRecyclerView)
    public BuildingsGridRecyclerView recyclerView;
    //public RecyclerView recyclerView;


    // Incoming data
    private CityData cityData;
    private StreetData streetData;
    private String streetName;
    private String streetId;
    private String[] appPermissions = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    // Grid components
    private BuildingsGridAdapter adapter;
    private ArrayList<BuildingsData> buildingsDatas = new ArrayList<>();

    // Buildings ViewModel
    private BuildingsViewModel viewModel;

    // Connection and Location Indicators
    private NetworkChangeReceiver.NetWorkChangedListener networkListener;
    private BroadcastReceiver locationReceiver;
    private boolean isGpsAvailable = false;
    private boolean isNetworkAvailable = false;

    // Helpers
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTag(Dynamic.tagBuildings);
        setContentView(R.layout.activity_buildings);
        ButterKnife.bind(this);
        context = this;

        // Init Basic data
        Intent intent = getIntent();
        cityData = intent.getParcelableExtra(Constants.NAME_CITY_DATA);
        streetData = intent.getParcelableExtra(Constants.NAME_STREET_DATA);
        streetName = streetData.getName();
        streetId = streetData.getId();
        userNameTextView.setText(Dynamic.USERNAME);

        // Header components
        userNameTextView.setText(String.valueOf(Dynamic.USERNAME));
        cityNameTextView.setText(String.valueOf(cityData.getName()));
        streetNameTextView.setText(String.valueOf(streetName));

        // Init ViewModel and retrieve data
        viewModel = ViewModelProviders.of(this).get(BuildingsViewModel.class);
        loadingBuildingsData();

        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
    }

    public void onResume() {
        super.onResume();
        checkForBackStep();
        getPermissions();
    }

    private void loadingBuildingsData() {
        viewModel.getBuildingsByStreetId(streetId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<List<BuildingsData>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<BuildingsData>> listDataWrapper) {
                if (null != progressDialog) progressDialog.hide();
                Log.d(TAG, "loadingBuildingsData onChanged");
                if (null != listDataWrapper.getThrowable()) {
                    Toast.makeText(context, getString(R.string.cities_error_message, listDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    List<BuildingsData> list = listDataWrapper.getData();
                    updateBuildingsList(list);
                }
            }
        });
    }

    private void updateBuildingsList(List<BuildingsData> list) {
        if (null != list && false == list.isEmpty()) {
            buildingsDatas.addAll(list);
            if (null != buildingsDatas && 0 < buildingsDatas.size()) {
                Collections.sort(buildingsDatas, new Comparator<BuildingsData>() {
                    @Override
                    public int compare(BuildingsData o1, BuildingsData o2) {
                        int first = stringToNumber( o1.getNumber());
                        int second = stringToNumber( o2.getNumber());
                        return first - second;
                    }
                });
            }
        }

        adapter = new BuildingsGridAdapter(context, buildingsDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 10));
        int resId = R.anim.layout_animation_grid_items_slide_from_bottom;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                handleItemClicked(buildingsDatas.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                // Do Nothing
            }
        }));
        recyclerView.setAdapter(adapter);
    }

    private int stringToNumber(String str){
        int number = 0;
        String s = str;
        String match = "[^0-9]";
        Matcher m = Pattern.compile(match).matcher(s);
        if (m.find()) {
            s = s.substring(0, m.end() - 1);
        }
        if(!s.isEmpty()) {
            number = Integer.parseInt(s);
        }
        return number;
    }

    private int getNumber(String number) {
        int slashInx = number.indexOf("/");
        if (0 < slashInx) {
            String num = number.substring(0, slashInx);
            return Integer.parseInt(num);
        } else {
            return Integer.parseInt(number);
        }
    }

    @OnClick(R.id.buildingsLogoutIcon)
    public void showLogoutDialog(View view) {
        AppExitDialog dialog = new AppExitDialog();
        dialog.show(getSupportFragmentManager(), "AppExitDialog");
    }

    @OnClick({R.id.buildingsCityNameTextView})
    public void goBackToCitiesScreen(View view) {
        goBackToActivityTag(Dynamic.tagCitiesAndStreets);
        finish();
    }

    private void handleItemClicked(BuildingsData buildingsData) {
        Log.d(TAG, "building click: " + buildingsData.getNumber());
//        Toast.makeText(context, "Building click: " + buildingsData.getNumber(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, NestedBuildingActivity.class);
        intent.putExtra(Constants.NAME_CITY_DATA, cityData);
        intent.putExtra(Constants.NAME_STREET_DATA, streetData);
        intent.putExtra(Constants.NAME_BUILDING_DATA, buildingsData);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
    }

    private void startLocationMonitoring() {
        // GPS reception listener
        locationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                isGpsAvailable = Constants.LOCATION_AVAILABLE.equalsIgnoreCase(action);
                repaintHeader();
            }
        };
        registerLocalLocationReceiver();
        Intent locationServiceIntent = new Intent(context, locationListenerService.class);
        startService(locationServiceIntent);

        // Network Connection listener
        if (null == networkListener) {
            networkListener = new NetworkChangeReceiver.NetWorkChangedListener() {
                @Override
                public void onNetworkAvailable(boolean available) {
                    isNetworkAvailable = available;
                    repaintHeader();
                }
            };
        }
        ApartmentsEvidenceApp.getInstance().setConnectionListener(networkListener);
        isNetworkAvailable = NetworkChangeReceiver.isConnected();
        repaintHeader();
    }

    private void registerLocalLocationReceiver() {
        IntentFilter locationIntentFilter = new IntentFilter();
        locationIntentFilter.addAction(Constants.LOCATION_NOT_AVAILABLE);
        locationIntentFilter.addAction(Constants.LOCATION_AVAILABLE);
        LocalBroadcastManager.getInstance(context).registerReceiver(locationReceiver, locationIntentFilter);
    }

    private void repaintHeader() {
        Log.d(TAG, "repaintHeader");
        // Network
        noNetworkImageView.setVisibility(isNetworkAvailable ? View.INVISIBLE : View.VISIBLE);
        // GPS
        noGpsImageView.setVisibility(isGpsAvailable ? View.INVISIBLE : View.VISIBLE);

        // Paint header's bg color
        if ((true == isNetworkAvailable) && (true == isGpsAvailable)) {
            headerRootView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            headerRootView.setBackgroundColor(getResources().getColor(R.color.buildings_header_warning_bg_color));
        }
    }

    private void stopLocationMonitoring() {
        // Stop listening to network changes
        ApartmentsEvidenceApp.getInstance().setConnectionListener(null);
        unregisterLocalLocationReceiver();
    }

    private void unregisterLocalLocationReceiver() {
        if (null != locationReceiver) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(locationReceiver);
        }
    }

    public void onPause() {
        super.onPause();
        stopLocationMonitoring();
    }

    //************************************************  PERMISSIONS  *******************************
    @AfterPermissionGranted(RC_APP_PERMISSION)
    private void getPermissions() {
        // Check if it not happen when permissions have been pre granted
        if (true == hasAppPermissions()) {
            Log.d(TAG, "appPermissionsTask");
            // Check if it not happen when permissions have been pre granted
            startLocationMonitoring();
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
        // Check if it not happen when permissions have been pre granted
        startLocationMonitoring();
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
}