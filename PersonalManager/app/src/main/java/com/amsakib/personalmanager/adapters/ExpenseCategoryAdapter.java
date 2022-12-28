package com.amsakib.personalmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amsakib.personalmanager.R;
import com.amsakib.personalmanager.models.ExpenseCategory;

import java.util.List;

public class ExpenseCategoryAdapter extends ArrayAdapter<ExpenseCategory> {

   public ExpenseCategoryAdapter(Context context, List<ExpenseCategory> categories) {
        super(context, 0, categories);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.expense_category_list_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.categoryListItemTitle);
        ExpenseCategory currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            textViewName.setText(currentItem.getName());
        }
        return convertView;
    }
}

