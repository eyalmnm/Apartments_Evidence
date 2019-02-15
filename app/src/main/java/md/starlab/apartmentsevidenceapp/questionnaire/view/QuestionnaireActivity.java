package md.starlab.apartmentsevidenceapp.questionnaire.view;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
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
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Apartment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.FailedToAddFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.SuccessDialogFragment;
import md.starlab.apartmentsevidenceapp.questionnaire.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.questionnaire.model.QuestionModel;
import md.starlab.apartmentsevidenceapp.questionnaire.model.TransactionAnswerModel;
import md.starlab.apartmentsevidenceapp.questionnaire.viewmodel.QuestionnaireViewModel;
import md.starlab.apartmentsevidenceapp.recivers.network.NetworkChangeReceiver;
import md.starlab.apartmentsevidenceapp.services.location.locationListenerService;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLCheckBoxGroup;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLDateTimeTextView;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLEditText;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLFileBrowser;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLRadioGroup;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLSpinner;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLTextView;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLTextViewSeparated;
import md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire.SLWidgetInterface;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;
import md.starlab.apartmentsevidenceapp.utils.FileUtils;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class QuestionnaireActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, View.OnClickListener {
    private static final String TAG = "QuestionnaireActivity";

    private static final int READ_REQUEST_CODE = 42;

    private static final long FIFTEEN_MIN = 1000 * 60 * 15;

    // Permissions components
    private static final int RC_APP_PERMISSION = 123;
//    Button addAttachmentButton;
    TextView addAttachmentButton;

    private boolean hasFailedResponses;

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
    @BindView(R.id.questionnaireStatusIndicator)
    public TextView statusTextView;
    @BindView(R.id.questionnaireStatusTitle)
    public TextView statusTitle;
    @BindView(R.id.container)
    LinearLayout container;
    LinearLayout.LayoutParams containerParams;
    LinearLayout.LayoutParams attachmentContainerParams;
    LinearLayout attachmentsContainer;
    private String[] appPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.questionnaireSaveButton)
    public Button questionnaireSaveButton;

    // Screen data
    private BuildingAssignedTransaction transaction;
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
    private ArrayList<QuestionModel> questions;
    private ArrayList<SLWidgetInterface> slWidgets;
    private ArrayList<SLFileBrowser> attachmentList = new ArrayList<>(3);
    private QuestionnaireViewModel viewModel;

    // Helpers
    private Context context;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTag(Dynamic.tagQuestionnaire);
        setContentView(R.layout.questionnaire_activity);
        ButterKnife.bind(this);
        context = this;

        Intent inComingIntent = getIntent();
        cityData = inComingIntent.getParcelableExtra(Constants.NAME_CITY_DATA);
        streetData = inComingIntent.getParcelableExtra(Constants.NAME_STREET_DATA);
        buildingsData = inComingIntent.getParcelableExtra(Constants.NAME_BUILDING_DATA);
        apartmentData = inComingIntent.getParcelableExtra(Constants.NAME_APARTMENT_DATA);
        transaction = inComingIntent.getParcelableExtra(Constants.NAME_TRANSACTION_DATA);
        if (null == transaction) {
            throw new NullPointerException("Transaction ID can not be null or empty");
        }
        userNameTextView.setText(String.valueOf(Dynamic.USERNAME));
        cityNameTextView.setText(cityData.getName());
        streetNameTextView.setText(streetData.getName());
        buildingNumberTextView.setText(String.valueOf(buildingsData.getNumber()));
        apartmentNumberTextView.setText(String.valueOf(apartmentData.getNumber()));

        viewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel.class);
        loadingQuestionnaire(transaction.getTransactionId(), Dynamic.TOKEN);

        // Change Status color according to Transaction color
        ColorsUtils.setBgColorDrawable(statusTextView, transaction.getColor());

        // Set Questionnaire title
        statusTitle.setText(String.valueOf(transaction.getTitle()));

        attachmentsContainer = new LinearLayout(context);
        attachmentsContainer.setVisibility(View.INVISIBLE);
