package com.amsakib.dressadviser_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DressExpert dressExpert = new DressExpert();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSuggestDress(View view) {
        Spinner types = (Spinner) findViewById(R.id.spinnerType);
        String selectedType = String.valueOf(types.getSelectedItem());

        Intent intent = new Intent(MainActivity.this, SuggestionActivity.class);
        intent.putExtra("selectedType", selectedType);
        startActivity(intent);
    }
}