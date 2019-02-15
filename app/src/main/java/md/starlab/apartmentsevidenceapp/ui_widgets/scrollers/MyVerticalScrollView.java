package md.starlab.apartmentsevidenceapp.ui_widgets.scrollers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MyVerticalScrollView extends FrameLayout {
    private static final String TAG = "MyVerticalScrollView";

    // View Components
    private ScrollView scrollView;
    private RelativeLayout relativeLayout;
    private LinearLayout layout = null;
    private LinearLayout coverLayout;

    private volatile boolean fromDrag = false;
    private MotionEvent downEvent;

    public MyVerticalScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public MyVerticalScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyVerticalScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        scrollView = new ScrollView(getContext());
        relativeLayout = new RelativeLayout(getContext());
        layout = new LinearLayout(getContext());

        // Init RelativeLayout (Wrapper)
        LinearLayout.LayoutParams wrapperParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        scrollView.addView(relativeLayout, wrapperParams);

        // Init the container layout
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set Scroll additional features
        scrollView.setScrollBarSize(0);
        scrollView.setVerticalScrollBarEnabled(false);
        // Do not allow to scroll but do not blocks clicks
        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("Eyal", "scrollView onTouch");
                return true;
            }
        });


        // Add to the FrameLayout
        relativeLayout.addView(layout, params);
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

    public ScrollView getScrollView() {
        return scrollView;
    }

    public boolean addViewTo(View view) {
        layout.addView(view);
        relativeLayout.setMinimumHeight(scrollView.getHeight());
        scrollView.scrollTo(scrollView.getScrollX(), layout.getHeight());
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 10);
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
