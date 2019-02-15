package md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs;

import android.content.Context;
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
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Entrance;

public class DeleteEntranceEmptyFragment extends DialogFragment {

    @BindView(R.id.messageTextView)
    public TextView messageTextView;
    private OnEntranceDeletingListener listener;
    // Helper
    private Entrance entrance;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnEntranceDeletingListener) context;
    }

    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.nested_building_delete_floor_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.nested_building_delete_empty_entrance_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_entrance_empty_dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        entrance = args.getParcelable(Constants.NAME_ENTRANCE_DATA);
        messageTextView.setText(R.string.nested_building_delete_entrance_empty_warning);
    }

    @OnClick(R.id.noButton)
    public void onNoButtonClick(View view) {
        dismiss();
    }

    @OnClick(R.id.yesButton)
    public void onYesButtonClick(View view) {
        if (null != listener)
            listener.onEntranceDeleting(entrance.getId(), entrance.getNumber());
        dismiss();
    }

    public interface OnEntranceDeletingListener {
        void onEntranceDeleting(String entranceId, String entranceTitle);
    }
}
