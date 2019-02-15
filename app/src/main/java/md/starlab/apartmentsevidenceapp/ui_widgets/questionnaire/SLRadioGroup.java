package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.questionnaire.model.AnswerModel;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;


public class SLRadioGroup extends RadioGroup implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;
    private ArrayList<AnswerModel> options;
    private TextView textView;

    private RadioButton[] buttons;

    public SLRadioGroup(Context context, String questionId, String questionTitle, ArrayList<AnswerModel> options) {
        this(context, questionId, questionTitle, options, -1);
    }

    public SLRadioGroup(Context context, String questionId, String questionTitle, ArrayList<AnswerModel> options, int checked) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.options = options;

        int titleMarginTop = getResources().getDimensionPixelSize(R.dimen.question_titles_margin_top);
        int contentMarginTop = getResources().getDimensionPixelSize(R.dimen.question_content_margin_top);
        int contentMarginLeft = getResources().getDimensionPixelSize(R.dimen.question_content_margin_left);
        int textPaddingH = getResources().getDimensionPixelSize(R.dimen.question_text_padding_h);
        int textPaddingV = getResources().getDimensionPixelSize(R.dimen.question_text_padding_v);
        int boxMerginTop = getResources().getDimensionPixelSize(R.dimen.question_box_margin_top);
        int boxMerginLeft = getResources().getDimensionPixelSize(R.dimen.question_box_margin_left);

        // Init Component
        setOrientation(RadioGroup.VERTICAL);

        RadioGroup.LayoutParams tvParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(contentMarginLeft, titleMarginTop, 0, 0);

        textView = new TextView(context);
        textView.setText(questionTitle);
        textView.setTextColor(getResources().getColor(R.color.question_title));
        textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.question_title_text_size));
        if (false == StringUtils.isNullOrEmpty(questionTitle)) {
            addView(textView, tvParams);
        }

        RadioGroup.LayoutParams etParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        etParams.setMargins(boxMerginLeft, boxMerginTop, 0, 0);

        buttons = new RadioButton[options.size()];
        for (int i = 0; i < options.size(); i++) {
            buttons[i] = new RadioButton(context);
            buttons[i].setText(options.get(i).getValue());
            buttons[i].setTextSize((getResources().getDimensionPixelSize(R.dimen.question_option_text_size)));
            buttons[i].setTextColor(getResources().getColor(R.color.question_title));
            if (i == checked) buttons[i].isChecked();
            addView(buttons[i], etParams);
        }
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
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isChecked())
                return options.get(i).getKey();
        }
        return "";
    }
}
