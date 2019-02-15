package md.starlab.apartmentsevidenceapp.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

// Ref: https://stackoverflow.com/questions/17823451/set-android-shape-color-programmatically

public class ColorsUtils {

    public static void setBgColorDrawable(View view, String colorStr) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(getColor(colorStr));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(getColor(colorStr));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(getColor(colorStr));
        }
    }

    public static void setBgColorDrawable(View view, int color) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(color);
        }
    }

    public static int getColor(String colorStr) {
        return Color.parseColor(colorStr);
    }
}
