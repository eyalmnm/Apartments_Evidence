package md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class FailedToAddFragment extends DialogFragment {

    @BindView(R.id.messageTextView)
    public TextView messageTextView;

    private boolean applyBack;

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.nested_building_new_entrance_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.nested_building_failure_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_failure_dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String message = getArguments().getString(Constants.NAME_MESSAGE, "");
        applyBack = getArguments().getBoolean(Constants.NAME_APPLY_BACK, false);
        if (true == StringUtils.isNullOrEmpty(message)) {
            messageTextView.setVisibility(View.INVISIBLE);
        } else {
            messageTextView.setText(message);
            messageTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (true == applyBack) {
            getActivity().onBackPressed();
        }
    }

}