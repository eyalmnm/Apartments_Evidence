package md.starlab.apartmentsevidenceapp.cities_streets.view;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.buildings.view.BuildingsActivity;
import md.starlab.apartmentsevidenceapp.cities_streets.model.CityData;
import md.starlab.apartmentsevidenceapp.cities_streets.model.StreetData;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.dialogs.AppExitDialog;

public class StreetsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "StreetsActivity";

    private final int ANIMATION_DURATION = 1000;

    // Screen components
    @BindView(R.id.streetsUserNameTextView)
    public TextView streetsUserNameTextView;
    @BindView(R.id.streetCityNameTitleTextView)
    public TextView streetCityNameTitleTextView;

    // Cities ListView properties
    @BindView(R.id.streetsListView)
    public ListView streetsListView;

    // "Animation" Component
    @BindView(R.id.streetsWhitCircleImageView)
    public ImageView streetsWhitCircleImageView;

    private ArrayList<StreetData> streetDataArrayList = new ArrayList<>();
    private StreetsAdapter adapter;

    // Helpers
    private Context context;
    private float whiteCircleTargetHeight = 0;
    private float whiteCircleTargetWidth = 0;
    private float screenW;
    private float screenH;
    private float circleSize;

    private CityData cityData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streets);
        ButterKnife.bind(this);
        context = this;

        screenW = Dynamic.screenWidth;
        screenH = Dynamic.screenHeight;
        circleSize = getResources().getDimensionPixelSize(R.dimen.login_circle_size);

        // Init Scree size properties
        whiteCircleTargetHeight = Dynamic.screenHeight;
        whiteCircleTargetWidth = Dynamic.screenWidth;

        // Hide the list and the title
        streetsListView.setVisibility(View.INVISIBLE);
        streetCityNameTitleTextView.setVisibility(View.INVISIBLE);

        // Init City data
        Intent intent = getIntent();
        cityData = intent.getParcelableExtra(Constants.NAME_CITY_DATA);
        streetDataArrayList = cityData.getStreets();

        // init the white circle
        streetsWhitCircleImageView.setX(-circleSize);
        streetsWhitCircleImageView.setVisibility(View.VISIBLE);
        streetsWhitCircleImageView.animate()
                .setDuration(50)
                .translationX(screenW / 2f)
                .scaleY(1.25f);

        // Add user name
        streetsUserNameTextView.setText(String.valueOf(Dynamic.USERNAME));

        // Init City name title
        streetCityNameTitleTextView.setText(cityData.getName());

        // Init streets list
        adapter = new StreetsAdapter(context);
        streetsListView.setAdapter(adapter);
        streetsListView.setOnItemClickListener(this);

        // Loading list to screen
        loadCityNameTitle();
        //initStreetsData();
    }

    private void initStreetsData() {
        streetsListView.setVisibility(View.VISIBLE);
        Animation listViewAnim = AnimationUtils.loadAnimation(context, R.anim.enter_from_left);
        listViewAnim.setDuration(ANIMATION_DURATION);
        listViewAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        streetsListView.startAnimation(listViewAnim);
    }

    @OnClick(R.id.streetsLogoutIcon)
    public void showLogoutDialog(View view) {
        AppExitDialog dialog = new AppExitDialog();
        dialog.show(getSupportFragmentManager(), "AppExitDialog");
    }

    private void loadCityNameTitle() {
        streetCityNameTitleTextView.setVisibility(View.VISIBLE);
        Animation titleAnim = AnimationUtils.loadAnimation(context, R.anim.enter_from_left);
        titleAnim.setDuration(ANIMATION_DURATION);
        titleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        titleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initStreetsData();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        streetCityNameTitleTextView.startAnimation(titleAnim);
    }

    // AdapterView.OnItemClickListener implementation
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemClick");
        StreetData streetData = streetDataArrayList.get(i);
        Intent intent = new Intent(context, BuildingsActivity.class);
        intent.putExtra(Constants.NAME_STREET_DATA, streetData);
        intent.putExtra(Constants.NAME_CITY_DATA, cityData);
        startActivity(intent);
    }

    @OnClick(R.id.streetCityNameTitleTextView)
    public void onCityTitleClick(View view) {
        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out_none);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private class StreetsAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;

        public StreetsAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return streetDataArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return streetDataArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertedView, ViewGroup viewGroup) {
            View view = convertedView;
            if (null == view) {
                view = inflater.inflate(R.layout.item_cities_list_layout, null);
            }

            TextView textView = view.findViewById(R.id.item_cities_list_text_view);
            textView.setText(String.valueOf(streetDataArrayList.get(i).getName()));

            return view;
        }
    }
}
