package md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class NewEntranceEditorFragment extends DialogFragment {

    // Inter Fragment communicator
    public interface OnNewEntranceAddListener {
        void onNewEntranceAdd(String order, int numOfFloors, String name);
    }
    private OnNewEntranceAddListener listener;

    // Screen components
    @BindView(R.id.nestedBuildingNewEntranceEditText)
    public EditText titleEditText;
    @BindView(R.id.nestedBuildingNewEntranceNumberOfFloorsEditText)
    public EditText numOfFloorsEditText;
    @BindView(R.id.nestedBuildingNewEntranceOkButton)
    public Button okButton;

    // Helpers
    private String order;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnNewEntranceAddListener) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.nested_building_new_entrance_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.nested_building_new_entrance_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_entrance_editor, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        order = getArguments().getString(Constants.NAME_ENTRANCE_ORDER);
        numOfFloorsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    okButton.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.nestedBuildingNewEntranceOkButton)
    public void onOkButtonClick(View view) {
        String title = titleEditText.getText().toString();
        String number = numOfFloorsEditText.getText().toString();
        if (true == StringUtils.isNullOrEmpty(title)) {
            showToast("Title name can not be empty");
        } else if (true == StringUtils.isNullOrEmpty(number)) {
            showToast("Number of floors can not be empty");
        } else {
            if (null != listener) {
                listener.onNewEntranceAdd(order, Integer.parseInt(number), title);
            }
            dismiss();
        }
    }

    @OnClick(R.id.nestedBuildingNewEntranceCancelButton)
    public void onCancelButtonClick(View view) {
        dismiss();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        View view = toast.getView();
        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        TextView textView = view.findViewById(android.R.id.message);
        textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        textView.setTextColor(getResources().getColor(R.color.colorBlack));
        toast.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
