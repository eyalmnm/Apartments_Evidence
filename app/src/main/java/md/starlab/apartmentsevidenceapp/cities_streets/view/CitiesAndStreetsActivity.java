package md.starlab.apartmentsevidenceapp.cities_streets.view;

import android.animation.Animator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.base.BaseActivity;
import md.starlab.apartmentsevidenceapp.buildings.view.BuildingsActivity;
import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.cities_streets.model.StreetData;
import md.starlab.apartmentsevidenceapp.cities_streets.viewmodel.CitiesViewModel;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.dialogs.AppExitDialog;


// Ref: https://stackoverflow.com/questions/10318642/highlight-for-selected-item-in-expandable-list

public class CitiesAndStreetsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CitiesAndStreetsAct";

    private final int ANIMATION_DURATION = 1000;

    // Screen components
    @BindView(R.id.citiesUserNameTextView)
    public TextView citiesUserNameTextView;

    // Cities ListView properties
    @BindView(R.id.citiesListView)
    public ExpandableListView citiesListView;

    // Animation Component
    @BindView(R.id.citiesWhitCircleImageView)
    public ImageView citiesWhitCircleImageView;
    @BindView(R.id.citiesStarNetLogoImageView)
    public ImageView logoImageView;

    private ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    private CitiesAdapter adapter;

    // Helpers
    private Context context;
    private float whiteCircleTargetHeight = 0;
    private float whiteCircleTargetWidth = 0;
    private float screenW;
    private float screenH;
    private float circleSize;

    // Cities ViewModel
    private CitiesViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTag(Dynamic.tagCitiesAndStreets);
        setContentView(R.layout.cities_and_streets_activity);
        ButterKnife.bind(this);
        context = this;


        screenW = Dynamic.screenWidth;
        screenH = Dynamic.screenHeight;
        circleSize = getResources().getDimensionPixelSize(R.dimen.login_circle_size);

        // Add user name
        citiesUserNameTextView.setText(String.valueOf(Dynamic.USERNAME));

        // Init Cities ListView
        cityDataArrayList = new ArrayList<>();
        adapter = new CitiesAdapter(context);
        citiesListView.setAdapter(adapter);
        //listener for child row click
        citiesListView.setOnChildClickListener(myListItemClicked);
        //listener for group heading click
        citiesListView.setOnGroupClickListener(myListGroupClicked);
        citiesListView.setGroupIndicator(null);

        // Init view Model
        viewModel = ViewModelProviders.of(this).get(CitiesViewModel.class);

        context = this;
        // Init Scree size properties
        whiteCircleTargetHeight = Dynamic.screenHeight;
        whiteCircleTargetWidth = Dynamic.screenWidth;


        initSnLogo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForBackStep();
        loadingCitiesList();
        citiesListView.setVisibility(View.INVISIBLE);
    }

    private void loadingCitiesList() {
        viewModel.getCitiesAndStreets(Dynamic.TOKEN).observe(this, new Observer<DataWrapper<List<CityData>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<CityData>> citiesDataWrapper) {
                Log.d(TAG, "loadingCitiesList onChanged");
                if (null != citiesDataWrapper.getThrowable()) {
                    Toast.makeText(context, getString(R.string.cities_error_message, citiesDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                }
                if (null != citiesDataWrapper.getData()) {
                    List<CityData> response = citiesDataWrapper.getData();
                    updateCitiesList(response);
                }
            }
        });
    }

    private void updateCitiesList(final List<CityData> cities) {
        if (1 > cities.size()) {
            Toast toast = Toast.makeText(context, "City list is empty. Please contact the operator!", Toast.LENGTH_SHORT);
            View view = toast.getView();
            view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            TextView textView = view.findViewById(android.R.id.message);
            textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            textView.setTextColor(getResources().getColor(R.color.colorBlack));
            toast.show();
        }
        Log.d(TAG, "updateCitiesList: " + cities.size());
        loadListBackground();
        cityDataArrayList = (ArrayList<CityData>) cities;
        adapter.notifyDataSetInvalidated();
    }

    private void initSnLogo() {
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                Animation starNetLogoRotation = AnimationUtils.loadAnimation(context, R.anim.logo_rotation);
//                starNetLogoRotation.setInterpolator(new DecelerateInterpolator());
//                logoImageView.startAnimation(starNetLogoRotation);
                float shift = screenW / 12f;
                logoImageView.setVisibility(View.VISIBLE);
                logoImageView.setX(circleSize / 1.5f);
                float logoEndScale = 0.5f;
                logoImageView.animate()
                        .setDuration(ANIMATION_DURATION)
                        .setInterpolator(new AnticipateOvershootInterpolator())
                        .scaleX(logoEndScale)
                        .scaleY(logoEndScale)
                        .translationX(shift);
            }
        }, 50);
    }

    private void loadListBackground() {
        citiesWhitCircleImageView.setVisibility(View.VISIBLE);
        citiesWhitCircleImageView.setX(-circleSize);
        citiesWhitCircleImageView.animate()
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .translationX(-circleSize / 2.3f)
                .scaleX(2.0f)
                .scaleY(2.0f)
                .setListener(new Animator.AnimatorListener() {
                    @Override   public void onAnimationStart(Animator animator) { }
                    @Override   public void onAnimationEnd(Animator animator) {
                        showCitiesListView();
                    }
                    @Override   public void onAnimationCancel(Animator animator) {  }
                    @Override   public void onAnimationRepeat(Animator animator) {  }
                });
    }

    private void showCitiesListView() {
        citiesListView.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.enter_from_left);
        anim.setDuration(350);
        citiesListView.startAnimation(anim);
    }

    @OnClick(R.id.citiesLogoutIcon)
    public void showLogoutDialog(View view) {
        AppExitDialog dialog = new AppExitDialog();
        dialog.show(getSupportFragmentManager(), "AppExitDialog");
    }

    // AdapterView.OnItemClickListener method
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(context, StreetsActivity.class);
        intent.putExtra(Constants.NAME_CITY_DATA, cityDataArrayList.get(i));
        startActivity(intent);
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out_none);
    }

    //our child listener
    private ExpandableListView.OnChildClickListener myListItemClicked =  new ExpandableListView.OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the child data
            CityData cityData = cityDataArrayList.get(groupPosition);
            StreetData streetData = cityData.getStreets().get(childPosition);
            //display it or do something with it
            //Toast.makeText(getBaseContext(), "Clicked on Street " +  streetData.getName(), Toast.LENGTH_LONG).show();
            // Move to the next screen
            Intent intent = new Intent(context, BuildingsActivity.class);
            intent.putExtra(Constants.NAME_STREET_DATA, streetData);
            intent.putExtra(Constants.NAME_CITY_DATA, cityData);
            startActivity(intent);
            return false;
        }
    };

    //our group listener
    private ExpandableListView.OnGroupClickListener myListGroupClicked =  new ExpandableListView.OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            //get the group data
            CityData cityData = cityDataArrayList.get(groupPosition);
            //display it or do something with it