//        addAttachmentButton = new Button(context);
        addAttachmentButton = new TextView(context);
        addAttachmentButton.setVisibility(View.INVISIBLE);

        //progressDialog = ProgressDialog.show(this, "", "Loading...", true);
    }

    private void loadingQuestionnaire(String transactionId, String token) {
        viewModel.getQuestionnaireByID(transactionId, token).observe(this, new Observer<DataWrapper<List<QuestionModel>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<QuestionModel>> dataWrapper) {
                if (null != progressDialog) progressDialog.hide();
                Log.d(TAG, "loadingQuestionnaire onChanged");
                if (null != dataWrapper.getThrowable()) {
                    Toast.makeText(context, getString(R.string.cities_error_message, dataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    questions = (ArrayList<QuestionModel>) dataWrapper.getData();
                    createQuestionnaire(questions);
                }
            }
        });
    }

    private void createQuestionnaire(ArrayList<QuestionModel> questions) {
        slWidgets = new ArrayList<>();
        containerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = getResources().getDimensionPixelSize(R.dimen.question_questions_margin_size);
        containerParams.setMargins(0, margin, 0, margin);
        attachmentContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //int attchMargin = getResources().getDimensionPixelSize(R.dimen.question_attachment_margin_size);
        //attachmentContainerParams.setMargins(0, attchMargin, 0, attchMargin);
        for (QuestionModel question : questions) {
            SLWidgetInterface widget = slWidgetBuilder(question);
            if (null != widget) {
                slWidgets.add(widget);
                container.addView((View) widget, containerParams);
            }
        }

        // Attachments
        String attachmentTitleText = getResources().getString(R.string.attachment_title);
        SLWidgetInterface label = new SLTextViewSeparated(this, attachmentTitleText, attachmentTitleText);
        container.addView((View) label, containerParams);

        attachmentsContainer.setOrientation(LinearLayout.VERTICAL);
        container.addView(attachmentsContainer, attachmentContainerParams);

        // Add Attachment Button

//        <item name="android:textSize">@dimen/top_bar_text_size</item>
//        <item name="android:textColor">@color/buildings_city_name_text_color</item>
//        <item name="android:layout_width">wrap_content</item>
//        <item name="android:layout_height">@dimen/top_bar_but_height</item>
//        <item name="android:layout_centerVertical">true</item>
//        <item name="android:background">@drawable/building_city_name_bg_shape</item>
//        <item name="android:gravity">center</item>
//        <item name="android:singleLine">true</item>
//        <item name="android:paddingLeft">@dimen/top_bar_text_padding</item>
//        <item name="android:paddingRight">@dimen/top_bar_text_padding</item>
//        <item name="android:ellipsize">marquee</item>

        addAttachmentButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_ok_button_background_shape));
        addAttachmentButton.setWidth(getResources().getDimensionPixelSize(R.dimen.question_attach_but_width));
        addAttachmentButton.setGravity(Gravity.CENTER);
        addAttachmentButton.setHeight(getResources().getDimensionPixelSize(R.dimen.question_attach_but_height));
        addAttachmentButton.setText(R.string.questionnaire_add_Attachment_button_text);
        addAttachmentButton.setBackground(getResources().getDrawable(R.drawable.questio_button_shape));
        addAttachmentButton.setTextColor(getResources().getColor(R.color.question_title));
        addAttachmentButton.setTextSize(getResources().getDimensionPixelSize(R.dimen.question_option_text_size));
        addAttachmentButton.setAllCaps(false);
        addAttachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SLFileBrowser attachment = new SLFileBrowser(context, String.valueOf(attachmentList.size()),
                        "", QuestionnaireActivity.this);
                attachmentsContainer.addView(attachment, attachmentContainerParams);
                attachmentList.add(attachment);
            }
        });
        LinearLayout.LayoutParams addAttachmentParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addAttachmentParam.setMargins(0, 15, 0, 0);
        container.addView(addAttachmentButton, addAttachmentParam);

        attachmentList.add(new SLFileBrowser(this, String.valueOf(attachmentList.size()),
                "Attachment", this));
        attachmentsContainer.addView(attachmentList.get(0), attachmentContainerParams);
    }

    private SLWidgetInterface slWidgetBuilder(QuestionModel question) {
        switch (question.getType()) {
            case Constants.TYPE_DATE_PICKER:
                return new SLDateTimeTextView(this, question.getId(), question.getTitle(), true);
            case Constants.TYPE_CHECKBOX:
                return new SLCheckBoxGroup(this, question.getId(), question.getTitle(), question.getAnswers());
            case Constants.TYPE_LABEL:
                return new SLTextViewSeparated(this, question.getId(), question.getTitle());
            case Constants.TYPE_NUMBER:
                return new SLEditText(this, question.getId(), question.getTitle(), InputType.TYPE_CLASS_PHONE);
            case Constants.TYPE_RADIO:
                return new SLRadioGroup(this, question.getId(), question.getTitle(), question.getAnswers());
            case Constants.TYPE_SPINNER:
                return new SLSpinner(this, question.getId(), question.getTitle(), question.getAnswers());
            case Constants.TYPE_STRING:
                return new SLEditText(this, question.getId(), question.getTitle());
            case Constants.TYPE_TIME_PICKER:
                return new SLDateTimeTextView(this, question.getId(), question.getTitle(), false);
            default:
                return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForBackStep();
        // Start monitoring
        startLocationMonitoring();
        getPermissions();
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
                Log.d(TAG, "Location Updated");
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
        if (true == isNetworkAvailable) {
            noNetworkImageView.setVisibility(View.INVISIBLE);
            enabledSaveButton(true);
        } else {
            noNetworkImageView.setVisibility(View.VISIBLE);
            enabledSaveButton(false);
        }
        // GPS
        if (true == isGpsAvailable) {
            noGpsImageView.setVisibility(View.INVISIBLE);
        } else {
            noGpsImageView.setVisibility(View.VISIBLE);
        }
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

    public void onPause() {
        super.onPause();
        stopLocationMonitoring();
    }

    @OnClick(R.id.questionnaireSaveButton)
    public void onSaveClick(View view) {
        if (null == slWidgets || slWidgets.isEmpty()) {
            Toast.makeText(context, "Please reload questionnaire again when connectivity returns", Toast.LENGTH_LONG).show();
            return;
        }
        if (false == isAnswersValidate()) {
            Toast.makeText(context, "All question and descriptions must be filled", Toast.LENGTH_LONG).show();
            return;
        }
        enabledSaveButton(false);
        HashMap<String, Object> answerModels = new HashMap<String, Object>();
        for (SLWidgetInterface widget : slWidgets) {
            if (widget instanceof SLCheckBoxGroup) {
                answerModels.put(widget.getQuestionId(), widget.getQuestionAnswer().split(","));
            } else {
                answerModels.put(widget.getQuestionId(), widget.getQuestionAnswer());
            }
        }

        if (FIFTEEN_MIN < (System.currentTimeMillis() - timeStamp)) {
            latitude = 0;
            longitude = 0;
        }
        viewModel.submitAnswer(longitude, latitude, apartmentData.getId(), transaction.getTransactionId(), answerModels, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<TransactionAnswerModel>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<TransactionAnswerModel> dataWrapper) {
                if (null == dataWrapper.getThrowable()) {
                    boolean hasAttachment = false;
                    for (int i = 0; i < attachmentList.size(); i++) {
                        if (attachmentList.get(i).getFileUri() != null) hasAttachment = true;
                    }
                    if (true == hasAttachment) {
                        String serverId = dataWrapper.getData().getId();
                        uploadAttachments(serverId, attachmentList, 0, "");
                    } else {
                        showSuccessfullyDialog(true);
                    }
                } else {
                    enabledSaveButton(true);
                    showFailureDialog(getResources().getString(R.string.failed_to_bumit_questionnaire), false);
                }
            }
        });
    }

    private void enabledSaveButton(boolean enable) {
        questionnaireSaveButton.setEnabled(enable);
        if (enable)
            questionnaireSaveButton.setBackgroundColor(getResources().getColor(R.color.login_confirm_button_bg_color));
        else
            questionnaireSaveButton.setBackgroundColor(getResources().getColor(R.color.grey_light));
    }

    private boolean isAnswersValidate() {
        if (null == slWidgets) return false;
        for (SLWidgetInterface widget : slWidgets) {
            if (StringUtils.isNullOrEmpty(widget.getQuestionAnswer()))
                return false;
        }
        for (SLFileBrowser browser : attachmentList) {
            if (null == browser.getFileUri() && true == StringUtils.isNullOrEmpty(browser.getDescription())) {
                continue;
            } else {
                if (null == browser.getFileUri() || true == StringUtils.isNullOrEmpty(browser.getDescription()))
                    return false;
            }
        }
        return true;
    }

    private void uploadAttachments(final String serverId, final ArrayList<SLFileBrowser> attachmentList, final int index, final String failed) {
        if (index < attachmentList.size()) {
            Uri fileUri = attachmentList.get(index).getFileUri();
            final String description = attachmentList.get(index).getDescription();
            if (null == fileUri) {
                uploadAttachments(serverId, attachmentList, index + 1, failed);
            }
            viewModel.uploadAttachment(this, serverId, Dynamic.TOKEN, fileUri, description).observe(this, new Observer<DataWrapper<ResponseBody>>() {
                @Override
                public void onChanged(@Nullable DataWrapper<ResponseBody> dataWrapper) {
                    if (null == dataWrapper.getThrowable()) {
                        uploadAttachments(serverId, attachmentList, index + 1, failed);
                    } else {
                        uploadAttachments(serverId, attachmentList, index + 1, failed + ", " + description);
                    }
                }
            });
        } else {
            if (true == StringUtils.isNullOrEmpty(failed)) {
                showSuccessfullyDialog(true);
            } else {
                showFailureDialog(getResources().getString(R.string.failed_to_upload_file, failed), true);
            }
        }
    }

    private void showFailureDialog(String message, boolean applyBack) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString(Constants.NAME_MESSAGE, message);
        args.putBoolean(Constants.NAME_APPLY_BACK, applyBack);
        FailedToAddFragment fragment = new FailedToAddFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    private void showSuccessfullyDialog(boolean applyBack) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        SuccessDialogFragment fragment = new SuccessDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putBoolean(Constants.NAME_APPLY_BACK, applyBack);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    @Override
    public void onClick(View v) {
        try {
            SLFileBrowser fb = (SLFileBrowser) v.getParent().getParent();
            callFilePicker(fb);
        } catch (Throwable tr) {
            Log.e(TAG, "onClick", tr);
        }
    }

    private void showAttachmentContainer() {
        attachmentsContainer.setVisibility(View.VISIBLE);
        addAttachmentButton.setVisibility(View.VISIBLE);
    }

    private void callFilePicker(SLFileBrowser fileBrowser) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*"); // ("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE + Integer.parseInt(fileBrowser.getQuestionId()));
    }

    // Used by Ease Permissions also.
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
        } else if (requestCode >= READ_REQUEST_CODE && requestCode < READ_REQUEST_CODE + attachmentList.size()) {
            if (RESULT_OK == resultCode) {
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    String path = FileUtils.getPathFromUri(context, uri);
                    attachmentList.get(requestCode - READ_REQUEST_CODE).setUri(uri); // Convert Uri to path
                } else {
                    attachmentList.get(requestCode - READ_REQUEST_CODE).setUri(null);
                }
            }
        }
    }


    //************************************************  PERMISSIONS  *******************************
    @AfterPermissionGranted(RC_APP_PERMISSION)
    private void getPermissions() {
        // Check if it not happen when permissions have been pre granted
        if (true == hasAppPermissions()) {
            Log.d(TAG, "appPermissionsTask");
            // Check if it not happen when permissions have been pre granted
            showAttachmentContainer();
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
        showAttachmentContainer();
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

}
