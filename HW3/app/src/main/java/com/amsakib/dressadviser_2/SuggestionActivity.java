package com.amsakib.dressadviser_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class SuggestionActivity extends AppCompatActivity {

    private DressExpert dressExpert = new DressExpert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        Intent intent = getIntent();
        String selectedType = intent.getStringExtra("selectedType");

        TextView selectedTypeTextView = (TextView) findViewById(R.id.tvSelectedType);
        selectedTypeTextView.setText(String.format("Here are some suggestions for dress type: %s",
                selectedType));

        TextView suggestion = (TextView) findViewById(R.id.tvSuggestion);
        List<String> suggestionsFromExpert = dressExpert.getDressSuggestion(selectedType);
        StringBuilder formattedSuggestion = new StringBuilder();
        for (String expertSuggestion : suggestionsFromExpert) {
            formattedSuggestion.append(expertSuggestion).append('\n');
        }
        suggestion.setText(formattedSuggestion);

    }

    public void onClickGoBackButton(View view) {
        finish();
    }
}