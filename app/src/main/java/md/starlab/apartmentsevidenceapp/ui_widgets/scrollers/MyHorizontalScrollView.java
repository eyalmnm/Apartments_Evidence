package md.starlab.apartmentsevidenceapp.ui_widgets.scrollers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class MyHorizontalScrollView extends FrameLayout {
    private static final String TAG = "HorizontalScrollView";

    // View Components
    private HorizontalScrollView scrollView;
    private LinearLayout layout = null;
    private LinearLayout coverLayout;

    private volatile boolean fromDrag = false;
    private MotionEvent downEvent;
    private GestureDetector gestureDetector;


    public MyHorizontalScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyHorizontalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        scrollView = new HorizontalScrollView(getContext());
        layout = new LinearLayout(getContext());
        layout.setClickable(true);
        layout.setFocusable(true);

        // Init the container layout
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.LEFT;
        layout.setOrientation(LinearLayout.HORIZONTAL);

        // Set Scroll additional features
        scrollView.setScrollBarSize(0);
        scrollView.setHorizontalFadingEdgeEnabled(false);
        // Do not allow to scroll but do not blocks clicks
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Eyal", "scrollView onTouch");
                return true;
            }
        });

        // Add to the FrameLayout
        scrollView.addView(layout, params);
        addView(scrollView);

        // Transparent layout to block scrolling
        coverLayout = new LinearLayout(getContext());
        coverLayout.setBackgroundColor(Color.TRANSPARENT);
        coverLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        fromDrag = false;
                        downEvent = motionEvent;
                        return false;
                    case MotionEvent.ACTION_MOVE:
                        fromDrag = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        return fromDrag;
                }
                return true;  // Consume all events
            }
        });

        coverLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        addView(coverLayout);
    }


    public HorizontalScrollView getScrollView() {
        return scrollView;
    }

    public boolean addViewTo(View view) {
        layout.addView(view);
        return true;
    }

    public void removeAllViews() {
        layout.removeAllViews();
    }

    @Override
    public void scrollTo(int x, int y) {
        scrollView.scrollTo(x, y);
    }

    @Override
    public void scrollBy(int x, int y) {
        scrollView.scrollBy(x, y);
    }
}