//            Toast.makeText(getBaseContext(), "Clicked on City " + cityData.getName(),
//                    Toast.LENGTH_LONG).show();

            return false;
        }
    };


    private class CitiesAdapter extends BaseExpandableListAdapter {

        private LayoutInflater inflater;
        private Context context;

        public CitiesAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }


        @Override
        public int getGroupCount() {
            return cityDataArrayList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return cityDataArrayList.get(groupPosition).getStreets().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return cityDataArrayList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return cityDataArrayList.get(groupPosition).getStreets().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (null == view) {
                view = inflater.inflate(R.layout.item_cities_list_layout, null);
            }

            TextView textView = view.findViewById(R.id.item_cities_list_text_view);
            textView.setText(String.valueOf(cityDataArrayList.get(groupPosition).getName()));

            if (true == ((ExpandableListView)parent).isGroupExpanded(groupPosition)) {
                //view.setBackgroundResource(R.drawable.cities_list_item_background_shape_green);
                textView.setBackgroundResource(R.drawable.cities_list_item_background_shape_green);
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
            } else {
                //view.setBackgroundResource(R.drawable.cities_list_item_background_shape_light);
                textView.setBackgroundResource(R.drawable.cities_list_item_background_shape_light);
                textView.setTextColor(getResources().getColor(R.color.colorBlack));
            }

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
            if (null == view) {
                view = inflater.inflate(R.layout.item_streets_list_layout, null);
            }

            TextView textView = view.findViewById(R.id.item_streets_list_text_view);
            textView.setText(String.valueOf(cityDataArrayList.get(groupPosition).getStreets().get(childPosition).getName()));

            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
