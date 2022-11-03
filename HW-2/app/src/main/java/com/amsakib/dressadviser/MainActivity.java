package com.amsakib.dressadviser;

import androidx.appcompat.app.AppCompatActivity;

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
        TextView suggestion = (TextView) findViewById(R.id.tvSuggestion);
        Spinner types = (Spinner) findViewById(R.id.spinnerType);
        String selectedType = String.valueOf(types.getSelectedItem());
        List<String> suggestionsFromExpert = dressExpert.getDressSuggestion(selectedType);
        StringBuilder formattedSuggestion = new StringBuilder();
        for (String expertSuggestion : suggestionsFromExpert) {
            formattedSuggestion.append(expertSuggestion).append('\n');
        }
        suggestion.setText(formattedSuggestion);

    }
}