package com.amsakib.personalmanager;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.amsakib.personalmanager.adapters.ExpenseCategoryAdapter;
import com.amsakib.personalmanager.database.ExpenseManagementService;
import com.amsakib.personalmanager.models.Expense;
import com.amsakib.personalmanager.models.ExpenseCategory;
import com.amsakib.personalmanager.models.Income;

import java.util.Date;
import java.util.List;

public class ExpenseManagementActivity extends AppCompatActivity {

    private ExpenseManagementService expenseManagementService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expemene_management);

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
                    //Dismiss once everything is OK.
                    dialog.dismiss();

                });
            });
            dialog.show();

        } else {
            // we do have available income
            // current month summary


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

                    dialog.dismiss();
                });
            });
            dialog.show();
        });



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