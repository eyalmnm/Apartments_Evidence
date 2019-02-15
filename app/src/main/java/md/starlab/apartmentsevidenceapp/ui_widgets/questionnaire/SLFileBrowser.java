package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.utils.FileUtils;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class SLFileBrowser extends LinearLayout implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;
    private View.OnClickListener listener;
    private Uri fileUri;

    // UI Components
    private TextView button;
    private EditText descEditText;
    private TextView pathTextView;

    public SLFileBrowser(Context context, String questionId, String questionTitle, View.OnClickListener listener) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.listener = listener;


        int titleMarginTop = getResources().getDimensionPixelSize(R.dimen.question_titles_margin_top);
        int contentMarginTop = getResources().getDimensionPixelSize(R.dimen.question_content_margin_top);
        int contentMarginLeft = getResources().getDimensionPixelSize(R.dimen.question_content_margin_left);
        int textPaddingH = getResources().getDimensionPixelSize(R.dimen.question_text_padding_h);
        int textPaddingV = getResources().getDimensionPixelSize(R.dimen.question_text_padding_v);
        int boxMerginTop = getResources().getDimensionPixelSize(R.dimen.question_box_margin_top);
        int boxMerginLeft = getResources().getDimensionPixelSize(R.dimen.question_box_margin_left);

        // Init Component
        setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(HORIZONTAL);

        LinearLayout.LayoutParams tvsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tvsParams.gravity = Gravity.CENTER_VERTICAL;
        tvsParams.setMargins(0, titleMarginTop, 0, contentMarginTop);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        tvParams.setMargins(contentMarginLeft, titleMarginTop, 0, 0);



        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        inputParams.setMargins(0, contentMarginTop, 0, 0);

        LinearLayout.LayoutParams browseParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        browseParams.setMargins(contentMarginLeft, contentMarginTop, 0, 0);


        int margin = getResources().getDimensionPixelSize(R.dimen.question_questions_margin_size);
        tvParams.setMargins(0, margin, 0, 0);
        tvParams.gravity = Gravity.CENTER_VERTICAL;

        descEditText = new EditText(context);
        descEditText.setWidth((getResources().getDimensionPixelSize(R.dimen.question_attach_text_width)));
        descEditText.setTextSize((getResources().getDimensionPixelSize(R.dimen.question_option_text_size)));
        descEditText.setTextColor((getResources().getColor(R.color.question_title)));
        descEditText.setBackgroundDrawable(getResources().getDrawable(R.drawable.sl_edit_text_bg_shape));
        descEditText.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
        descEditText.setHint(getResources().getString(R.string.question_attach_hint));
        linearLayout.addView(descEditText, inputParams);

        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        btnParams.setMargins(margin, margin, 0, margin);


        button = new TextView(context);
        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_ok_button_background_shape));
        button.setWidth(getResources().getDimensionPixelSize(R.dimen.question_attach_but_width));
        button.setGravity(Gravity.CENTER);
        button.setHeight(getResources().getDimensionPixelSize(R.dimen.question_attach_but_height));
        button.setText(R.string.browse);
        button.setBackground(getResources().getDrawable(R.drawable.questio_button_shape));
        button.setTextColor(getResources().getColor(R.color.question_title));
        button.setTextSize(getResources().getDimensionPixelSize(R.dimen.question_option_text_size));
        button.setAllCaps(false);
        button.setOnClickListener(listener);
        linearLayout.addView(button, browseParams);


        addView(linearLayout, tvsParams);

        LinearLayout.LayoutParams paramsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pathTextView = new TextView(context);
        pathTextView.setTextSize((getResources().getDimensionPixelSize(R.dimen.question_info_text_size)));
        pathTextView.setPadding(contentMarginLeft, 0, 0, margin);
        pathTextView.setLayoutParams(paramsParams);
        pathTextView.setVisibility(GONE);
        addView(pathTextView);

        descEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkText();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        checkText();
    }

    @Override
    public String getQuestionId() {
        return this.questionId;
    }

    @Override
    public String getQuestionTitle() {
        return this.questionTitle;
    }

    @Override
    public String getQuestionAnswer() {
        String baseStr = descEditText.getText().toString();
        baseStr = baseStr.replace("/storage/emulated/0/", "/");
        return Environment.getExternalStorageDirectory().getAbsolutePath() + baseStr;
    }

    public String getDescription() {
        return descEditText.getText().toString();
    }

    public Uri getFileUri() { return fileUri; }

    public void setUri(Uri uri) {
        this.fileUri = uri;
        String filePath = FileUtils.getPathFromUri(getContext(), fileUri);
        int lastFSInx = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(lastFSInx + 1);
        pathTextView.setText(fileName);
        pathTextView.setVisibility(VISIBLE);
    }

    private void checkText(){
        button.setVisibility(descEditText.length() > 0 ? VISIBLE : INVISIBLE);
    }
}
