package md.starlab.apartmentsevidenceapp.nestedbuilding.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.nestedbuilding.model.Apartment;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;

public class ApartmentsGridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Apartment> apartments;

    private LayoutInflater inflater;

    public ApartmentsGridAdapter(Context context, ArrayList<Apartment> apartments) {
        this.context = context;
        this.apartments = apartments;

        inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Apartment> apartments) {
        this.apartments = apartments;
    }

    @Override
    public int getCount() {
        return apartments.size();
    }

    @Override
    public Object getItem(int position) {
        return apartments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.item_nested_building_apartment_layout, null);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        if (null == viewHolder) {
            viewHolder = new ViewHolder();
            viewHolder.apartmentItemTitleTextView = convertView.findViewById(R.id.apartmentItemTitleTextView);
            viewHolder.apartmentItemTitleTextView.setRotationY(180);
            viewHolder.apartmentItemIndicatorView = convertView.findViewById(R.id.apartmentItemIndicatorView);
            convertView.setTag(viewHolder);
        }
        viewHolder.apartmentItemTitleTextView.setText(apartments.get(position).getNumber());
        ColorsUtils.setBgColorDrawable(viewHolder.apartmentItemIndicatorView, apartments.get(position).getStatus());

        return convertView;
    }

    private class ViewHolder {
        TextView apartmentItemTitleTextView;
        View apartmentItemIndicatorView;
    }
}
