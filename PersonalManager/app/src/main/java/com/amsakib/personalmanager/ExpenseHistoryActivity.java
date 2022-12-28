package com.amsakib.personalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.amsakib.personalmanager.adapters.ExpenseHistoryAdapter;
import com.amsakib.personalmanager.database.ExpenseManagementService;
import com.amsakib.personalmanager.models.ExpenseHistoryItem;

import java.util.List;

public class ExpenseHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_history);

        ListView historyListView = findViewById(R.id.historyListView);

        ExpenseManagementService service = new ExpenseManagementService(getApplicationContext());
        List<ExpenseHistoryItem> historyItems = service.getHistoryItems();
        ExpenseHistoryAdapter adapter = new ExpenseHistoryAdapter(getApplicationContext(), historyItems);

        historyListView.setAdapter(adapter);

    }
}