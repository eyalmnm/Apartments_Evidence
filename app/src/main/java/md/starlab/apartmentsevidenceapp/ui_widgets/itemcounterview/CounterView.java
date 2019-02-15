package md.starlab.apartmentsevidenceapp.ui_widgets.itemcounterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;

public class CounterView extends RelativeLayout implements View.OnClickListener {
    public static final String TAG = CounterView.class.getSimpleName();
    private TextView itemCounterValue;
    private TextView titleTextView;
    private ImageButton incButton;
    private ImageButton decButton;
    private Drawable incIcon;
    private Drawable decIcon;
    private Drawable incDecBackground;
    private Drawable viewBackground;
    private ViewGroup rootView;
    private int strokeWidthRef = 0;
    private float incDecViewBorderWidth;
    private int incDecViewBorderColor;
    private int incDecViewCounterValueColor;
    private Drawable incDecViewCounterValueBgColor;
    private float defWidthValue;
    private CounterListener listener;
    private ArrayList<String> values;
    private int index = 0;
    private String title;

    public CounterView(Context context) {
        super(context);
        def(context);
        init(context, null, 0);
    }

    public CounterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        def(context);
        init(context, attrs, 0);
    }

    public CounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        def(context);
        init(context, attrs, defStyleAttr);
    }

    private void def(Context context) {
        this.incIcon = context.getResources().getDrawable(R.drawable.ic_plus);
        this.decIcon = context.getResources().getDrawable(R.drawable.ic_remove);
        this.incDecBackground = context.getResources().getDrawable(R.drawable.counter_view_button_background_shape);
        this.viewBackground = context.getResources().getDrawable(R.drawable.counter_view_background_shape);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.view_counter_item, this);
        this.rootView = findViewById(R.id.root_view);
        this.itemCounterValue = findViewById(R.id.item_counter_value);
        this.titleTextView = findViewById(R.id.item_counter_title);
        this.incButton = findViewById(R.id.inc_button);
        this.decButton = findViewById(R.id.dec_button);
        this.incButton.setOnClickListener(this);
        this.decButton.setOnClickListener(this);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IncDecView, defStyle, 0);
            try {
                this.incIcon = a.getDrawable(R.styleable.IncDecView_inc_icon);
                this.decIcon = a.getDrawable(R.styleable.IncDecView_dec_icon);
                this.viewBackground = a.getDrawable(R.styleable.IncDecView_view_background);
                this.incDecBackground = a.getDrawable(R.styleable.IncDecView_inc_dec_button_color);
                this.incDecViewBorderWidth = a.getDimension(R.styleable.IncDecView_border_width, 0);
                this.incDecViewBorderColor = a.getColor(R.styleable.IncDecView_border_color, 0);
                this.incDecViewCounterValueColor = a.getColor(R.styleable.IncDecView_counterValueColor, 0);
                this.incDecViewCounterValueBgColor = a.getDrawable(R.styleable.IncDecView_textBackgroundColor);
                this.title = a.getString(R.styleable.IncDecView_titleText);
            } catch (Exception e) {
                Log.i(TAG, "init: " + e.getLocalizedMessage());
            } finally {
                a.recycle();
            }
