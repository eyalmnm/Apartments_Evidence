package md.starlab.apartmentsevidenceapp.buildings.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.buildings.model.BuildingsData;

public class BuildingsGridAdapter extends RecyclerView.Adapter<BuildingsGridAdapter.BuildingsDataViewHolder> {

    private ArrayList<BuildingsData> buildingsDataArrayList;
    private Context context;

    public BuildingsGridAdapter(Context context, ArrayList<BuildingsData> buildingsDataArrayList) {
        this.buildingsDataArrayList = buildingsDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuildingsDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_building_grid_layout, viewGroup, false);
        return new BuildingsDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingsDataViewHolder holder, int i) {
        BuildingsData data = buildingsDataArrayList.get(i);
        holder.itemTitleTextView.setText(String.valueOf(data.getNumber()));
        Drawable indicatorBGDrawable = getDrawableBg(data.getUrgency());
        holder.itemIndicatorView.setBackground(indicatorBGDrawable);
    }

    private Drawable getDrawableBg(int urgency) {
        if (1 == urgency) {
            return context.getResources().getDrawable(R.drawable.buildings_item_indicator_bg_shape_red);
        } else if (2 == urgency) {
            return context.getResources().getDrawable(R.drawable.buildings_item_indicator_bg_shape_yellow);
        } else if (3 == urgency) {
            return context.getResources().getDrawable(R.drawable.buildings_item_indicator_bg_shape_green);
        }
        return context.getResources().getDrawable(R.drawable.buildings_item_indicator_bg_shape_white);
    }

    @Override
    public int getItemCount() {
        return buildingsDataArrayList.size();
    }

    class BuildingsDataViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitleTextView;
        private View itemIndicatorView;

        public BuildingsDataViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitleTextView = itemView.findViewById(R.id.buildingsItemTitleTextView);
            itemIndicatorView = itemView.findViewById(R.id.buildingsItemIndicatorView);
        }
    }
}
