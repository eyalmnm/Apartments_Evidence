package md.starlab.apartmentsevidenceapp.history.apartment.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import md.starlab.apartmentsevidenceapp.R;
import md.starlab.apartmentsevidenceapp.history.apartment.model.BuildingAssignedTransaction;
import md.starlab.apartmentsevidenceapp.utils.ColorsUtils;

public class ApartmentHistoryTransactionsAdapter extends BaseAdapter {

    private OnHistoryTractionClickListener listener;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<BuildingAssignedTransaction> transactionsList = new ArrayList<>();

    public ApartmentHistoryTransactionsAdapter(Context context, ArrayList<BuildingAssignedTransaction> transactionsList) {
        this.context = context;
        listener = (OnHistoryTractionClickListener) context;
        if (null != transactionsList) {
            this.transactionsList = transactionsList;
        }
        inflater = LayoutInflater.from(context);
    }

    /**
     * MUST run on UI thread since this method notifies the adapter.
     *
     * @param transactionsList an ArrayList of BuildingAssignedTransaction
     */
    public void setData(ArrayList<BuildingAssignedTransaction> transactionsList) {
        if (null != transactionsList) {
            this.transactionsList.clear();
            this.transactionsList.addAll(transactionsList);
        }
    }

    @Override
    public int getCount() {
        return transactionsList.size();
    }

    @Override
    public Object getItem(int i) {
        return transactionsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (null == view) {
            view = inflater.inflate(R.layout.item_apartmant_history_transctions_list_layout, null);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (null == viewHolder) {
            viewHolder = new ViewHolder();
            viewHolder.transactionsOkButton = view.findViewById(R.id.apartmentHistoryTransactionsOkButton);
            viewHolder.transactionsDescTextView = view.findViewById(R.id.apartmentHistoryTransactionsDescTextView);
            viewHolder.transactionsDateTextView = view.findViewById(R.id.apartmentHistoryTransactionsDateTextView);
            viewHolder.apartmentHistoryTransactionsButYES = view.findViewById(R.id.apartmentHistoryTransactionsButYES);
            viewHolder.apartmentHistoryTransactionsButNO = view.findViewById(R.id.apartmentHistoryTransactionsButNO);
            view.setTag(viewHolder);
        }

        final BuildingAssignedTransaction item = transactionsList.get(i);
        ColorsUtils.setBgColorDrawable(viewHolder.transactionsOkButton, item.getColor());
        viewHolder.transactionsDescTextView.setText(String.valueOf(item.getTitle()));
        viewHolder.transactionsDateTextView.setText(String.valueOf(item.getEnd_at()));
        viewHolder.apartmentHistoryTransactionsButYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((View) v.getParent().getParent()).setVisibility(View.GONE);
                ((View) v.getParent().getParent().getParent()).findViewById(R.id.apartmentHistoryTransactionsFirstView).setVisibility(View.VISIBLE);
                if (listener != null) {
                    listener.onHistoryTransactionClick(true, item);
                }
            }
        });
        viewHolder.apartmentHistoryTransactionsButNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((View) v.getParent().getParent()).setVisibility(View.GONE);
                ((View) v.getParent().getParent().getParent()).findViewById(R.id.apartmentHistoryTransactionsFirstView).setVisibility(View.VISIBLE);
                if (listener != null) {
                    listener.onHistoryTransactionClick(false, item);
                }
            }
        });
        return view;
    }

    public interface OnHistoryTractionClickListener {
        void onHistoryTransactionClick(boolean yesPressed, BuildingAssignedTransaction item);
    }

    private class ViewHolder {
        TextView transactionsOkButton;
        TextView transactionsDescTextView;
        TextView transactionsDateTextView;
        TextView apartmentHistoryTransactionsButYES;
        TextView apartmentHistoryTransactionsButNO;
    }
}