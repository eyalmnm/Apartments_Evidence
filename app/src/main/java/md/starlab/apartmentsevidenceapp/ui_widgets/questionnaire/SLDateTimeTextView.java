package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;


/**
 * Created by Sergey Ostrovsky
 * on 7/10/18
 */
public class SLDateTimeTextView extends LinearLayout implements SLWidgetInterface {

    // UI Components
    private TextView timeText;
    private TextView textView;

    private String questionId;
    private String questionTitle;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    // My Listener listen to time selection
    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker picker,
                                      int hour, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    SLDateTimeTextView.this.hour = hour;
                    SLDateTimeTextView.this.minute = minute;
                    StringBuilder sb = new StringBuilder();
                    sb.append(hour).append(":");
                    sb.append(minute);
                    timeText.setText(sb.toString());
                }
            };
    // My Listener listen to date selection
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker picker,
                                      int year, int month, int day) {
                    calendar.set(year, month, day);
                    StringBuilder sb = new StringBuilder();
                    SLDateTimeTextView.this.year = year;
                    SLDateTimeTextView.this.month = month;
                    SLDateTimeTextView.this.day = day;
                    sb.append(day).append("/");
                    sb.append(month).append("/");
                    sb.append(year);
                    timeText.setText(sb.toString());
                }
            };

    public SLDateTimeTextView(Context context, String questionId, String questionTitle, final boolean isDate) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;

        int titleMarginTop = getResources().getDimensionPixelSize(R.dimen.question_titles_margin_top);
        int contentMarginTop = getResources().getDimensionPixelSize(R.dimen.question_content_margin_top);
        int contentMarginLeft = getResources().getDimensionPixelSize(R.dimen.question_content_margin_left);
        int textPaddingH = getResources().getDimensionPixelSize(R.dimen.question_text_padding_h);
        int textPaddingV = getResources().getDimensionPixelSize(R.dimen.question_text_padding_v);

        // Init the Calendar with current time
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);

        setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(contentMarginLeft, titleMarginTop, 0, 0);

        textView = new TextView(context);
        textView.setText(questionTitle);
        textView.setTextColor(getResources().getColor(R.color.question_title));
        textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.question_title_text_size));
        if (false == StringUtils.isNullOrEmpty(questionTitle)) {
            addView(textView, tvParams);
        }

        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        etParams.setMargins(0, contentMarginTop, 0, 0);

        timeText = new TextView(context);
        timeText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        timeText.setTextColor(getResources().getColor(R.color.question_title));
        timeText.setTextSize((getResources().getDimensionPixelSize(R.dimen.question_option_text_size)));
        timeText.setBackgroundDrawable(getResources().getDrawable(R.drawable.sl_edit_text_bg_shape));
        timeText.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
        addView(timeText, etParams);

        // Init the UI Component
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDate) {
                    openDatePicker();
                } else {
                    openTimePicker();
                }
            }
        });
        if (isDate) {
            StringBuilder sb = new StringBuilder();
            SLDateTimeTextView.this.year = year;
            SLDateTimeTextView.this.month = month;
            SLDateTimeTextView.this.day = day;
            sb.append(day).append("/");
            sb.append(month).append("/");
            sb.append(year);
            timeText.setText(sb.toString());
        } else {
            SLDateTimeTextView.this.hour = hour;
            SLDateTimeTextView.this.minute = minute;
            StringBuilder sb = new StringBuilder();
            sb.append(hour).append(":");
            sb.append(minute);
            timeText.setText(sb.toString());
        }
    }

    private void openTimePicker() {
        final TimePickerDialog timeDialog = new TimePickerDialog(getContext(), myTimeListener, hour, minute, true);
        timeDialog.show();
    }

    private void openDatePicker() {
        final DatePickerDialog dateDialog = new DatePickerDialog(getContext(),
                myDateListener, year, month, day);
        dateDialog.show();
    }

    @Override
    public String getQuestionId() {
        return questionId;
    }

    @Override
    public String getQuestionTitle() {
        return questionTitle;
    }

    @Override
    public String getQuestionAnswer() {
        return String.valueOf(calendar.getTimeInMillis());
    }
}
