package com.amsakib.dressadviser_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class SuggestionActivity extends AppCompatActivity {

    private DressExpert dressExpert = new DressExpert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        Intent intent = getIntent();
        String selectedType = intent.getStringExtra("selectedType");
        TextView suggestion = (TextView) findViewById(R.id.tvSuggestion);
        List<String> suggestionsFromExpert = dressExpert.getDressSuggestion(selectedType);
        StringBuilder formattedSuggestion = new StringBuilder();
        for (String expertSuggestion : suggestionsFromExpert) {
            formattedSuggestion.append(expertSuggestion).append('\n');
        }
        suggestion.setText(formattedSuggestion);

    }
}