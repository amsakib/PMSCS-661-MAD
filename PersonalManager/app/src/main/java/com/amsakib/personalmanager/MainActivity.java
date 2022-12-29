package com.amsakib.personalmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        userName = "";

        if(!sharedPref.contains("user_name")) {
            showSetNameDialog(sharedPref, false);
        } else {
            userName = sharedPref.getString("user_name", "");
            updateUserNameTextView();
        }


        Button btnExpenseManagement = findViewById(R.id.btnExpenseManagement);
//        Button btnTaskManagement = findViewById(R.id.btnTaskManagement);
//        Button btnJournal = findViewById(R.id.btnJournal);

        btnExpenseManagement.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExpenseManagementActivity.class);
            startActivity(intent);
        });

        Button btnChangeName = findViewById(R.id.btnChangeName);

        btnChangeName.setOnClickListener(view -> {
            showSetNameDialog(sharedPref, true);
        });

//        btnTaskManagement.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, TaskManagementActivity.class);
//            startActivity(intent);
//        });
//
//        btnJournal.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, JournalActivity.class);
//            startActivity(intent);
//        });
    }

    private void showSetNameDialog(SharedPreferences sharedPref, boolean isCancelable) {
        final EditText userNameEditText = new EditText(this);
        userNameEditText.setHint("Your full name");
        userNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Welcome")
                .setMessage("Please enter your name")
                .setView(userNameEditText)
                .setPositiveButton("Set", null);

        builder.setCancelable(isCancelable);

        if(isCancelable) {
            builder.setNegativeButton("Cancel", null);
        }


        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Button dialogPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            dialogPositiveButton.setOnClickListener(view -> {
                userName = String.valueOf(userNameEditText.getText());

                if(userName.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter a valid name.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("user_name", userName);
                editor.apply();
                updateUserNameTextView();
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void updateUserNameTextView() {
        TextView userNameTextView = findViewById(R.id.userName);
        userNameTextView.setText(userName);
       // Log.d("ExpenseManagement", userName);

    }
}