package md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Apartment;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;
import md.starlab.apartmentsevidenceapp.utils.DimenUtils;


public class FloorView extends LinearLayout {
    private static final String TAG = "FloorView";

    // Floor's properties
    private String floorId;
    private String title;
    private int floorNumber;
    private EntranceView hostEntrance;
    private OnBuildingWidgetClickListener listener;
    private int half;

    // View properties
    private Context context;
    private ArrayList<Apartment> apartments;
    private LinearLayout apartmentsContainer;
    private LinearLayout apartmentsContainerTop;
    private LinearLayout apartmentsContainerBottom;
    private View floorCover;
    private View floorDeleteCover;
    private View layout;
    private boolean editMode = false;
    private LayoutParams viewsParam;
    private ArrayList<View> apartmentsViews;

    public FloorView(Context context, String id, String title, EntranceView hostEntrance) {
        super(context);
        init(id, title, hostEntrance);
    }

    private void init(final String id, String title, EntranceView hostEntrance) {
        // Init Floor's properties
        this.floorId = id;
        this.title = title;
        this.hostEntrance = hostEntrance;

        // Init View's properties
        this.context = getContext();
        apartments = new ArrayList<>();

        // Inflate the main view.
        layout = inflate(context, R.layout.item_nested_building_floor_layout, null);
        apartmentsContainerTop = layout.findViewById(R.id.apartmentsContainerTop);
        apartmentsContainerBottom = layout.findViewById(R.id.apartmentsContainerBottom);
        apartmentsContainer = layout.findViewById(R.id.apartmentsContainer);
        floorCover = layout.findViewById(R.id.floorApartmentsContainerCover);
        floorDeleteCover = layout.findViewById(R.id.floorDeleteCover);
        addView(layout);

        apartments = new ArrayList<>();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != listener) {
                    listener.onWidgetClicked(FloorView.this, id);
                }
            }
        });
        viewsParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) DimenUtils.convertDpToPixel((float) 5, context);
        viewsParam.setMargins(margin, margin, margin, margin);
    }

    public boolean hasApartments() {
        return null != apartments && false == apartments.isEmpty();
    }

    public ArrayList<View> getApartmentsViews() {
        return apartmentsViews;
    }

    public boolean delegateOnClick() {
        if (false == editMode) {
            if (listener != null) {
                return listener.onWidgetClicked(FloorView.this, getFloorId());
            }
        }
        return false;
    }

    /**
     * This method MUST run On UI Thread
     *
     * @param apartments apartments to be displayed
     * @param listener   listener to apartment's click
     */
    public void setApartments(final ArrayList<Apartment> apartments,
                              int half,
                              OnBuildingWidgetClickListener listener) {
        this.listener = listener;
        if (false == this.apartments.isEmpty()) {
            this.apartments.clear();
            this.apartments.addAll(apartments);
        } else {
            this.apartments = apartments;
        }
        this.half = half;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                apartmentsViews = new ArrayList<>();
                for (int i = 0; i < FloorView.this.apartments.size(); i++) {
                    View view = inflate(context, R.layout.item_nested_building_apartment_layout, null);
                    TextView apartmentItemTitleTextView = view.findViewById(R.id.apartmentItemTitleTextView);
                    apartmentItemTitleTextView.setText(FloorView.this.apartments.get(i).getNumber());
                    View apartmentItemIndicatorView = view.findViewById(R.id.apartmentItemIndicatorView);
                    //Log.d(TAG, "status color: " +  FloorView.this.apartments.get(i).getStatus());
                    ColorsUtils.setBgColorDrawable(apartmentItemIndicatorView, FloorView.this.apartments.get(i).getStatus());
                    view.setTag(FloorView.this.apartments.get(i));
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (false == delegateOnClick()) {
                                if (null != FloorView.this.listener) {
                                    FloorView.this.listener.onWidgetClicked(null, ((Apartment) v.getTag()).getId());
                                }
                            }
                        }
                    });
                    apartmentsViews.add(view);
                    if (FloorView.this.half > i) {
                        apartmentsContainerBottom.addView(view, viewsParam);
                    } else {
                        apartmentsContainerTop.addView(view, viewsParam);
                    }
                }
                if (FloorView.this.half > FloorView.this.apartments.size()) {
                    for (int i = FloorView.this.apartments.size(); i < FloorView.this.half; i++) {
                        View view = inflate(context, R.layout.item_nested_building_apartment_layout, null);
                        view.setVisibility(INVISIBLE);
                        apartmentsContainerBottom.addView(view, viewsParam);
                    }
                }
                invalidate();
            }
        });
    }

    public String getFloorId() {
        return floorId;
    }

    public String getTitle() {
        return title;
    }

    public void setCover(boolean cover, boolean editMode) {
        this.editMode = editMode;
        if (null != floorCover) {
            if (true == cover) {
                FrameLayout.LayoutParams coverParams = new FrameLayout.LayoutParams(apartmentsContainerBottom.getWidth(),
                        getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_height));
                floorCover.setLayoutParams(coverParams);
                floorCover.setVisibility(VISIBLE);
            } else {
                floorCover.setVisibility(GONE);
            }
        }
    }

    public void setFloorDeleteCover(boolean cover, boolean editMode) {
        this.editMode = editMode;
        if (null != floorDeleteCover) {
            if (true == cover) {
                floorDeleteCover.setVisibility(VISIBLE);
            } else {
                floorDeleteCover.setVisibility(GONE);
            }
        }
    }

    public float getFloorWidth() {
        return getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_width);
    }

    public float getFloorHeight() {
        return getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_height);
    }

    public EntranceView getHostEntrance() {
        return hostEntrance;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void addApartMode(boolean addAp) {
        if (true == addAp) {
            apartmentsContainer.setBackgroundColor(getResources().getColor(R.color.nested_building_buttons_bg_color)); // Green
        } else {
            apartmentsContainer.setBackgroundColor(getResources().getColor(R.color.building_floor_bg_color));
        }
    }

    public boolean containsApartment(String apartmentId) {
        boolean containsApartment = false;
        for(Apartment apartment: apartments) {
            if(apartment.getId().equalsIgnoreCase(apartmentId)) {
                containsApartment = true;
            }
        }
        return containsApartment;
    }
}