//            if (this.incIcon != null)
//                this.incButton.setImageDrawable(this.incIcon);
//            if (this.decIcon != null)
//                this.decButton.setImageDrawable(this.decIcon);
            if (this.viewBackground != null)
                this.rootView.setBackgroundDrawable(this.viewBackground);
            if (this.incDecBackground != null) {
                this.incButton.setBackgroundDrawable(this.incDecBackground);
                this.decButton.setBackgroundDrawable(this.incDecBackground);
            }
            if (title != null) {
                this.titleTextView.setText(title);
            }
            if (this.incDecViewBorderWidth != 0) {
                this.setBorderWidth(this.incDecViewBorderWidth);
            }
            if (this.incDecViewBorderColor != 0) {
                this.setBorderColor_(this.incDecViewBorderColor);
            }
            if (null == this.values || 0 >= this.values.size()) {
                this.itemCounterValue.setBackgroundColor(Color.RED);
            }
            if (this.incDecViewCounterValueColor != 0) {
                this.itemCounterValue.setTextColor(getResources().getColor(R.color.colorBlack));
            }
            if (null != incDecViewCounterValueBgColor) {
                this.itemCounterValue.setBackgroundColor(getResources().getColor(R.color.footer_value_bg_color));
            }
        }
    }

    public CounterView setCounterListener(CounterListener counterListener) {
        listener = counterListener;
        return this;
    }

    public String getCounterValue() {
        String text = "";
        if (this.itemCounterValue != null)
            text = this.itemCounterValue.getText().toString();
        return text;
    }

    public void setValues(ArrayList<String> values) {
        if (null == values || 0 >= values.size()) {
            return;
        }
        this.values = values;
        index = 0;
//        itemCounterValue.setBackground(viewBackground);
        if (this.incDecViewCounterValueBgColor != null) {
//            itemCounterValue.setBackground(incDecViewCounterValueBgColor);
        }
        itemCounterValue.setText(String.valueOf(values.get(0)));
    }

    public CounterView setIncButtonIcon(@DrawableRes int incButtonIcon) {
        if (this.incButton != null)
            this.incButton.setImageDrawable(getDrawable(incButtonIcon));
        return this;
    }

    public CounterView setDecButtonIcon(@DrawableRes int decButtonIcon) {
        if (this.decButton != null)
            this.decButton.setImageDrawable(getDrawable(decButtonIcon));
        return this;
    }

    public CounterView setBorderWidth(@DimenRes int strokeWidth) {
        this.strokeWidthRef = strokeWidth;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
        drawable.setStroke((int) getDimension(strokeWidth), getResources().getColor(android.R.color.darker_gray));
        if (rootView != null)
            rootView.setBackgroundDrawable(drawable);
        return this;
    }

    private CounterView setBorderWidth(float value) {
        this.defWidthValue = value;
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
        drawable.setStroke((int) value, getResources().getColor(android.R.color.darker_gray));
        if (rootView != null)
            rootView.setBackgroundDrawable(drawable);
        return this;
    }

    public CounterView setBorderColor(@ColorRes int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
        if (this.strokeWidthRef != 0)
            drawable.setStroke((int) getDimension(this.strokeWidthRef), getColor(strokeColor));
        else
            drawable.setStroke((int) getDimension(R.dimen.inc_dec_counter_view_stroke_width), getColor(strokeColor));
        if (rootView != null)
            rootView.setBackgroundDrawable(drawable);
        return this;
    }

    private CounterView setBorderColor_(int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(getDimension(R.dimen.inc_dec_counter_view_corner_radius));
        if (this.defWidthValue != 0)
            drawable.setStroke((int) defWidthValue, color);
        else
            drawable.setStroke((int) getDimension(R.dimen.inc_dec_counter_view_stroke_width), color);
        if (rootView != null)
            rootView.setBackgroundDrawable(drawable);
        return this;
    }

    private int getColor(@ColorRes int colorRes) {
        return getContext().getResources().getColor(colorRes);
    }

    private float getDimension(@DimenRes int dimenID) {
        return getContext().getResources().getDimension(dimenID);
    }

    private String getString(@StringRes int textResourceValue) {
        return getContext().getString(textResourceValue);
    }

    private Drawable getDrawable(@DrawableRes int drawableResource) {
        return getContext().getResources().getDrawable(drawableResource);
    }

    @Override
    public void onClick(View view) {
        if (null == values) return;
        int i = view.getId();
        if (i == R.id.inc_button) {
            if ((index + 1) < values.size())
                index++;
            this.itemCounterValue.setText(String.valueOf(values.get(index)));
            if (this.listener != null)
                this.listener.onIncClick(this.itemCounterValue.getText().toString());
        } else if (i == R.id.dec_button) {
            if (0 < index)
                index--;

            this.itemCounterValue.setText(String.valueOf(values.get(index)));
            if (this.listener != null)
                this.listener.onDecClick(this.itemCounterValue.getText().toString());
        }
    }

    public void scrollTo(String value) {
        if (true == values.contains(value)) {
            index = values.indexOf(value);
            this.itemCounterValue.setText(String.valueOf(values.get(index)));
        }
    }

    public void scrollFloorTo(String value) {
        if (true == values.contains(value)) {
            index = values.indexOf(value);
            int invertedIndex = (values.size() - index - 1);
            if(invertedIndex >= 0) {
                this.itemCounterValue.setText(String.valueOf(values.get(invertedIndex)));
            }
        }
    }
}
