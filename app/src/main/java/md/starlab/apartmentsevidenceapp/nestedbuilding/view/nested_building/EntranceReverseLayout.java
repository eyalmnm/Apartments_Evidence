package md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import md.starlab.apartmentsevidenceapp.ui_widgets.layouts.MyLinearLayout;

public class EntranceReverseLayout extends MyLinearLayout {

    // Floor's properties
    private String entranceId;
    private String title;
    private int entranceNumber;
    private OnBuildingWidgetClickListener listener;

    // View's properties
    private View[] views;

    public EntranceReverseLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public EntranceReverseLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public EntranceReverseLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public void setViews(View[] views) {
        this.views = views;

        // Add regularly
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = (int) convertDpToPixel(5, getContext());
        params.setMargins(margin, margin, margin, margin);
        setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i = 0; i < this.views.length; i++) {
            views[i].setLayoutParams(params);
            addView(views[i]);
        }

        // Reverse order
        for (int i = 0; i < views.length; i++) {
            this.removeView(views[i]);
            this.addView(views[i], views.length - 1 - i);
        }
    }
}
