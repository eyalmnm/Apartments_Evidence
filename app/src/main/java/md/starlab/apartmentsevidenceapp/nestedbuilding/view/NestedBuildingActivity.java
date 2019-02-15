package md.starlab.apartmentsevidenceapp.nestedbuilding.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

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
import md.starlab.apartmentsevidenceapp.dialogs.BuildingInfoDialogFragment;
import md.starlab.apartmentsevidenceapp.history.apartment.view.ApartmentHistoryActivity;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Apartment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Entrance;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Floor;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.MobileApartmentModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NestedBuilding;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewEntranceResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.NewFloorResponse;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.CanNotBeDeletedFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.DeleteApartmentFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.DeleteEntranceEmptyFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.DeleteFloorEmptyFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.DeleteFloorNotEmptyFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.EntranceEditDialogFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs.SuccessDialogFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors.NewEntranceEditorFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors.NewFloorEditorFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors.NewFloorInEntranceEditorFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building.EntranceView;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building.FloorView;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building.OnBuildingWidgetClickListener;
import md.starlab.apartmentsevidenceapp.nestedbuilding.viewmodel.NestedBuildingViewModel;
import md.starlab.apartmentsevidenceapp.recivers.network.NetworkChangeReceiver;
import md.starlab.apartmentsevidenceapp.services.location.locationListenerService;
import md.starlab.apartmentsevidenceapp.ui_widgets.VerticalTextView;
import md.starlab.apartmentsevidenceapp.ui_widgets.itemcounterview.CounterListener;
import md.starlab.apartmentsevidenceapp.ui_widgets.itemcounterview.CounterView;
import md.starlab.apartmentsevidenceapp.ui_widgets.layouts.MyLinearLayout;
import md.starlab.apartmentsevidenceapp.ui_widgets.scrollers.MyHorizontalScrollView;
import md.starlab.apartmentsevidenceapp.ui_widgets.scrollers.MyVerticalScrollView;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

// Ref: https://stackoverflow.com/a/31228412
// Ref: https://stackoverflow.com/questions/10042142/how-to-consume-child-views-touch-event-in-parent-views-touchlistener

