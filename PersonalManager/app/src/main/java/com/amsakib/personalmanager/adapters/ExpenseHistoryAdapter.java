package com.amsakib.personalmanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amsakib.personalmanager.R;
import com.amsakib.personalmanager.models.ExpenseCategory;
import com.amsakib.personalmanager.models.ExpenseHistoryItem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseHistoryAdapter extends ArrayAdapter<ExpenseHistoryItem> {

    public ExpenseHistoryAdapter(Context context, List<ExpenseHistoryItem> historyItems) {
        super(context, 0, historyItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item, parent, false);
        }

        TextView datePart = convertView.findViewById(R.id.datePart);
        TextView timePart = convertView.findViewById(R.id.timePart);
        TextView type = convertView.findViewById(R.id.type);
        TextView category = convertView.findViewById(R.id.category);
        TextView amount = convertView.findViewById(R.id.amount);


        ExpenseHistoryItem currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            datePart.setText( new SimpleDateFormat("dd/MM/yyyy").format(currentItem.getDate().getTime()));
            timePart.setText( new SimpleDateFormat("hh:mm a").format(currentItem.getDate().getTime()));
            type.setText(currentItem.isIncome() ? "Income" : "Expense");
            category.setText(currentItem.getCategory());
            amount.setText(String.format("%.2f", currentItem.getAmount()));

            if(currentItem.isIncome()) {
                type.setTextColor(Color.parseColor("#FF4CAF50"));
                amount.setTextColor(Color.parseColor("#FF4CAF50"));
            } else {
                type.setTextColor(Color.parseColor("#FB0055"));
                amount.setTextColor(Color.parseColor("#FB0055"));
            }
        }
        return convertView;
    }

}
