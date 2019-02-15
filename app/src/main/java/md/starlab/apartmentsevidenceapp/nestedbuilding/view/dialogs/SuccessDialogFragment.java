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

import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;

public class SuccessDialogFragment extends DialogFragment {

    private boolean applyBack;

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
        View view = inflater.inflate(R.layout.fragment_success_dialog, null);
        Bundle args = getArguments();
        if (null != args) {
            applyBack = args.getBoolean(Constants.NAME_APPLY_BACK, false);
        }
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMainImageClick(v);
            }
        });
        return view;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (true == applyBack) {
            getActivity().onBackPressed();
        }
    }

    @OnClick(R.id.mainImage)
    public void onMainImageClick(View view) {
        dismiss();
    }
}
