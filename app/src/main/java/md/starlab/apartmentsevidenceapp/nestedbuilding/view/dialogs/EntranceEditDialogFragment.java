package md.starlab.apartmentsevidenceapp.nestedbuilding.view.dialogs;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.config.Constants;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Entrance;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Floor;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.editors.NewFloorInEntranceEditorFragment;
import md.starlab.apartmentsevidenceapp.nestedbuilding.view.nested_building.FloorView;
import md.starlab.apartmentsevidenceapp.nestedbuilding.viewmodel.NestedBuildingViewModel;
import md.starlab.apartmentsevidenceapp.utils.StringUtils;


/**
 * Created by Sergey Ostrovsky
 * on 9/5/18
 */
public class EntranceEditDialogFragment extends DialogFragment implements DeleteFloorEmptyFragment.OnFloorDeletingListener,
        DeleteEntranceEmptyFragment.OnEntranceDeletingListener {

    // List Components
    @BindView(R.id.floorsRecyclerView)
    public RecyclerView floorsRecyclerView;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private EntranceFloorsAdapter floorsAdapter;
    private ArrayList<Floor> floorsList;
    private ArrayList<FloorView> floorViewList = new ArrayList<>();

    @BindView(R.id.empty_view)
    public TextView addFloorInEmptyEntranceTextView;

    @BindView(R.id.entranceTitleTextView)
    public TextView entranceTitleTextView;

    private Entrance entrance;

    // Nested Building ViewModel
    private NestedBuildingViewModel viewModel;

    public static EntranceEditDialogFragment newInstance(Entrance entrance) {
        EntranceEditDialogFragment fragment = new EntranceEditDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable(Constants.NAME_ENTRANCE, entrance);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        int width = (getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_width) + (2 * getResources().getDimensionPixelSize(R.dimen.nested_building_floor_card_padding)));
        int height = getResources().getDimensionPixelSize(R.dimen.entrance_edit_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entrance = getArguments().getParcelable(Constants.NAME_ENTRANCE);
        floorsList = entrance.getFloors();

        for (int i = 0; i < floorsList.size(); i++) {
            floorViewList.add(new FloorView(getContext(), floorsList.get(i).getId(), floorsList.get(i).getNumber(), null));
        }

        int maxApartments = 0;
        for (int i = 0; i < floorsList.size(); i++) {
            maxApartments = (floorsList.get(i).getApartments().size() > maxApartments) ? floorsList.get(i).getApartments().size() : maxApartments;
        }
        int half = (maxApartments + 1) / 2;
        half = (4 >= half) ? 4 : half;
        // Add Apartments
        for (int i = 0, j = floorsList.size() - 1; i < floorsList.size(); i++, j--) {
            floorViewList.get(j).setApartments(floorsList.get(i).getApartments(), half,null);
        }

        // Init view Model
        viewModel = ViewModelProviders.of(this).get(NestedBuildingViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entrance_edit_dialog, null);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, view);

        entranceTitleTextView.setText(entrance.getNumber());

        floorsAdapter = new EntranceFloorsAdapter(getActivity(), floorViewList, floorsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewLayoutManager = linearLayoutManager;
        floorsRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        floorsRecyclerView.setAdapter(floorsAdapter);

        if(floorsList.isEmpty()) {
            floorsRecyclerView.setVisibility(View.GONE);
            addFloorInEmptyEntranceTextView.setVisibility(View.VISIBLE);
            addFloorInEmptyEntranceTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddFloorDialog(1);
                }
            });
        } else {
            floorsRecyclerView.setVisibility(View.VISIBLE);
            addFloorInEmptyEntranceTextView.setVisibility(View.GONE);

            if (floorViewList.size() > 0) {
                floorsRecyclerView.smoothScrollToPosition(floorViewList.size() - 1);
            }
        }

        return view;
    }

    @OnClick(R.id.deleteEntranceImageView)
    public void deleteEntrance(View view){
        showDeleteEntranceDialog();
    }

    private void showDeleteFloorDialog(FloorView floorView, Floor floor) {
        Bundle data = new Bundle();
        data.putString( Constants.NAME_FLOOR_DATA,
                        floor.getId());
        DialogFragment fragment;
        if (floorView.hasApartments()) {
            fragment = new DeleteFloorNotEmptyFragment();
        } else {
            fragment = new DeleteFloorEmptyFragment();
        }
        fragment.setArguments(data);
        fragment.show(getActivity().getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }

    private void showAddFloorDialog(int floorOrder) {
        Bundle args = new Bundle();
        args.putInt(Constants.NAME_FLOOR_ORDER, floorOrder);                // Minimum value is 1
        args.putString(Constants.NAME_ENTRANCE_ID, entrance.getId());       // Entrance uuId
        FragmentManager fm = getActivity().getSupportFragmentManager();
        NewFloorInEntranceEditorFragment fragment = new NewFloorInEntranceEditorFragment();
        fragment.setArguments(args);
        fragment.show(fm, fragment.getClass().getSimpleName());
    }

    @Override
    public void onFloorDeleting(String floorId, String floorNumber) {
        dismiss();
    }

    private void showDeleteEntranceDialog() {
        Bundle data = new Bundle();
        data.putParcelable(Constants.NAME_ENTRANCE_DATA, entrance);
        DialogFragment fragment;

        if (entrance.hasApartments()) {
            fragment = new DeleteEntranceNotEmptyFragment();
        } else {
            fragment = new DeleteEntranceEmptyFragment();
        }
        fragment.setArguments(data);
        fragment.show(getActivity().getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }

    @Override
    public void onEntranceDeleting(String entranceId, String entranceTitle) {
        dismiss();
    }

    /**
     * Entry Floors List Adapter
     */
    private class EntranceFloorsAdapter extends RecyclerView.Adapter<EntranceFloorsAdapter.ViewHolder> {
        private Context context;
        private ArrayList<FloorView> floorViewList = new ArrayList<>();
        private ArrayList<Floor> floorsList = new ArrayList<>();

        public EntranceFloorsAdapter(Context context, ArrayList<FloorView> floorViewList, ArrayList<Floor> floorsList) {
            this.context = context;
            if (null != floorViewList) {
                this.floorViewList = floorViewList;
            }

            if(null != floorsList) {
                this.floorsList = floorsList;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(   R.layout.item_entrance_floor_list_layout,
                            parent,
                            false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            final int invertedPosition = (floorViewList.size() - position - 1);

            if(position >= 0) {

                if(position == 0) {
                    viewHolder.addFloorAboveTextView.setVisibility(View.VISIBLE);
                    viewHolder.addFloorAboveTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int newFloorOrder = 0;
                            int floorsSum = floorsList.size();
                            if(floorsSum > 0) {
                                Floor lastFloor         = floorsList.get(floorsSum-1);
                                String lastFloorOrder   = lastFloor.getOrder();

                                if (!StringUtils.isNullOrEmpty(lastFloorOrder)) {
                                    newFloorOrder = Integer.valueOf(lastFloorOrder);
                                }
                            }
                            newFloorOrder++;
                            showAddFloorDialog(newFloorOrder);
                        }
                    });
                }

                FloorView floorView = floorViewList.get(position);
                floorView.setFloorDeleteCover(true, true);

                viewHolder.floorViewContainer.removeView(floorView);
                viewHolder.floorViewContainer.addView(floorView);

                final String addFloorBelowTextViewTag = Constants.ADD_FLOOR_TEXT_VIEW_TAG + invertedPosition;
                viewHolder.addFloorBelowTextView.setTag(addFloorBelowTextViewTag);
                viewHolder.addFloorBelowTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int floorOrder = 0;
                        String floorOrderStr = floorsList.get(invertedPosition).getOrder();
                        if (!StringUtils.isNullOrEmpty(floorOrderStr)) {
                            floorOrder = Integer.valueOf(floorOrderStr);
                        }

                        if (floorOrder > 1) {
                            floorOrder--;
                        }
                        showAddFloorDialog(floorOrder);
                    }
                });

                final String floorDeleteCoverTag = Constants.FLOOR_DELETE_COVER_TAG + position;
                final FloorView FLOOR_VIEW = floorView;
                final Floor floor = floorsList.get(invertedPosition);

                LinearLayout floorDeleteCover = floorView.findViewById(R.id.floorDeleteCover);
                floorDeleteCover.setTag(floorDeleteCoverTag);
                floorDeleteCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeleteFloorDialog(FLOOR_VIEW, floor);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return floorViewList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout floorViewContainer;
            public TextView addFloorAboveTextView;
            public TextView addFloorBelowTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                floorViewContainer = itemView.findViewById(R.id.floorViewContainer);
                addFloorAboveTextView = itemView.findViewById(R.id.addFloorAboveTextView);
                addFloorBelowTextView = itemView.findViewById(R.id.addFloorBelowTextView);
            }
        }
    }
}
