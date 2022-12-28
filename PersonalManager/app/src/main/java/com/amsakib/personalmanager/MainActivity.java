package com.amsakib.personalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnExpenseManagement = findViewById(R.id.btnExpenseManagement);
        Button btnTaskManagement = findViewById(R.id.btnTaskManagement);
        Button btnJournal = findViewById(R.id.btnJournal);

        btnExpenseManagement.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ExpenseManagementActivity.class);
            startActivity(intent);
        });

        btnTaskManagement.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TaskManagementActivity.class);
            startActivity(intent);
        });

        btnJournal.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, JournalActivity.class);
            startActivity(intent);
        });
    }
}