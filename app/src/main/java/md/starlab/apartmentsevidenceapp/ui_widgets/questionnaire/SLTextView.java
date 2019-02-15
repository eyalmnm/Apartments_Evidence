package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;

/**
 * Created by Sergey Ostrovsky
 * on 7/10/18
 */
public class SLTextView extends AppCompatTextView implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;

    public SLTextView(Context context, String questionId, String questionTitle) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        setText(questionTitle);
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
        return null;
    }
}
