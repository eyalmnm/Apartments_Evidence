package md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs;

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

public class DeleteFloorNotEmptyFragment extends DialogFragment {

    @BindView(R.id.messageTextView)
    public TextView messageTextView;


    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.nested_building_delete_floor_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.nested_building_delete_floor_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_floor_not_empty_dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageTextView.setText(R.string.nested_building_delete_floor_not_empty_message);
    }

    @OnClick(R.id.closeButton)
    public void onCloseButtonClick(View view) {
        dismiss();
    }
}
