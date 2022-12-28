package com.amsakib.personalmanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amsakib.personalmanager.adapters.ExpenseCategoryAdapter;
import com.amsakib.personalmanager.database.ExpenseManagementService;
import com.amsakib.personalmanager.models.BalanceSummary;
import com.amsakib.personalmanager.models.CategorySummary;
import com.amsakib.personalmanager.models.Expense;
import com.amsakib.personalmanager.models.ExpenseCategory;
import com.amsakib.personalmanager.models.Income;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpenseManagementActivity extends AppCompatActivity {

    private ExpenseManagementService expenseManagementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_management);

        expenseManagementService = new ExpenseManagementService(this);

        if(!expenseManagementService.haveIncomeValues()) {
            Toast.makeText(this, "We don't have any income value", Toast.LENGTH_SHORT).show();
            final EditText beginningBalanceEditText = new EditText(this);
            beginningBalanceEditText.setHint("Beginning Balance Amount");
            beginningBalanceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("No beginning balance.")
                    .setMessage("No beginning balance is set. Please enter a beginning balance.")
                    .setView(beginningBalanceEditText)
                    .setCancelable(false)
                    .setPositiveButton("Set", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button dialogPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                dialogPositiveButton.setOnClickListener(view -> {
                    String text = String.valueOf(beginningBalanceEditText.getText());
                    double beginningBalance = -1;
                    try{
                        beginningBalance = Double.parseDouble(text);
                    } catch (Exception ex) {

                    }

                    if(beginningBalance <= 0) {
                        Toast.makeText(ExpenseManagementActivity.this, "Please enter a positive amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addIncome(beginningBalance, "Beginning Balance");
                    updateSummaryView();
                    //Dismiss once everything is OK.
                    dialog.dismiss();

                });
            });
            dialog.show();

        } else {

            updateSummaryView();

        }

        Button btnAddAmount = findViewById(R.id.btnAddAmount);
        Button btnAddExpense = findViewById(R.id.btnAddExpense);
        Button btnHistory = findViewById(R.id.btnHistory);


        btnAddAmount.setOnClickListener(view -> {

            LayoutInflater inflater = LayoutInflater.from(ExpenseManagementActivity.this);
            View dialogBody = inflater.inflate(R.layout.fragment_add_amount_dialog, null);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add Amount")
                    .setMessage("Please enter an amount and source of the amount.")
                    .setView(dialogBody)
                    .setPositiveButton("Add Amount", null)
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button dialogPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                dialogPositiveButton.setOnClickListener(view1 -> {
                    EditText dialogAmount = dialogBody.findViewById(R.id.dialogAmount);
                    EditText dialogSource = dialogBody.findViewById(R.id.dialogSource);

                    String dialogAmountText = String.valueOf(dialogAmount.getText());
                    double amount = -1;
                    try{
                        amount = Double.parseDouble(dialogAmountText);
                    } catch (Exception ex) {

                    }

                    String dialogSourceText = String.valueOf(dialogSource.getText());

                    if(amount <= 0) {
                        Toast.makeText(ExpenseManagementActivity.this, "Please enter a positive amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(dialogSourceText.equals("")) {
                        Toast.makeText(ExpenseManagementActivity.this, "Please enter a source", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addIncome(amount, dialogSourceText);
                    updateSummaryView();
                    dialog.dismiss();
                });
            });
            dialog.show();
        });

        btnAddExpense.setOnClickListener(view -> {

            LayoutInflater inflater = LayoutInflater.from(ExpenseManagementActivity.this);
            View dialogBody = inflater.inflate(R.layout.fragment_add_expense_dialog_fragment, null);

            // set the category list inside the spinner of the dialog fragment
            Spinner spinner = dialogBody.findViewById(R.id.dialogCategory);
            List<ExpenseCategory> categories = expenseManagementService.getAllExpenseCategories();
            categories.add(0, new ExpenseCategory(-1, "Select Category"));
            ExpenseCategoryAdapter adapter = new ExpenseCategoryAdapter(ExpenseManagementActivity.this, categories);
            spinner.setAdapter(adapter);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Add Expense")
                    .setMessage("Please enter an expenditure amount and select a category.")
                    .setView(dialogBody)
                    .setPositiveButton("Add Expense", null)
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button dialogPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                dialogPositiveButton.setOnClickListener(view1 -> {

                    EditText dialogAmount = dialogBody.findViewById(R.id.dialogAmount);

                    String dialogAmountText = String.valueOf(dialogAmount.getText());
                    double amount = -1;
                    try{
                        amount = Double.parseDouble(dialogAmountText);
                    } catch (Exception ex) {

                    }

                    if(amount <= 0) {
                        Toast.makeText(ExpenseManagementActivity.this, "Please enter a positive amount.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ExpenseCategory category = (ExpenseCategory) spinner.getSelectedItem();
                    if(category.getId() <0) {
                        Toast.makeText(ExpenseManagementActivity.this, "Please select a category.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Expense expense = new Expense();
                    expense.setCategoryId(category.getId());
                    expense.setAmount(amount);
                    expense.setDate(new Date());

                    expenseManagementService.addExpense(expense);
                    updateSummaryView();
                    dialog.dismiss();
                });
            });
            dialog.show();
        });

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(ExpenseManagementActivity.this, ExpenseHistoryActivity.class);
            startActivity(intent);
        });

    }

    private void updateSummaryView() {
        LinearLayout layout = findViewById(R.id.visibleArea);
        layout.setVisibility(View.VISIBLE);

        TextView monthName = findViewById(R.id.monthName);
        Calendar calendar = Calendar.getInstance();
        monthName.setText(new SimpleDateFormat("MMMM, yyyy").format(calendar.getTime()));

        BalanceSummary summary = expenseManagementService.getBalanceSummary(calendar);

        TextView previousBalance = findViewById(R.id.previousBalance);
        TextView totalIncome = findViewById(R.id.totalIncome);
        TextView expense = findViewById(R.id.totalExpense);
        TextView balance = findViewById(R.id.balance);

        previousBalance.setText(String.format("%.2f", summary.getPreviousBalance()));
        totalIncome.setText(String.format("%.2f", summary.getTotalIncome()));
        expense.setText(String.format("%.2f", summary.getTotalExpense()));
        balance.setText(String.format("%.2f", summary.getBalance()));


        List<CategorySummary> summaries = expenseManagementService.getExpenseSummary(calendar);

        drawChart(summaries);
    }

    private void drawChart(List<CategorySummary> summaries) {
        List<String> colors = new ArrayList<>(Arrays.asList("#FFA726", "#66BB6A", "#EF5350", "#29B6F6", "#E91E63", "#FFBB86FC", "#FF000000", "#FF018786"));
        PieChart pieChart = findViewById(R.id.piechart);
        pieChart.clearChart();
        LinearLayout pieChartDetails = findViewById(R.id.pieChartDetails);
        pieChartDetails.removeAllViews();

        for(int i = 0; i< summaries.size(); i++) {
            CategorySummary categorySummary = summaries.get(i);
            pieChart.addPieSlice(
                    new PieModel(
                            categorySummary.getName(),
                            (float) categorySummary.getAmount(),
                            Color.parseColor(colors.get(i % summaries.size()))));

            Context context = getApplicationContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.pie_chart_details_item, null);
            View colorView = view.findViewById(R.id.color);
            TextView titleView = view.findViewById(R.id.title);

            colorView.setBackgroundColor(Color.parseColor(colors.get(i % summaries.size())));
            titleView.setText(String.format("%s - %.2f", categorySummary.getName(), categorySummary.getAmount()));

            pieChartDetails.addView(view);
        }

        // To animate the pie chart
        pieChart.startAnimation();
    }

    private void addIncome(double amount, String dialogSourceText) {
        Income income = new Income();
        income.set_amount(amount);
        income.set_source(dialogSourceText);
        income.set_date(new Date());

        // save income to the database
        expenseManagementService.addIncome(income);
    }
}