public class NestedBuildingActivity extends BaseActivity implements OnBuildingWidgetClickListener,
        NewFloorEditorFragment.OnNewFloorAddListener, NewEntranceEditorFragment.OnNewEntranceAddListener,
        NewFloorInEntranceEditorFragment.OnNewFloorInEntranceAddListener, DeleteEntranceEmptyFragment.OnEntranceDeletingListener,
        DeleteFloorEmptyFragment.OnFloorDeletingListener, DeleteApartmentFragment.OnApartmentDeletingListener {
    private static final String TAG = "NestedBuildingActivity";

    // Header components
    @BindView(R.id.buildingsHeaderRootView)
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
    @BindView(R.id.buildingsInfoImageView)
    public ImageView infoIconView;
    @BindView(R.id.buildingsEditImageView)
    public ImageView penIconView;

    // Screen components
    @BindView(R.id.containerFrameLayout)
    public FrameLayout containerFrameLayout;
    //@BindView(R.id.container)
    public RelativeLayout container;
    @BindView(R.id.buildingsBuildingNumberTextView)
    public TextView buildingNumberTextView;
    @BindView(R.id.nestedBuildingMainView)
    public RelativeLayout mainView;

    // Scrollbars (Vertical, Horizontal regular mode)
    @BindView(R.id.nestedBuildingHorizontalScrollView)
    public MyHorizontalScrollView horizontalScroll;
    @BindView(R.id.nestedBuildingVerticalScrollView)
    public MyVerticalScrollView verticalScroll;

    // Scrollbars (Vertical, Horizontal edit mode)
    @BindView(R.id.nestedBuildingHorizontalEditScrollView)
    public MyHorizontalScrollView horizontalEditScroll;
    @BindView(R.id.nestedBuildingVerticalEditScrollView)
    public MyVerticalScrollView verticalEditScroll;

    // Main Layout
    private MyLinearLayout mainLayout;
    private RelativeLayout.LayoutParams mainLayoutParams;

    // Screen data
    private String buildingId;
    private CityData cityData;
    private StreetData streetData;
    private BuildingsData buildingsData;
    private NestedBuilding building;

    // Nested Building ViewModel
    private NestedBuildingViewModel viewModel;
    private ArrayList<EntranceView> entranceViewViews;

    // Helpers
    private Context context;
    private float sceneHeight = 0;
    private float sceneWidth = 0;
    private ProgressDialog progressDialog;

    // ScrollView Position
    private int xPos;
    private int yPos;

    // Connection and Location Indicators
    private NetworkChangeReceiver.NetWorkChangedListener networkListener;
    private BroadcastReceiver locationReceiver;
    private boolean isGpsAvailable = false;
    private boolean isNetworkAvailable = false;

    // Helpers
    private View buttonsView;
    private OnContainerTouchImplementation onContainerTouchImplementation;
    private boolean isEditMode;
    private View apartmentView;
    private FloorView floorView;

    // Performance Monitoring
    private Trace myTrace;

    // EntranceEditDialogFragment listener for deleting a floor
    private DeleteFloorEmptyFragment.OnFloorDeletingListener floorInEntranceDeletingListener;

    // EntranceEditDialogFragment listener for deleting an entrance
    private DeleteEntranceEmptyFragment.OnEntranceDeletingListener entranceDeletingListener;

    private EntranceEditDialogFragment entranceEditDialogFragment;

    private float scrollStepX = 0.0f;
    private float scrollStepY = 0.0f;

    private volatile boolean fromDrag = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTag(Dynamic.tagNestedBuilding);
        setContentView(R.layout.activity_nested_building);
        ButterKnife.bind(this);
        context = this;
        isEditMode = false;

        myTrace = FirebasePerformance.getInstance().newTrace("nested_building_trace");
        myTrace.start();

        Intent inComingIntent = getIntent();
        cityData = inComingIntent.getParcelableExtra(Constants.NAME_CITY_DATA);
        streetData = inComingIntent.getParcelableExtra(Constants.NAME_STREET_DATA);
        buildingsData = inComingIntent.getParcelableExtra(Constants.NAME_BUILDING_DATA);
        buildingId = buildingsData.getId();
        if (true == StringUtils.isNullOrEmpty(buildingId))
            throw new NullPointerException("Building Id is null or empty");

        userNameTextView.setText(String.valueOf(Dynamic.USERNAME));
        cityNameTextView.setText(cityData.getName());
        streetNameTextView.setText(streetData.getName());
        buildingNumberTextView.setText(String.valueOf(buildingsData.getNumber()));

        // TODO Remove
//        buildingId = "100";
//        userNameTextView.setText(String.valueOf("Test"));
//        cityNameTextView.setText("cityData");
//        streetNameTextView.setText("streetData");
//        buildingNumberTextView.setText(String.valueOf(125));


        // Init view Model
        viewModel = ViewModelProviders.of(this).get(NestedBuildingViewModel.class);
        loadingNestedBuilding();

        initContainers();

        // Init Scree size properties
        sceneHeight = Dynamic.screenHeight;
        sceneWidth = Dynamic.screenWidth;

        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        //loadingNestedBuilding(); // TODO Remove
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initContainers() {
        if (null == onContainerTouchImplementation) {
            onContainerTouchImplementation = new OnContainerTouchImplementation();
        }
        container = new RelativeLayout(context) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                onContainerTouchImplementation.onTouch(container, ev);
                return false;
            }
        };
        // "White spaces" onTouch Listener
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onContainerTouchImplementation.onTouch(container, event);
                return true;
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.BOTTOM;
        container.setGravity(Gravity.BOTTOM);
        containerFrameLayout.addView(container, params);

        // Create the main layout
        mainLayout = new MyLinearLayout(context);
        mainLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mainLayout.setLayoutParams(mainLayoutParams);
        mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        mainLayout.setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForBackStep();
        // Start monitoring
        startLocationMonitoring();
    }

    public void onPause() {
        super.onPause();
        stopLocationMonitoring();
    }


    private void uncoverAll() {
        for (int i = 0; i < entranceViewViews.size(); i++) {
            EntranceView entranceView = entranceViewViews.get(i);
            if (null != entranceView) {
                ArrayList<FloorView> floorViewList = entranceView.getFloorViewViews();
                if (null != floorViewList) {
                    for (int j = 0; j < floorViewList.size(); j++) {
                        FloorView floorView = floorViewList.get(j);
                        if (null != floorView) {
                            floorView.setCover(false, false);
                        }
                    }
                }
            }
        }
    }

    private void hideButtons() {
        buttonsView.setVisibility(View.GONE);
    }

    // Only in Edit Mode!!!
    private void handleFloorClick(FloorView floorView) {
        if (null != floorView) {
            if ((null != this.floorView) && (false == this.floorView.getFloorId().equals(floorView.getFloorId()))) {
                resetSelectedApartment();
            }
            this.floorView = floorView;
            hideAllFloors(floorView);
            showButtons(floorView, null, ButtonsModes.EDIT_FLOOR);
        }
    }

    private void loadingNestedBuilding() {
        Log.d(TAG, "drawBuilding (Query) start");
        viewModel.getBuilding(buildingId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<NestedBuilding>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<NestedBuilding> nestedBuildingDataWrapper) {
                if (null != nestedBuildingDataWrapper.getThrowable()) {
                    if (null != progressDialog) progressDialog.hide();
                    Toast.makeText(context, getString(R.string.cities_error_message,
                            nestedBuildingDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    building = nestedBuildingDataWrapper.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawBuilding(building);
                        }
                    });
                }
            }
        });
    }

    private void drawBuilding(NestedBuilding building) {
        long now = System.currentTimeMillis();
        Log.d(TAG, "drawBuilding start");

        if (null != entranceViewViews) {
            entranceViewViews.clear();
        }
        mainLayout.removeAllViews();
        container.removeView(mainLayout);

        // Get Entrances and convert them to views
        ArrayList<Entrance> entranceModels = building.getEntrances();

        // Get Entrances' titles
        // String[] entrancesTitles = new String[entranceModels.size()];
        // Convert the Entrance Model array to Entrance View array
        entranceViewViews = new ArrayList<>(entranceModels.size());
        int highestEntranceIndex = 0;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < entranceModels.size(); i++) {
            if (entranceModels.get(i).getFloors().size() > entranceModels.get(highestEntranceIndex).getFloors().size())
                highestEntranceIndex = i;
            EntranceView entranceView = new EntranceView(context, entranceModels.get(i).getId(), entranceModels.get(i).getNumber(), entranceModels.get(i).getOrder(), this);

            ArrayList<Floor> floorModels = entranceModels.get(i).getFloors();
            entranceView.setFloors(floorModels);

            entranceViewViews.add(entranceView);
            mainLayout.addView(entranceView, params);
        }
        container.addView(mainLayout);

        // Get Floors' titles of the highest entrance
        String[] floorsTitles = new String[0];
        if ((null != entranceModels) && (false == entranceModels.isEmpty())) {
            floorsTitles = new String[entranceModels.get(highestEntranceIndex).getFloors().size()];
            for (int i = 0; i < entranceModels.get(highestEntranceIndex).getFloors().size(); i++) {
                floorsTitles[i] = entranceModels.get(highestEntranceIndex).getFloors().get(i).getNumber();
            }
        }

        final String[] finalFloorsTitles = floorsTitles;
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the progress dialog
                if (null != progressDialog) progressDialog.hide();
                mainLayout.setY(container.getHeight() - mainLayout.getHeight());

                // Init the scroller
                initVerticalScroll(finalFloorsTitles);
                initVerticalEditScroll(finalFloorsTitles);
                initHorizontalScroll(entranceViewViews);
                initHorizontalEditScroll(entranceViewViews);

                // Reset covers in case of edit mode
                if (isEditMode && (null != floorView)) {
                    handleFloorClick(floorView);
                } else {
                    showButtons(floorView, null, ButtonsModes.NONE);
                }
            }
        }, 300);

        float minWidth = sceneWidth;  // Calculate the horizontal scroll width
        float minHeight = sceneHeight; // Calculate the vertical scroll height

        container.invalidate();
        container.requestLayout();

        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                container.getGlobalVisibleRect(rect);
                Log.d(TAG, "onGlobalLayout: " + rect.toString());
                ViewTreeObserver obs = container.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
            }
        });

        Log.d(TAG, "drawBuilding end " + (System.currentTimeMillis() - now));
    }


    private void initHorizontalEditScroll(@Nullable ArrayList<EntranceView> entranceViews) {
        // horizontal green plus
        int plusSignWidth = getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_add_width);
        LinearLayout.LayoutParams addSignParams = new LinearLayout.LayoutParams(
                plusSignWidth,    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_add_height));  // Return in Pixels

        // horizontal BIG green plus
        LinearLayout.LayoutParams addBigSignParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_big_plus_width),    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_add_height));  // Return in Pixels

        int leftMargin = getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_title_margin_left);
        int rightMargin = getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_title_margin_right);

        addSignParams.setMargins(leftMargin, 0, rightMargin, 0);
        addBigSignParams.setMargins(leftMargin, 0, rightMargin, 0);

        int padding = getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_title_padding);

        horizontalEditScroll.removeAllViews();

        for (int i = 0; i < entranceViews.size(); i++) {
            final int entrancePosition = i;
            EntranceView entranceView = entranceViews.get(i);
            int entranceWidth = getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_title_width);

            ArrayList<FloorView> floorViews = entranceView.getFloorViewViews();
            if ((null != floorViews) && (!floorViews.isEmpty())) {
                entranceWidth = (entranceView.getWidth() - padding - plusSignWidth - (3 * (leftMargin + rightMargin)));
            }

            // plus in the middle
            if (0 < i) {
                horizontalEditScroll.addViewTo(createPlusTextView(addBigSignParams, entranceView.getOrder()));
            }
            // the first plus (if building have at least on entrance)
            else {
                horizontalEditScroll.addViewTo(createPlusTextView(addSignParams, entranceView.getOrder()));
            }

            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.horizontal_edit_scroll_title_background_shape);
            textView.setText(entranceView.getTitle());
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setPadding(0, 0, 0, 0);
            textView.setTextColor(getResources().getColor(R.color.colorWhite));
            textView.setTextSize(getResources().getDimension(R.dimen.horizontal_scroll_title_text_size));
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    entranceWidth,   // Value in Pixels
                    getResources().getDimensionPixelSize(R.dimen.horizontal_edit_scroll_title_height));  // Return in Pixels
            titleParams.setMargins(leftMargin, 0, rightMargin, 0);
            textView.setLayoutParams(titleParams);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = getSupportFragmentManager();
                    entranceEditDialogFragment = EntranceEditDialogFragment.newInstance(building.getEntrances().get(entrancePosition));
                    floorInEntranceDeletingListener = entranceEditDialogFragment;
                    entranceDeletingListener = entranceEditDialogFragment;

                    entranceEditDialogFragment.show(fm, entranceEditDialogFragment.getClass().getSimpleName());
                }
            });
            horizontalEditScroll.addViewTo(textView);
        }

        // Add The plus sign anyway (can be the first, if building doesn't have any entrance)
        horizontalEditScroll.addViewTo(createPlusTextView(addSignParams, null));
    }

    private TextView createPlusTextView(LinearLayout.LayoutParams signParams, String order) {
        //Log.e(TAG, "createPlusTextView():\n\torder: " +order);
        TextView plusTextView = new TextView(context);
        plusTextView.setGravity(Gravity.CENTER);
        plusTextView.setBackground(getResources().getDrawable(R.drawable.horizontal_edit_scroll_title_background_shape));

        plusTextView.setLayoutParams(signParams);
        if (false == StringUtils.isNullOrEmpty(order)) {
            //plusTextView.setTag(Integer.parseInt(order) + 1);
            plusTextView.setTag(order);
        }

        Log.d(TAG, "createPlusTextView plusTextView tag: " + plusTextView.getTag());
        plusTextView.setText("+");

        plusTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        plusTextView.setTextSize(getResources().getDimension(R.dimen.scroller_text_size));
        plusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "createPlusTextView onClick tag: " + view.getTag());
                //showAddNewEntrance(String.valueOf(view.getTag()));
                showAddNewEntrance((String) view.getTag());
            }
        });
        return plusTextView;
    }

    private void updateScrollbars(float x, float y) {
        // Update the regular scroller
        if (2 < Math.abs(y)) {
            float step = 0.53f;

            if (y > 0) {
                y += step;
            } else {
                y -= step;
            }

            verticalScroll.scrollBy(0, (int) -y);
        }
        if (2 < Math.abs(x)) {
            float step = 0.40f;

            if (x > 0) {
                x += step;
            } else {
                x -= step;
            }

            horizontalScroll.scrollBy((int) -x, 0);
        }
        // Update the edit scroller
        if (2 < Math.abs(y)) verticalEditScroll.scrollBy(0, (int) -y);
        if (2 < Math.abs(x)) horizontalEditScroll.scrollBy((int) -x, 0);
    }

    private void initHorizontalScroll(@Nullable ArrayList<EntranceView> entranceViews) {
        int padding = getResources().getDimensionPixelSize(R.dimen.horizontal_scroll_title_padding);
        int leftMargin = getResources().getDimensionPixelSize(R.dimen.horizontal_scroll_title_margin_left);
        int rightMargin = getResources().getDimensionPixelSize(R.dimen.horizontal_scroll_title_margin_right);

        horizontalScroll.removeAllViews();
        for (int i = 0; i < entranceViews.size(); i++) {
            EntranceView entranceView = entranceViews.get(i);
            int entranceWidth = getResources().getDimensionPixelSize(R.dimen.horizontal_scroll_title_width);

            ArrayList<FloorView> floorViews = entranceView.getFloorViewViews();
            if ((null != floorViews) && (!floorViews.isEmpty())) {
                entranceWidth = (entranceViews.get(i).getWidth() - padding);
            }

            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.horizontal_scroll_title_background_shape);
            textView.setText(entranceViews.get(i).getTitle());
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    entranceWidth,
                    getResources().getDimensionPixelSize(R.dimen.horizontal_scroll_title_height));
            titleParams.setMargins(leftMargin, 0, rightMargin, 0);
            textView.setLayoutParams(titleParams);
            textView.setTextSize(getResources().getDimension(R.dimen.horizontal_scroll_title_text_size));

            horizontalScroll.addViewTo(textView);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) horizontalScroll.getLayoutParams();
        int scrollLeftMargin = getResources().getDimensionPixelSize(R.dimen.scroller_start_margin);
        lp.setMargins(scrollLeftMargin, 0, 15, 0);
        horizontalScroll.setLayoutParams(lp);
    }

    private void initVerticalEditScroll(@Nullable String[] floorsTitles) {
        // vertical green name
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_title_width),    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_title_height));

        // vertical green  plus
        LinearLayout.LayoutParams addSignParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_add_width),    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_add_height));

        // vertical BIG green plus
        LinearLayout.LayoutParams addBigSignParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_add_width),    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_big_plus_height));  // Return in Pixels

        int marginTop = getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_title_margin_top);
        int marginBottom = getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_title_margin_bottom);

        titleParams.setMargins(0, marginTop, 0, marginBottom);
        addSignParams.setMargins(0, marginTop, 0, marginBottom);
        addBigSignParams.setMargins(0, marginTop, 0, marginBottom);

        verticalEditScroll.removeAllViews();
        for (int i = floorsTitles.length; i >= 0; i--) {
            if (i < floorsTitles.length) {
                VerticalTextView textView = new VerticalTextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(getResources().getDrawable(R.drawable.vertical_edit_scroll_title_background_shape));
                textView.setText(floorsTitles[i]);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setLayoutParams(titleParams);
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                textView.setTextSize(getResources().getDimension(R.dimen.vertical_scroll_title_text_size));
                verticalEditScroll.addViewTo(textView);
            }

            VerticalTextView plusTextView = new VerticalTextView(context);
            plusTextView.setGravity(Gravity.CENTER);
            plusTextView.setBackground(getResources().getDrawable(R.drawable.vertical_edit_scroll_title_background_shape));
            plusTextView.setLayoutParams(addBigSignParams);
            if (i == 0 || i == floorsTitles.length) {
                plusTextView.setLayoutParams(addSignParams);
            }
            plusTextView.setText("+");
            plusTextView.setTag(i);
            plusTextView.setTextColor(getResources().getColor(R.color.colorWhite));
            plusTextView.setTextSize(getResources().getDimension(R.dimen.vertical_scroll_title_text_size));
            plusTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // showToast("add new floor below: " + view.getTag());
                    showAddNewFloor((Integer) view.getTag());
                }
            });
            verticalEditScroll.addViewTo(plusTextView);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) verticalEditScroll.getLayoutParams();
        int bottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_bottom_margin);
        int topMargin = getResources().getDimensionPixelSize(R.dimen.vertical_edit_scroll_top_margin);
        lp.setMargins(0, topMargin, 0, bottomMargin);
        verticalEditScroll.setLayoutParams(lp);
    }

    private void initVerticalScroll(@Nullable String[] floorsTitles) {
        // vertical gray names
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.nested_border_panel_small),    // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.entrance_floor_height));  // Return in Pixels

        int marginTop = getResources().getDimensionPixelSize(R.dimen.vertical_scroll_title_margin_top);
        int marginBottom = getResources().getDimensionPixelSize(R.dimen.vertical_scroll_title_margin_bottom);

        titleParams.setMargins(0, marginTop, 0, marginBottom);

        verticalScroll.removeAllViews();
        for (int i = floorsTitles.length - 1; i >= 0; i--) {
            VerticalTextView textView = new VerticalTextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(getResources().getDrawable(R.drawable.vertical_scroll_title_background_shape));
            textView.setText(floorsTitles[i]);
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            textView.setLayoutParams(titleParams);
            textView.setTextSize(getResources().getDimension(R.dimen.vertical_scroll_title_text_size));
            verticalScroll.addViewTo(textView);
        }

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) verticalScroll.getLayoutParams();
        int bottomMargin = getResources().getDimensionPixelSize(R.dimen.vertical_scroll_bottom_margin);
        lp.setMargins(0, 20, 0, bottomMargin);
        verticalScroll.setLayoutParams(lp);
    }

    // OnBuildingWidgetClickListener implementation
    @Override
    public boolean onWidgetClicked(View view, String id) {
        if (true == fromDrag) return true;
        if ((null != view) && (view instanceof FloorView)) {
            if (isEditMode) {
                if ((null == buttonsView) || (null != buttonsView && (!buttonsView.isShown()))) {
                    handleFloorClick((FloorView) view);
                }
                return true;
            }
        } else {
            Apartment apartmentData = getApartmentById(id);
            if (isEditMode) {
                if (null != buttonsView && buttonsView.isShown()) {
                    FloorView floorView = getFloorViewByApartmentId(id);
                    if ((floorView != null) && (this.floorView.getFloorId().equals(floorView.getFloorId()))) {
                        resetSelectedApartment();
                        apartmentView = findApartmentViewById(id, floorView);
                        if (null != apartmentView) {
                            TextView textView = apartmentView.findViewById(R.id.apartmentItemTitleTextView);
                            textView.setBackgroundTintList(context.getResources().getColorStateList(R.color.nested_building_selected_apartment_bg_color));
                        }
                        showButtons(floorView, apartmentData, ButtonsModes.MOVE_OR_DELETE_APARTMENT);
                    }
                }
            } else {
                Intent intent = new Intent(context, ApartmentHistoryActivity.class);
                intent.putExtra(Constants.NAME_CITY_DATA, cityData);
                intent.putExtra(Constants.NAME_STREET_DATA, streetData);
                intent.putExtra(Constants.NAME_BUILDING_DATA, buildingsData);
                intent.putExtra(Constants.NAME_APARTMENT_DATA, apartmentData);
                startActivity(intent);
            }
            return true;
        }
        return false;
    }

    private void showAddNewEntrance(String order) {
        Log.e(TAG, "showAddNewEntrance(): order: " + order);
        Bundle args = new Bundle();
        args.putString(Constants.NAME_ENTRANCE_ORDER, order);  // Minimum value is 1
        FragmentManager fm = getSupportFragmentManager();
        NewEntranceEditorFragment fragment = new NewEntranceEditorFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    private void showAddNewFloor(Integer order) {
        Bundle args = new Bundle();
        args.putInt(Constants.NAME_FLOOR_ORDER, order + 1);  // Minimum value is 1
        FragmentManager fm = getSupportFragmentManager();
        NewFloorEditorFragment fragment = new NewFloorEditorFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    // NewEntranceEditorFragment.OnNewEntranceAddListener implementation
    @Override
    public void onNewEntranceAdd(String order, int numOfFloors, final String name) {
        // Log.d(TAG, "drawBuilding entrance sent order: " + order);
        Log.e(TAG, "drawBuilding entrance sent:\n\tbuildingId:" + buildingId + "\n\torder: " + order + "\n\ttoken:" + Dynamic.TOKEN);
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        viewModel.addNewEntrance(name, order, buildingId, numOfFloors, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<NewEntranceResponse>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<NewEntranceResponse> dataWrapper) {
                if (null != progressDialog) progressDialog.hide();
                if (null == dataWrapper.getThrowable()) {
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                } else {
                    showFailedToAddEntrance(name);
                }
            }
        });
    }

    private void showFailedToAddEntrance(String name) {
        String message = getResources().getString(R.string.nested_building_entrance_failure_msg, name);
        showFailureDialog(message);
        Log.d(TAG, "showFailedToAddEntrance: " + name);
    }

    // NewFloorEditorFragment.OnNewFloorAddListener implementation
    @Override
    public synchronized void onNewFloorAdd(final int order, final String name, final int index, final String error) {
        if (0 == building.getEntrances().size()) {
            showToast("Building is Empty! you should add at least one entrance!");
            return;
        }
        if (building.getEntrances().size() > index) {
            progressDialog = ProgressDialog.show(this, "", "Loading...", true);
            viewModel.addNewFloor(name, order, building.getEntrances().get(index).getId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<NewFloorResponse>>() {
                @Override
                public void onChanged(@Nullable DataWrapper<NewFloorResponse> dataWrapper) {
                    if (null != progressDialog) progressDialog.hide();
                    if (null != dataWrapper.getThrowable()) {
                        String errorMsg = dataWrapper.getThrowable().getMessage() + (",");
                        onNewFloorAdd(order, name, index + 1, error + errorMsg);
                    } else {
                        onNewFloorAdd(order, name, index + 1, error);
                    }
                }
            });
        } else {
            if (true == StringUtils.isNullOrEmpty(error)) {
                loadingNestedBuilding();
                showSuccessfullyDialog();
            } else {
                loadingNestedBuilding();
                showFailedToAddFloor(name, error.toString());
            }
        }
    }


    // NewFloorInEntranceEditorFragment.OnNewFloorInEntranceAddListener implementation
    @Override
    public synchronized void onNewFloorInEntranceAdd(final int order, final String name, String entranceId) {
        final StringBuilder errorsSb = new StringBuilder();
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        viewModel.addNewFloor(name, order, entranceId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<NewFloorResponse>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<NewFloorResponse> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    errorsSb.append(dataWrapper.getThrowable().getMessage()); //.append(",");
                }
                if (null != progressDialog) progressDialog.hide();
                if (0 == errorsSb.toString().length()) {
                    if (null != entranceEditDialogFragment) {
                        entranceEditDialogFragment.dismiss();
                    }
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                } else {
                    showFailedToAddFloorInEntrance(name, errorsSb.toString());
                }
            }
        });
    }

    public synchronized void onNewApartmentAdd(final String apartmentNumber, String entranceName, final String floorNumber) {
        Floor floor = null;
        if ((StringUtils.isNullOrEmpty(entranceName)) || (StringUtils.isNullOrEmpty(floorNumber))) {
            return;
        }
        Entrance entrance = findEntranceByName(entranceName);
        if (null != entrance) {
            floor = findFloorByName(entrance, floorNumber);
            if (null == floorNumber) {
                return;
            }
        }

        final StringBuilder errorsSb = new StringBuilder();
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        viewModel.addApartment("", apartmentNumber, floor.getId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<MobileApartmentModel>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<MobileApartmentModel> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    errorsSb.append(dataWrapper.getThrowable().getMessage());
                }
                if (null != progressDialog) progressDialog.hide();
                if (0 == errorsSb.toString().length()) {
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                } else {
                    showFailedToAddApartment(apartmentNumber, floorNumber);
                }
            }
        });
    }

    private void showFailedToAddApartment(String apartmentNumber, String floorNumber) {
        String message = getResources().getString(R.string.nested_building_apartment_add_failure_msg, apartmentNumber, floorNumber);
        showFailureDialog(message);
    }

    public synchronized void onApartmentMove(final Apartment apartment, final String entranceName, final String floorNumber) {
        Floor floor = null;
        if ((StringUtils.isNullOrEmpty(entranceName)) || (StringUtils.isNullOrEmpty(floorNumber))) {
            return;
        }
        Entrance entrance = findEntranceByName(entranceName);
        if (null != entrance) {
            floor = findFloorByName(entrance, floorNumber);
            if (null == floorNumber) {
                return;
            }
        }

        final StringBuilder errorsSb = new StringBuilder();
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        viewModel.moveApartment(apartment.getId(), apartment.getNumber(), floor.getId(), Dynamic.TOKEN).observe(this, new Observer<DataWrapper<MobileApartmentModel>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<MobileApartmentModel> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    errorsSb.append(dataWrapper.getThrowable().getMessage());
                }
                if (null != progressDialog) progressDialog.hide();
                if (0 == errorsSb.toString().length()) {
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                } else {
                    showFailedToMoveApartment(apartment.getNumber(), floorNumber);
                }
            }
        });
    }

    private void showFailedToMoveApartment(String apartmentNumber, String floorNumber) {
        String message = getResources().getString(R.string.nested_building_apartment_move_failure_msg, apartmentNumber, floorNumber);
        showFailureDialog(message);
    }

    // DeleteApartmentFragment.OnApartmentDeletingListener Implementation
    @Override
    public void onApartmentDeleting(final String apartmentId, final String apartmentNumber) {
        viewModel.removeApartment(apartmentId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<String>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<String> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    showFailedToDeleteApartmentDialog(apartmentId, apartmentNumber);
                } else {
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                }
            }
        });
    }

    // DeleteFloorEmptyFragment.OnFloorDeletingListener Implementation
    @Override
    public void onFloorDeleting(final String floorId, final String floorNumber) {
        viewModel.removeFloor(floorId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<String>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<String> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    FloorView floor = findFloorById(floorId);
                    if (floor != null && false == floor.hasApartments()) {
                        showFloorCanNotBeDeletedDialog(floor.getTitle());
                    } else {
                        showFailedToDeleteFloorDialog(floorId, floorNumber);
                    }
                } else {
                    if (null != floorInEntranceDeletingListener) {
                        floorInEntranceDeletingListener.onFloorDeleting(floorId, floorNumber);
                    }
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                }
            }
        });
    }

    @Override
    public void onEntranceDeleting(final String entranceId, final String entranceTitle) {
        viewModel.removeEntrance(entranceId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<String>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<String> dataWrapper) {
                if (null != dataWrapper.getThrowable()) {
                    showFailedToDeleteEntranceDialog(entranceTitle);
                } else {
                    if (null != entranceDeletingListener) {
                        entranceDeletingListener.onEntranceDeleting(entranceId, entranceTitle);
                    }
                    showSuccessfullyDialog();
                    loadingNestedBuilding();
                }
            }
        });
    }

    private void showFailedToAddFloor(String floorName, String entrances) {
        String[] ids = entrances.split(",");
        StringBuilder namesSb = new StringBuilder();
        Log.d(TAG, "showFailedToAddFloor: " + ids.length);
        for (int i = 0; i < ids.length; i++) {
            Entrance entrance = findEntranceById(ids[i]);
            if (null == entrance) continue;
            namesSb.append(entrance.getNumber()).append(", ");
        }
        String entrancesNames = "";
        if (null != namesSb && 1 < namesSb.length()) {
            entrancesNames = namesSb.toString().substring(0, namesSb.length() - 2);
        }
        String message = getResources().getString(R.string.nested_building_floor_failure_msg, floorName, entrancesNames);
        showFailureDialog(message);
        Log.d(TAG, "showFailedToAddFloor: " + entrancesNames);
    }

    private void showFailedToAddFloorInEntrance(String floorName, String entranceId) {
        Entrance entrance = findEntranceById(entranceId);
        String entranceNumber = entrance.getNumber();
        String message = getResources().getString(R.string.nested_building_floor_in_entrance_failure_msg, floorName, entranceNumber);
        showFailureDialog(message);
        Log.d(TAG, "showFailedToAddFloor: " + entranceNumber);
    }

    private void showFailedToDeleteApartmentDialog(String apartmentId, String apartmentNumber) {
        String message = getResources().getString(R.string.nested_building_apartment_delete_failure_msg, apartmentNumber);
        showFailureDialog(message);
    }

    private void showFailedToDeleteFloorDialog(String floorId, String floorNumber) {
        String message = getResources().getString(R.string.nested_building_floor_delete_failure_msg, floorNumber);
        showFailureDialog(message);
    }

    private void showFailedToDeleteEntranceDialog(String entranceTitle) {
        String message = getResources().getString(R.string.nested_building_entrance_delete_failure_msg, entranceTitle);
        showFailureDialog(message);
    }

    private Entrance findEntranceById(String id) {
        ArrayList<Entrance> entrances = building.getEntrances();
        for (Entrance entrance : entrances) {
            if (id.trim().equals(entrance.getId())) {
                return entrance;
            }
        }
        return null;
    }

    private void showFloorCanNotBeDeletedDialog(String floorNumber) {
        String message = getString(R.string.nested_building_floor_can_not_be_deleted);
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString(Constants.NAME_MESSAGE, message);
        CanNotBeDeletedFragment fragment = new CanNotBeDeletedFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    private void showSuccessfullyDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SuccessDialogFragment fragment = new SuccessDialogFragment();
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    private void showFailureDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString(Constants.NAME_MESSAGE, message);
        CanNotBeDeletedFragment fragment = new CanNotBeDeletedFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    private void showButtons(final FloorView floorViewView, final Apartment apartment, ButtonsModes mode) {
        // Defaults
        verticalEditScroll.setEnabled(false);
        horizontalEditScroll.setEnabled(false);
        if (null != floorViewView) {
            floorViewView.addApartMode(false);
        }

        if (null == buttonsView) {
            LayoutInflater inflater = LayoutInflater.from(this);
            buttonsView = inflater.inflate(R.layout.layout_floor_edit_buttons, null);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mainView.addView(buttonsView, params);
        }

        View editFloorButtons = buttonsView.findViewById(R.id.editFloorButtons);
        View addApartmentButtons = buttonsView.findViewById(R.id.addApartmentButtonLView);
        View moveOrDeleteApartmentButtons = buttonsView.findViewById(R.id.moveOrDeleteApartmentButtonLView);

        buttonsView.setVisibility(View.VISIBLE);

        if (ButtonsModes.EDIT_FLOOR == mode) {
            // ADD Apartment
            LinearLayout addApartmentButton = buttonsView.findViewById(R.id.addApartmentButton);
            addApartmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showButtons(floorViewView, null, ButtonsModes.ADD_APARTMENT);
                }
            });

            // DELETE
            LinearLayout delFloorButton = buttonsView.findViewById(R.id.delFloorButton);
            delFloorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    floorViewView.addApartMode(false);
                    uncoverAll();
                    showButtons(null, null, ButtonsModes.EDIT_FLOOR);
                    hideButtons();
                    changeScrollerView(true);
                    Bundle data = new Bundle();
                    data.putString(Constants.NAME_FLOOR_DATA, floorViewView.getFloorId());
                    data.putString(Constants.NAME_FLOOR_NAME, floorViewView.getTitle());
                    DialogFragment fragment;
                    if (floorViewView.hasApartments()) {
                        fragment = new DeleteFloorNotEmptyFragment();
                    } else {
                        fragment = new DeleteFloorEmptyFragment();
                    }
                    fragment.setArguments(data);
                    fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
                    NestedBuildingActivity.this.floorView = null;
                }
            });

            // BACK 1 step
            LinearLayout unDoFloorButton = buttonsView.findViewById(R.id.unDoFloorButton);
            unDoFloorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != floorViewView) floorViewView.addApartMode(false);
                    uncoverAll();
                    showButtons(null, null, ButtonsModes.EDIT_FLOOR);
                    hideButtons();
                    changeScrollerView(true);
                }
            });

            changeScrollerView(false);
            addApartmentButtons.setVisibility(View.GONE);
            editFloorButtons.setVisibility(View.VISIBLE);
            moveOrDeleteApartmentButtons.setVisibility(View.GONE);
        } else if (ButtonsModes.ADD_APARTMENT == mode) {
            // Add Apartment
            final CounterView entrancesCounterNF = buttonsView.findViewById(R.id.entrancesCounter);
            final CounterView floorsCounterNF = buttonsView.findViewById(R.id.floorsCounter);

            loadingCounterData(floorViewView, entrancesCounterNF, floorsCounterNF);
            entrancesCounterNF.scrollTo(floorViewView.getHostEntrance().getTitle());
            floorsCounterNF.scrollFloorTo(floorViewView.getTitle());

            final EditText addApartmentValue = buttonsView.findViewById(R.id.addApartmentValue);
            addApartmentValue.setText("");
            TextView addApartmentOkButton = buttonsView.findViewById(R.id.addApartmentOkButton);
            addApartmentOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addApartmentValue.clearFocus();
                    String newApNumber = addApartmentValue.getText().toString();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    if (true == StringUtils.isNullOrEmpty(newApNumber)) {
                        showToast("Apartment number can not be empty");
                    } else {
                        showButtons(floorViewView, null, ButtonsModes.EDIT_FLOOR);
                        onNewApartmentAdd(newApNumber, entrancesCounterNF.getCounterValue(), floorsCounterNF.getCounterValue());
                    }
                }
            });

            // BACK 2 step
            LinearLayout addApartmentBack2 = buttonsView.findViewById(R.id.addApartmentBack);
            addApartmentBack2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != floorViewView) floorViewView.addApartMode(false);
                    showButtons(floorViewView, null, ButtonsModes.EDIT_FLOOR);
                }
            });

            addApartmentButtons.setVisibility(View.VISIBLE);
            editFloorButtons.setVisibility(View.GONE);
            moveOrDeleteApartmentButtons.setVisibility(View.GONE);
        } else if (ButtonsModes.MOVE_OR_DELETE_APARTMENT == mode) {
            final CounterView entrancesCounterNF = buttonsView.findViewById(R.id.moveOrDeleteApartmentEntrancesCounter);
            final CounterView floorsCounterNF = buttonsView.findViewById(R.id.moveOrDeleteApartmentFloorsCounter);

            loadingCounterData(floorViewView, entrancesCounterNF, floorsCounterNF);
            entrancesCounterNF.scrollTo(floorViewView.getHostEntrance().getTitle());
            floorsCounterNF.scrollFloorTo(floorViewView.getTitle());

            TextView moveApartmentOkButton = buttonsView.findViewById(R.id.moveOrDeleteApartmentOkButton);
            moveApartmentOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showButtons(floorViewView, apartment, ButtonsModes.EDIT_FLOOR);
                    onApartmentMove(apartment, entrancesCounterNF.getCounterValue(), floorsCounterNF.getCounterValue());
                    if (null != NestedBuildingActivity.this.apartmentView) {
                        TextView textView = NestedBuildingActivity.this.apartmentView.findViewById(R.id.apartmentItemTitleTextView);
                        textView.setBackgroundTintList(context.getResources().getColorStateList(R.color.buildings_grid_item_bg_color));
                        NestedBuildingActivity.this.apartmentView = null;
                    }
                }
            });

            // DELETE
            LinearLayout delApartmentButton = buttonsView.findViewById(R.id.moveOrDeleteApartmentDelButton);
            delApartmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showButtons(null, apartment, ButtonsModes.EDIT_FLOOR);
                    Bundle data = new Bundle();
                    data.putParcelable(Constants.NAME_APARTMENT_DATA, apartment);
                    DialogFragment fragment;
                    fragment = new DeleteApartmentFragment();
                    fragment.setArguments(data);
                    fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
                    resetSelectedApartment();
                }
            });

            // BACK 2 step
            LinearLayout moveOrDeleteApartmentUnDoButton = buttonsView.findViewById(R.id.moveOrDeleteApartmentUnDoButton);
            moveOrDeleteApartmentUnDoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != floorViewView) floorViewView.addApartMode(false);
                    resetSelectedApartment();
                    showButtons(floorViewView, apartment, ButtonsModes.EDIT_FLOOR);
                }
            });

            addApartmentButtons.setVisibility(View.GONE);
            editFloorButtons.setVisibility(View.GONE);
            moveOrDeleteApartmentButtons.setVisibility(View.VISIBLE);
        } else if (ButtonsModes.NONE == mode) {
            uncoverAll();
            hideButtons();
        }
    }

    private void resetSelectedApartment() {
        if (null != NestedBuildingActivity.this.apartmentView) {
            TextView textView = NestedBuildingActivity.this.apartmentView.findViewById(R.id.apartmentItemTitleTextView);
            textView.setBackgroundTintList(context.getResources().getColorStateList(R.color.buildings_grid_item_bg_color));
            NestedBuildingActivity.this.apartmentView = null;
        }
    }

    private View findApartmentViewById(String id, FloorView floorViewView) {
        if ((floorViewView.containsApartment(id)) && (null != floorViewView.getApartmentsViews())) {
            for (View view : floorViewView.getApartmentsViews()) {
                Apartment apartmentModel;
                if ((apartmentModel = (Apartment) view.getTag()) != null) {
                    if (apartmentModel.getId().trim().equals(id)) return view;
                }
            }
        }
        return null;
    }

    private FloorView getFloorViewByApartmentId(String apartmentId) {
        for (int i = 0; i < entranceViewViews.size(); i++) {
            EntranceView entranceView = entranceViewViews.get(i);
            if (null != entranceView) {
                ArrayList<FloorView> floorViewList = entranceView.getFloorViewViews();
                if (null != floorViewList) {
                    for (int j = 0; j < floorViewList.size(); j++) {
                        FloorView floorView = floorViewList.get(j);
                        if (null != floorView) {
                            // floorView.setCover(false, false);
                            if (floorView.containsApartment(apartmentId)) {
                                return floorView;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private Floor getFloorById(String entranceId, String floorId) {
        Entrance entrance = findEntranceById(entranceId);
        for (Floor floor : entrance.getFloors()) {
            String floorModelId = floor.getId();
            if (floorId.equals(floorModelId)) {
                return floor;
            }
        }
        return null;
    }

    private void loadingCounterData(FloorView floorViewView, CounterView entrancesCounter, final CounterView floorsCounter) {
        entrancesCounter.setValues(getEntrancesList(building));
        Entrance entrance = null;
        if (floorViewView != null) {
            entrance = findEntranceById(floorViewView.getHostEntrance().getEntranceId());
        }
        if (null == entrance) {
            entrance = building.getEntrances().get(0);
            floorsCounter.setValues(getFloorsList(entrance.getNumber()));
        } else {
            floorsCounter.setValues(getFloorsList(entrance.getNumber()));
            entrancesCounter.scrollTo(entrance.getNumber());
            floorsCounter.scrollTo(floorViewView.getTitle());
        }
        entrancesCounter.setCounterListener(new CounterListener() {
            @Override
            public void onIncClick(String value) {
                Entrance entrance = findEntranceByName(value);
                floorsCounter.setValues(getFloorsList(entrance.getNumber()));
            }

            @Override
            public void onDecClick(String value) {
                Entrance entrance = findEntranceByName(value);
                floorsCounter.setValues(getFloorsList(entrance.getNumber()));
            }
        });
        floorsCounter.setCounterListener(new CounterListener() {
            @Override
            public void onIncClick(String value) {
                // Do nothing currently
            }

            @Override
            public void onDecClick(String value) {
                // Do nothing currently
            }
        });
    }

    private void hideAllFloors(FloorView view) {
        for (int i = 0; i < entranceViewViews.size(); i++) {
            EntranceView entranceView = entranceViewViews.get(i);
            if (null != entranceView) {
                ArrayList<FloorView> floorList = entranceView.getFloorViewViews();
                if (null != floorList) {
                    for (int j = 0; j < floorList.size(); j++) {
                        FloorView floorView = floorList.get(j);
                        if (null != floorView) {
                            if (false == view.getFloorId().equals(floorView.getFloorId())) {
                                floorView.setCover(true, true);
                            } else {
                                floorView.setCover(false, true);
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<String> getFloorsList(String number) {
        Entrance entrance = null;
        ArrayList<String> floors = new ArrayList<>();
        for (int i = 0; i < building.getEntrances().size(); i++) {
            if (number.equalsIgnoreCase(building.getEntrances().get(i).getNumber())) {
                entrance = building.getEntrances().get(i);
                break;
            }
        }
        for (int i = 0; i < entrance.getFloors().size(); i++) {
            floors.add(entrance.getFloors().get(i).getNumber());
        }
        return floors;
    }

    private Entrance findEntranceByName(String value) {
        if (true == StringUtils.isNullOrEmpty(value)) return null;
        for (int i = 0; i < building.getEntrances().size(); i++) {
            if (true == value.equalsIgnoreCase(building.getEntrances().get(i).getNumber())) {
                return building.getEntrances().get(i);
            }
        }
        return null;
    }

    private Floor findFloorByName(Entrance entrance, String value) {
        if (null == entrance) return null;
        if (true == StringUtils.isNullOrEmpty(value)) return null;
        List<Floor> floorList = entrance.getFloors();
        for (int i = 0; i < floorList.size(); i++) {
            if (true == value.equalsIgnoreCase(floorList.get(i).getNumber())) {
                return floorList.get(i);
            }
        }
        return null;
    }

    private ArrayList<String> getEntrancesList(NestedBuilding buildingsData) {
        ArrayList<String> entrances = new ArrayList<>();
        for (int i = 0; i < buildingsData.getEntrances().size(); i++) {
            entrances.add(buildingsData.getEntrances().get(i).getNumber());
        }
        return entrances;
    }


    private FloorView findFloorById(String floorId) {
        //Log.d(TAG, "findFloorById id: " + floorId);
        for (EntranceView entranceView : entranceViewViews) {
            if (null != entranceView) {
                ArrayList<FloorView> floorViews = entranceView.getFloorViewViews();
                if (null != floorViews) {
                    for (FloorView floorView : floorViews) {
                        if (null != floorView) {
                            String modelFloorId = floorView.getFloorId();
                            if ((null != modelFloorId) && (modelFloorId.equals(floorId))) {
                                return floorView;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void changeScrollerView(boolean isActive) {
        verticalEditScroll.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
        horizontalEditScroll.setVisibility(isActive ? View.VISIBLE : View.INVISIBLE);
        verticalScroll.setVisibility(isActive ? View.INVISIBLE : View.VISIBLE);
        horizontalScroll.setVisibility(isActive ? View.INVISIBLE : View.VISIBLE);
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

    @Nullable
    private Apartment getApartmentById(String id) {
        if (true == StringUtils.isNullOrEmpty(id)) return null;
        ArrayList<Entrance> entrances = building.getEntrances();
        for (int i = 0; i < entrances.size(); i++) {
            ArrayList<Floor> floors = entrances.get(i).getFloors();
            for (int j = 0; j < floors.size(); j++) {
                ArrayList<Apartment> apartments = floors.get(j).getApartments();
                for (int k = 0; k < apartments.size(); k++) {
                    if (id.equalsIgnoreCase(apartments.get(k).getId())) {
                        return apartments.get(k);
                    }
                }
            }
        }
        return null;
    }

    @OnClick(R.id.buildingsLogoutIcon)
    public void showLogoutDialog(View view) {
        AppExitDialog dialog = new AppExitDialog();
        dialog.show(getSupportFragmentManager(), "AppExitDialog");
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

    @OnClick(R.id.buildingsInfoImageView)
    public void onInfoClick(View view) {
        FragmentManager fm = getSupportFragmentManager();
        BuildingInfoDialogFragment fragment = BuildingInfoDialogFragment.newInstance(buildingsData.getId());
        fragment.setParentActivity(this);
        fragment.show(fm, fragment.getClass().getSimpleName());
        changeInfoIconState(true);
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

    @OnClick(R.id.buildingsEditImageView)
    public void onEditButtonClick(View view) {
        if (isEditMode) {
            verticalEditScroll.setVisibility(View.GONE);
            horizontalEditScroll.setVisibility(View.GONE);
            verticalScroll.setVisibility(View.VISIBLE);
            horizontalScroll.setVisibility(View.VISIBLE);
            resetSelectedApartment();
            floorView = null;
            uncoverAll();
            showButtons(null, null, ButtonsModes.EDIT_FLOOR);
            hideButtons();
        } else {
            verticalEditScroll.setVisibility(View.VISIBLE);
            horizontalEditScroll.setVisibility(View.VISIBLE);
            verticalScroll.setVisibility(View.INVISIBLE);
            horizontalScroll.setVisibility(View.INVISIBLE);
        }

        isEditMode = !isEditMode;
        changePenIconState(isEditMode);
    }

    public void changeInfoIconState(boolean isOn) {
        infoIconView.setImageDrawable(ContextCompat.getDrawable(this, isOn ?
                R.drawable.ic_i_on :
                R.drawable.ic_i_off));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTrace.stop();
    }

    private void changePenIconState(boolean isOn) {
        penIconView.setImageDrawable(ContextCompat.getDrawable(this, isOn ?
                R.drawable.ic_crei_on :
                R.drawable.ic_crei_off));
    }

    // Buttons Display Mode enum
    public enum ButtonsModes {
        NONE, EDIT_FLOOR, ADD_APARTMENT, MOVE_OR_DELETE_APARTMENT
    }

    private class OnContainerTouchImplementation {
        private float initialX;
        private float initialY;
        private float initialTouchX;
        private float initialTouchY;
        private float initY;

        public boolean onTouch(View view, @Nullable MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    fromDrag = false;
                    initialX = mainLayout.getX();
                    initialY = mainLayout.getY();
                    initialTouchX = motionEvent.getRawX();
                    initialTouchY = motionEvent.getRawY();
                    return true;
                case MotionEvent.ACTION_UP:
                    int Xdiff = (int) (motionEvent.getRawX() - initialTouchX);
                    int Ydiff = (int) (motionEvent.getRawY() - initialTouchY);
                    if ((Math.abs(Xdiff) < 5) || (Math.abs(Ydiff) < 5)) {
                        // Do Nothing currently
                    }
                    container.updateViewLayout(mainLayout, mainLayoutParams);

                    scrollStepX = 0.0f;
                    scrollStepY = 0.0f;
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float diffX = (motionEvent.getRawX() - initialTouchX - scrollStepX);
                    scrollStepX += diffX;

                    float diffY = (motionEvent.getRawY() - initialTouchY - scrollStepY);
                    scrollStepY += diffY;

                    if ((Math.abs(diffX) > 5) || (Math.abs(diffY) > 5)) {
                        fromDrag = true;
                    }

                    float newX = (initialX + (int) (motionEvent.getRawX() - initialTouchX));
                    float newY = (initialY + (int) (motionEvent.getRawY() - initialTouchY));

                    mainLayout.setX(newX);
                    mainLayout.setY(newY);
                    boolean isSmall = mainLayout.getHeight() < container.getHeight();
                    boolean isNarrow = mainLayout.getWidth() < container.getWidth();
                    if (isSmall) {
                        mainLayout.setY(container.getHeight() - mainLayout.getHeight());
                        diffY = 0;
                    } else {
                        if (mainLayout.getY() < (container.getHeight() - mainLayout.getHeight())) {
                            mainLayout.setY(container.getHeight() - mainLayout.getHeight());
                            diffY = 0;
                            // vertical scroll to bottom
                            verticalScroll.scrollTo(0, mainLayout.getHeight());
                        } else if (-5 < mainLayout.getY()) {
                            mainLayout.setY(0);
                            diffY = 0;
                            // vertical scroll to top
                            verticalScroll.scrollTo(0, 0);
                        }
                    }
                    if (isNarrow) {
                        mainLayout.setX(0);
                        diffX = 0;
                    } else {
                        if (mainLayout.getX() > 0) {
                            mainLayout.setX(0);
                            diffX = 0;
                            // horizontal scroll to left
                            horizontalScroll.scrollTo(0, 0);
                        } else if (mainLayout.getX() < (container.getWidth() - mainLayout.getWidth())) {
                            mainLayout.setX(container.getWidth() - mainLayout.getWidth());
                            diffX = 0;
                            // horizontal scroll to right
                            horizontalScroll.scrollTo(mainLayout.getWidth(), 0);
                        }
                    }
                    updateScrollbars(diffX, diffY);
                    return true;
            }
            return false;
        }
    }
}
