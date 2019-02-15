package md.starlab.apartmentsevidenceapp.history.apartment.view.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.history.apartment.model.ApartmentTransactionAnswers;

public class ApartmentHistoryListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ApartmentTransactionAnswers> historyList = new ArrayList<>();

    public ApartmentHistoryListAdapter(Context context, ArrayList<ApartmentTransactionAnswers> historyList) {
        this.context = context;
        if (null != historyList) {
            this.historyList = historyList;
        }
        inflater = LayoutInflater.from(context);
    }

    private static void setBgColorDrawable(View view, String colorStr) {
        Drawable background = view.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(Color.parseColor(colorStr));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(Color.parseColor(colorStr));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(Color.parseColor(colorStr));
        }
    }

    /**
     * MUST run on UI thread since this method notifies the adapter.
     *
     * @param historyList an ArrayList of ApartmentTransactionAnswers
     */
    public void setData(ArrayList<ApartmentTransactionAnswers> historyList) {
        if (null != historyList) {
            this.historyList = historyList;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = inflater.inflate(R.layout.item_apartmant_history_list_layout, null);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (null == viewHolder) {
            viewHolder = new ViewHolder();
            viewHolder.circleImageView = view.findViewById(R.id.apartmentHistoryStatusCircleImageView);
            viewHolder.dateTextView = view.findViewById(R.id.apartmentHistoryDateTextView);
            viewHolder.descriptionTextView = view.findViewById(R.id.apartmentHistoryDescriptionTextView);
            view.setTag(viewHolder);
        }

        ApartmentTransactionAnswers item = historyList.get(i);
        viewHolder.dateTextView.setText(String.valueOf(item.getCreated_at()));
        viewHolder.descriptionTextView.setText(String.valueOf(item.getTitle()));
        setBgColorDrawable(viewHolder.circleImageView, item.getColor());

        return view;
    }

    private class ViewHolder {
        ImageView circleImageView;
        TextView dateTextView;
        TextView descriptionTextView;
    }
}
