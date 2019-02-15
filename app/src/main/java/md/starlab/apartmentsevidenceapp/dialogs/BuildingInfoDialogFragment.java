package md.starlab.apartmentsevidenceapp.dialogs;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.config.Dynamic;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.history.apartment.model.DataWrapper;
import md.starlab.apartmentsevidenceapp.history.apartment.viewmodel.ApartmentHistoryViewModel;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.NestedBuildingActivity;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;


/**
 * Created by Sergey Ostrovsky
 * on 8/30/18
 */
public class BuildingInfoDialogFragment extends DialogFragment {

    // List Components
    @BindView(R.id.buildingInfoListView)
    public ListView buildingInfoListView;
    private BuildingInfoAdapter buildingInfoAdapter;
    private ArrayList<BuildingAssignedTransaction> buildingInfoDataList = new ArrayList<>();

    private ApartmentHistoryViewModel viewModel;

    private String buildingId;
    private NestedBuildingActivity nestedBuildingActivity;

    public static BuildingInfoDialogFragment newInstance(String buildingId) {
        BuildingInfoDialogFragment fragment = new BuildingInfoDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(Constants.NAME_BUILDING_ID, buildingId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.building_info_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.building_info_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildingId = getArguments().getString(Constants.NAME_BUILDING_ID);
        //Toast.makeText(getActivity(), "Building identifier: " +buildingId, Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_building_info_dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);

        buildingInfoAdapter = new BuildingInfoAdapter(getActivity(), buildingInfoDataList);
        buildingInfoListView.setAdapter(buildingInfoAdapter);

        // Init view Model
        viewModel = ViewModelProviders.of(this).get(ApartmentHistoryViewModel.class);
        loadingBuildingInfoData();

        return view;
    }

    private void loadingBuildingInfoData() {
        viewModel.getTransaction(buildingId, Dynamic.TOKEN).observe(this, new Observer<DataWrapper<List<BuildingAssignedTransaction>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<BuildingAssignedTransaction>> listDataWrapper) {
                //if (null != progressDialog) progressDialog.hide();
                if (null != listDataWrapper.getThrowable()) {
                    Toast.makeText(getActivity(), getString(R.string.cities_error_message,
                            listDataWrapper.getThrowable().getMessage()),
                            Toast.LENGTH_SHORT).show();
                } else {
                    buildingInfoDataList = (ArrayList<BuildingAssignedTransaction>) listDataWrapper.getData();
                    if (true == buildingInfoDataList.isEmpty()) {
                        Toast.makeText(getActivity(), "No Building Information loaded from server!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                addNoStateItem();
                buildingInfoAdapter.setData(buildingInfoDataList);
            }
        });
    }

    private void addNoStateItem() {
        String noStateItemText = getResources().getString(R.string.no_state);
        String noStateItemColor = getResources().getString(R.string.no_state_item_color);

        BuildingAssignedTransaction noStateItem = new BuildingAssignedTransaction(noStateItemText,
                noStateItemColor, "", "", false, "");
        buildingInfoDataList.add(0, noStateItem);
    }

    public void setParentActivity(NestedBuildingActivity activity){
        nestedBuildingActivity = activity;
    }

//    @OnClick(R.id.closeButton)
//    public void onCloseDialog(View view) {
//        dismiss();
//    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        nestedBuildingActivity.changeInfoIconState(false);
    }



    private class BuildingInfoAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<BuildingAssignedTransaction> buildingInfoDataList = new ArrayList<>();

        public BuildingInfoAdapter(Context context, ArrayList<BuildingAssignedTransaction> buildingInfoDataList) {
            this.context = context;
            if (null != buildingInfoDataList) {
                this.buildingInfoDataList = buildingInfoDataList;
            }
            inflater = LayoutInflater.from(context);
        }

        /**
         * MUST run on UI thread since this method notifies the adapter.
         *
         * @param buildingInfoDataList an ArrayList of BuildingAssignedTransaction
         */
        public void setData(ArrayList<BuildingAssignedTransaction> buildingInfoDataList) {
            if (null != buildingInfoDataList) {
                this.buildingInfoDataList = buildingInfoDataList;
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return buildingInfoDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return buildingInfoDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (null == view) {
                view = inflater.inflate(R.layout.item_building_info_list_layout, null);
            }

            ViewHolder viewHolder = (ViewHolder) view.getTag();
            if (null == viewHolder) {
                viewHolder = new ViewHolder();
                viewHolder.buildingInfoStatusIndicatorView  = view.findViewById(R.id.buildingInfoStatusIndicatorView);
                viewHolder.buildingInfoDescTextView         = view.findViewById(R.id.buildingInfoDescTextView);

                view.setTag(viewHolder);
            }

            BuildingAssignedTransaction item = buildingInfoDataList.get(i);
            ColorsUtils.setBgColorDrawable(viewHolder.buildingInfoStatusIndicatorView, item.getColor());

            String stateText = String.valueOf(item.getTitle());

            StringBuilder infoDescSB = new StringBuilder();
            infoDescSB.append(stateText);

            int coloringStartPos    = -1;
            int coloringEndPos      = -1;

            if(i != 0) {
                infoDescSB.append(" (");
                infoDescSB.append(item.getEnd_at());
                infoDescSB.append(")");

                coloringStartPos = stateText.length();
            }

            SpannableString styledString = new SpannableString(infoDescSB.toString());
            coloringEndPos = styledString.length();

            if(coloringStartPos > 0) {
                styledString.setSpan(new ForegroundColorSpan(Color.parseColor(item.getColor())), coloringStartPos, coloringEndPos, 0);
            }

            viewHolder.buildingInfoDescTextView.setText(styledString);

            return view;
        }

        private class ViewHolder {
            View        buildingInfoStatusIndicatorView;
            TextView    buildingInfoDescTextView;
        }


    }
}
