package md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;

public class NewFloorInEntranceEditorFragment extends NewFloorEditorFragment {

    // Inter Fragment communicator
    public interface OnNewFloorInEntranceAddListener {
        void onNewFloorInEntranceAdd(int order, String name, String entranceId);
    }
    private OnNewFloorInEntranceAddListener listener;

    // Helpers
    private String entranceId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnNewFloorInEntranceAddListener) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        entranceId = getArguments().getString(Constants.NAME_ENTRANCE_ID);

        Log.e("Repository", "onViewCreated()_0: order= " +order);

        order = getArguments().getInt(Constants.NAME_FLOOR_ORDER);
        Log.e("Repository", "onViewCreated()_1: order= " +order);
    }

    @OnClick(R.id.nestedBuildingNewFloorOkButton)
    public void onOkButtonClick(View view) {
        String title = titEditText.getText().toString();
        if (true == StringUtils.isNullOrEmpty(title)) {
            showToast("Title name can not be empty");
        } else {
            if (null != listener) {
                listener.onNewFloorInEntranceAdd(order, title, entranceId);
            }
            dismiss();
        }
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
}
