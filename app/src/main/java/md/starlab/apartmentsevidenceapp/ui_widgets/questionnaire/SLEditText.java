package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class SLEditText extends LinearLayout implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;

    // UI Components
    private EditText editText;
    private TextView textView;

    public SLEditText(Context context, String questionId, String questionTitle) {
        this(context, questionId, questionTitle, InputType.TYPE_CLASS_TEXT);
    }


    public SLEditText(Context context, String questionId, String questionTitle, int inputType) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;

        // Init Component
        setOrientation(LinearLayout.VERTICAL);

        int titleMarginTop = getResources().getDimensionPixelSize(R.dimen.question_titles_margin_top);
        int contentMarginTop = getResources().getDimensionPixelSize(R.dimen.question_content_margin_top);
        int contentMarginLeft = getResources().getDimensionPixelSize(R.dimen.question_content_margin_left);
        int textPaddingH = getResources().getDimensionPixelSize(R.dimen.question_text_padding_h);
        int textPaddingV = getResources().getDimensionPixelSize(R.dimen.question_text_padding_v);

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

        editText = new EditText(context);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        editText.setTextColor(getResources().getColor(R.color.question_title));
        editText.setInputType(inputType);
        editText.setTextSize((getResources().getDimensionPixelSize(R.dimen.question_option_text_size)));
        editText.setBackgroundDrawable(getResources().getDrawable(R.drawable.sl_edit_text_bg_shape));
        editText.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
        addView(editText, etParams);
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
        return editText.getText().toString();
    }
}
