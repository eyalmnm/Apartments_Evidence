package md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import md.starlab.apartmentsevidenceapp.R;

public class FloorSchemeLinearLayout extends LinearLayout {
    private static final String TAG = "FloorSchemeLinearLayout";
    // View's properties
    private LayoutParams innersParam;
    private ViewGroup[] viewGroups;
    private View[] views;
    private LayoutParams params;

    public FloorSchemeLinearLayout(Context context) {
        super(context);
    }

    public FloorSchemeLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloorSchemeLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        viewGroups = new ViewGroup[2];
        innersParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_line_height));

        int margin = (int) convertDpToPixel(0, getContext());
        innersParam.setMargins(margin, margin, margin, margin);
        for (int i = 0; i < 2; i++) {
            viewGroups[i] = new LinearLayout(getContext());
            ((LinearLayout) viewGroups[i]).setOrientation(LinearLayout.HORIZONTAL);
            viewGroups[i].setLayoutParams(innersParam);
            addView(viewGroups[i]);
        }
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public void setViews(View[] views, int itemsInGroup, int maxItems) {
        this.views = views;
        if (0 > itemsInGroup) {
            throw new ArithmeticException("Items in group can not be smaller then 1");
        }
        if (0 > maxItems) {
            throw new ArithmeticException("Max items can not be smaller then 1");
        }

        int containerIndex = 0;
        for (int i = 0; i < views.length && i < maxItems; i++) {
            containerIndex = i < itemsInGroup ? 1 : 0;
            viewGroups[containerIndex].addView(views[i]);
        }
    }
}
