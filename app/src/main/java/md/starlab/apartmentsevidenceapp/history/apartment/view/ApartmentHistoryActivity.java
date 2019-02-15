package md.starlab.apartmentsevidenceapp.history.apartment.view;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.ApartmentsEvidenceApp;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.base.BaseActivity;
import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.StreetData;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.dialogs.AppExitDialog;
import md.starlab.apartmentsevidenceapp.history.apartment.model.ApartmentTransactionAnswers;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.history.apartment.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.history.apartment.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.history.apartment.view.adapters.ApartmentHistoryListAdapter;
import md.starlab.apartmentsevidenceapp.history.apartment.view.adapters.ApartmentHistoryTransactionsAdapter;
import md.starlab.apartmentsevidenceapp.history.apartment.viewmodel.ApartmentHistoryViewModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Apartment;
import md.starlab.apartmentsevidenceapp.questionnaire.view.QuestionnaireActivity;
import md.starlab.apartmentsevidenceapp.recivers.network.NetworkChangeReceiver;
import md.starlab.apartmentsevidenceapp.services.location.locationListenerService;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class ApartmentHistoryActivity extends BaseActivity implements
        ApartmentHistoryTransactionsAdapter.OnHistoryTractionClickListener {
    private static final String TAG = "ApartmentHistoryAct";

    private static final long FIFTEEN_MIN = 1000 * 60 * 15;

    // Screen Header Components
    @BindView(R.id.apartmentHistoryHeaderRootView)
    public View headerRootView;
    @BindView(R.id.buildingsUserNameTextView)
    public TextView userNameTextView;
    @BindView(R.id.buildingsNoGpsWarningImageView)
    public ImageView noGpsImageView;
    @BindView(R.id.buildingsNoCellularWarningImageView)
    public ImageView noNetworkImageView;
    @BindView(R.id.buildingsCityNameTextView)
    public TextView cityNameTextView;
    @BindView(R.id.buildingsStreetNameTextView)
    public TextView streetNameTextView;
    @BindView(R.id.buildingsBuildingNumberTextView)
    public TextView buildingNumberTextView;
    @BindView(R.id.buildingsApartmentNumberTextView)
    public TextView apartmentNumberTextView;

    // Lists Components
    @BindView(R.id.apartmentHistoryListView)
    public ListView historyListView;
    @BindView(R.id.apartmentHistoryTransactionListView)
    public ListView transactionListView;
    private ApartmentHistoryListAdapter historyListAdapter;
    private ArrayList<ApartmentTransactionAnswers> historyDataList = new ArrayList<>();
    private ApartmentHistoryTransactionsAdapter transactionsAdapter;
    private ArrayList<BuildingAssignedTransaction> transactionsDataList = new ArrayList<>();

    // Screen data
    private CityData cityData;
    private StreetData streetData;
    private BuildingsData buildingsData;
    private Apartment apartmentData;

    // Connection and Location Indicators
    private NetworkChangeReceiver.NetWorkChangedListener networkListener;
    private BroadcastReceiver locationReceiver;
    private boolean isGpsAvailable = false;
    private boolean isNetworkAvailable = false;
    private double longitude = 0;
    private double latitude = 0;
    private long timeStamp = 0;


    // Helper
    private Context context;
    private ProgressDialog progressDialog;

    private ApartmentHistoryViewModel viewModel;

    private AdapterView.OnItemClickListener transactionOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (transactionsDataList.get(position).isHasQuestionnaire()) {
                Intent intent = new Intent(context, QuestionnaireActivity.class);
                intent.putExtra(Constants.NAME_CITY_DATA, cityData);
                intent.putExtra(Constants.NAME_STREET_DATA, streetData);
                intent.putExtra(Constants.NAME_BUILDING_DATA, buildingsData);
                intent.putExtra(Constants.NAME_APARTMENT_DATA, apartmentData);
                intent.putExtra(Constants.NAME_TRANSACTION_DATA, transactionsDataList.get(position));
                startActivity(intent);
            } else {
                View buttonsLayout = view.findViewById(R.id.apartmentHistoryTransactionsSecondView);
                ColorsUtils.setBgColorDrawable(buttonsLayout, transactionsDataList.get(position).getColor());
                View dataView = view.findViewById(R.id.apartmentHistoryTransactionsFirstView);
                boolean isShown = buttonsLayout.isShown();
                buttonsLayout.setVisibility(isShown ? View.GONE : View.VISIBLE);
                dataView.setVisibility(isShown ? View.VISIBLE : View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTag(Dynamic.tagApartmentHistory);
        setContentView(R.layout.activity_apartment_history);
        ButterKnife.bind(this);
        context = this;

        Intent inComingIntent = getIntent();
        cityData = inComingIntent.getParcelableExtra(Constants.NAME_CITY_DATA);
        streetData = inComingIntent.getParcelableExtra(Constants.NAME_STREET_DATA);
        buildingsData = inComingIntent.getParcelableExtra(Constants.NAME_BUILDING_DATA);
        apartmentData = inComingIntent.getParcelableExtra(Constants.NAME_APARTMENT_DATA);
        if (true == StringUtils.isNullOrEmpty(apartmentData.getId())) {
            throw new NullPointerException("Apartment ID can not be null or empty");
        }
        userNameTextView.setText(String.valueOf(Dynamic.USERNAME));
        cityNameTextView.setText(cityData.getName());
        streetNameTextView.setText(streetData.getName());
        buildingNumberTextView.setText(String.valueOf(buildingsData.getNumber()));
        apartmentNumberTextView.setText(String.valueOf(apartmentData.getNumber()));

        // Init ListViews Adapters
        historyListAdapter = new ApartmentHistoryListAdapter(context, historyDataList);
        transactionsAdapter = new ApartmentHistoryTransactionsAdapter(context, transactionsDataList);
        historyListView.setAdapter(historyListAdapter);
        transactionListView.setAdapter(transactionsAdapter);
        transactionListView.setOnItemClickListener(transactionOnItemClickListener);

        // Init view Model
        viewModel = ViewModelProviders.of(this).get(ApartmentHistoryViewModel.class);

    }


    private void loadingTransactionsData() {
        viewModel.getTransaction(buildingsData.getId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<List<BuildingAssignedTransaction>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<BuildingAssignedTransaction>> listDataWrapper) {
                //if (null != progressDialog) progressDialog.hide();
                if (null != listDataWrapper.getThrowable()) {
                    Toast.makeText(context, getString(R.string.cities_error_message,
                            listDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    transactionsDataList = (ArrayList<BuildingAssignedTransaction>) listDataWrapper.getData();
                    transactionsAdapter.setData(transactionsDataList);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            transactionsAdapter.notifyDataSetChanged();
                        }
                    });
                    if (true == transactionsDataList.isEmpty()) {
                        // showToast("No Transactions recorded for this apartment");
                    }
                }
            }
        });
    }

    private void loadingHistoryData() {
        viewModel.getHistory(apartmentData.getId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<List<ApartmentTransactionAnswers>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<ApartmentTransactionAnswers>> listDataWrapper) {
                //if (null != progressDialog) progressDialog.hide();
                if (null != listDataWrapper.getThrowable()) {
                    Toast.makeText(context, getString(R.string.cities_error_message,
                            listDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    historyDataList = (ArrayList<ApartmentTransactionAnswers>) listDataWrapper.getData();
                    historyListAdapter.setData(historyDataList);
                    if (true == historyDataList.isEmpty()) {
                        // showToast("No History recorded for this apartment");
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForBackStep();
        // Start monitoring
        startLocationMonitoring();

        loadingHistoryData();
        loadingTransactionsData();

        //progressDialog = ProgressDialog.show(this, "", "Loading...", true);
    }

    public void onPause() {
        super.onPause();
        stopLocationMonitoring();
    }

    private void startLocationMonitoring() {
        // GPS reception listener
        locationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                isGpsAvailable = Constants.LOCATION_AVAILABLE.equalsIgnoreCase(action);
                if (true == isGpsAvailable) {
                    timeStamp = System.currentTimeMillis();
                    longitude = intent.getDoubleExtra(Constants.NAME_LONGITUDE_DATA, 0);
                    latitude = intent.getDoubleExtra(Constants.NAME_LATITUDE_DATA, 0);
                }

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

    @OnClick({R.id.buildingsCityNameTextView})
    public void goBackToCitiesScreen(View view) {
        goBackToActivityTag(Dynamic.tagCitiesAndStreets);
        finish();
    }

    @OnClick({R.id.buildingsStreetNameTextView})
    public void goBackToBuildings(View view) {
        goBackToActivityTag(Dynamic.tagBuildings);
        finish();
    }

    @OnClick({R.id.buildingsBuildingNumberTextView})
    public void goBackToNestedBuilding(View view) {
        goBackToActivityTag(Dynamic.tagNestedBuilding);
        finish();
    }

    @OnClick(R.id.buildingsLogoutIcon)
    public void onLogout(View view) {
        AppExitDialog dialog = new AppExitDialog();
        dialog.show(getSupportFragmentManager(), "AppExitDialog");
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        TextView textView = view.findViewById(android.R.id.message);
        textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        textView.setTextColor(getResources().getColor(R.color.colorBlack));
        toast.show();
    }

    // OnHistoryTractionClickListener implementation
    @Override
    public void onHistoryTransactionClick(boolean yesPressed, BuildingAssignedTransaction item) {
        Log.d(TAG, "onHistoryTransactionClick item: " + item.getTransactionId());
        if (true == yesPressed) {
            if (FIFTEEN_MIN < (System.currentTimeMillis() - timeStamp)) {
                latitude = 0;
                longitude = 0;
            }
            viewModel.submitAnswer(longitude, latitude, apartmentData.getId(), item.getTransactionId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<TransactionAnswerModel>>() {
                @Override
                public void onChanged(@Nullable DataWrapper<TransactionAnswerModel> transactionAnswerModelDataWrapper) {
                    loadingHistoryData();
                    loadingTransactionsData();
                }
            });
        }
    }
}