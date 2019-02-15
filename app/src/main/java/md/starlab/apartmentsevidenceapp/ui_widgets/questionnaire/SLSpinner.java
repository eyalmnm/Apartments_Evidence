package md.starlab.apartmentsevidenceapp.ui_widgets.questionnaire;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.questionnaire.model.AnswerModel;

/**
 * Created by Sergey Ostrovsky
 * on 7/10/18
 */
public class SLSpinner extends AppCompatSpinner implements SLWidgetInterface {

    private String questionId;
    private String questionTitle;
    private ArrayList<AnswerModel> answers;

    public SLSpinner(Context context, String questionId, String questionTitle, ArrayList<AnswerModel> answers) {
        this(context, questionId, questionTitle, answers, -1);
    }

    public SLSpinner(Context context, String questionId, String questionTitle, ArrayList<AnswerModel> answers, int selected) {
        super(context);
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.answers = answers;

        // Crete Items for answers
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            items.add(answers.get(i).getValue());
        }

        // Init Component
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(adapter);
        setSelection(selected);
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
        return getSelectedItem().toString();
    }
}
