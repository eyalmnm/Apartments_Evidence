package md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.ui_widgets.layouts.MyLinearLayout;
import md.starlab.apartmentsevidenceapp.utils.DimenUtils;

public class EntranceView extends MyLinearLayout {
    private static final String TAG = "EntranceView";

    // Entrance's properties
    private String entranceId;
    private String title;
    private String order;
    private OnBuildingWidgetClickListener listener;

    // View properties
    private Context context;
    private ArrayList<md.starlab.apartmentsevidenceapp.nestedbuilding.model.Floor> floors;
    private ArrayList<FloorView> floorViewViews = null;
    private int padding = 0;
    private int minimumWidth;
    private int minimumHight;


    public EntranceView(Context context, String id, String title, String order, OnBuildingWidgetClickListener listener) {
        super(context);
        init(id, title, order, listener);
    }

    private void init(String id, String title, String order, final OnBuildingWidgetClickListener listener) {
        // Init Entrance's properties
        this.entranceId = id;
        this.title = title;
        this.listener = listener;
        this.order = order;

        // Init View's properties
        this.context = getContext();
        setOrientation(LinearLayout.VERTICAL);
        minimumWidth = getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_width);
        minimumHight = getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_height);
        padding = (int) DimenUtils.convertDpToPixel(5, this.context);
        setPadding(padding, padding, padding, padding);
        floors = new ArrayList<>();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(minimumWidth, minimumHight);
        setLayoutParams(params);
    }

    public String getEntranceId() {
        return entranceId;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<FloorView> getFloorViewViews() {
        return floorViewViews;
    }

    public String getOrder() {
        return order;
    }

    /**
     * This method MUST run On UI Thread
     *
     * @param floors floors to be displayed
     */
    public void setFloors(ArrayList<md.starlab.apartmentsevidenceapp.nestedbuilding.model.Floor> floors) {
        if ((null != floors) && (false == floors.isEmpty())) {
            if (false == this.floors.isEmpty()) {
                this.floors.clear();
                this.floors.addAll(floors);
            } else {
                this.floors = floors;
            }
            redrawFloors();
        } else {
            FloorView tempFloorView = new FloorView(context, "", "", this);
            tempFloorView.setVisibility(INVISIBLE);
            addView(tempFloorView);
        }
    }

    private void redrawFloors() {
        // Convert Floor Model array to Floor View array
        floorViewViews = new ArrayList<>(floors.size());
        for (int i = 0; i < floors.size(); i++) {
            floorViewViews.add(new FloorView(context, floors.get(i).getId(), floors.get(i).getNumber(), this));
            addView(floorViewViews.get(i));
            addView(getAddFloorButton(i));
        }

        int maxApartments = 0;
        for (int i = 0; i < floors.size(); i++) {
            maxApartments = (floors.get(i).getApartments().size() > maxApartments) ? floors.get(i).getApartments().size() : maxApartments;
        }
        int half = (maxApartments + 1) / 2;
        half = (4 >= half) ? 4 : half;
        // Add Apartments
        for (int i = 0, j = floors.size() - 1; i < floors.size(); i++, j--) {
            floorViewViews.get(j).setApartments(floors.get(i).getApartments(), half, this.listener);
        }
    }

    private View getAddFloorButton(int id) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_width) - 18),      // Return in Pixels
                getResources().getDimensionPixelSize(R.dimen.add_floor_text_view_height));                  // Return in Pixels

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.horizontal_edit_scroll_add_floor_background_shape);
        textView.setTag(Constants.ADD_FLOOR_TEXT_VIEW_TAG + id);
        textView.setVisibility(GONE);

        return textView;
    }
}
