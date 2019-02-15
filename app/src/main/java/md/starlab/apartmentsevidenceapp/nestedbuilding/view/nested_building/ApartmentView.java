package md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;


// Ref: https://stackoverflow.com/questions/17823451/set-android-shape-color-programmatically

public class ApartmentView extends LinearLayout {
    private static final String TAG = "ApartmentView";

    // Apartment's properties
    private String apartmentId;
    private String title;
    private String status;
    private OnBuildingWidgetClickListener listener;
    private FloorView hostFloor;

    public ApartmentView(Context context, String id, String title, String status, FloorView hostFloor, OnBuildingWidgetClickListener listener) {
        super(context);
        Log.d(TAG, "Apartment: " + title);
        init(id, title, status, hostFloor, listener);
    }

    private void init(String id, String title, String status, final FloorView hostFloor, OnBuildingWidgetClickListener listener) {
        // Init Apartment's properties
        this.apartmentId = id;
        this.title = title;
        this.status = status;
        this.listener = listener;
        this.hostFloor = hostFloor;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // VOVA - BACK
//                if (false == hostFloor.delegateOnClick()) {
//                    if (null != ApartmentView.this.listener) {
//                        ApartmentView.this.listener.onWidgetClicked(ApartmentView.this, apartmentId);
//                    }
//                }
                if (null != ApartmentView.this.listener) {
                        ApartmentView.this.listener.onWidgetClicked(ApartmentView.this, apartmentId);
                    }
            }
        });
        View view = inflate(getContext(), R.layout.item_nested_building_apartment_layout, null);
        TextView titleTV = view.findViewById(R.id.apartmentItemTitleTextView);
        View statusView = view.findViewById(R.id.apartmentItemIndicatorView);

        titleTV.setText(String.valueOf(this.title));
        if (false == StringUtils.isNullOrEmpty(status)) {
            Drawable background = statusView.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(status));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(Color.parseColor(status));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(Color.parseColor(status));
            }
        }

        addView(view);
        invalidate();
    }

    public String getApartmentId() {
        return apartmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
