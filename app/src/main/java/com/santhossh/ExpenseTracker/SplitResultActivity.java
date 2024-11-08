package com.santhossh.ExpenseTracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class SplitResultActivity extends Activity {

    private TextView resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_result);

        resultsTextView = findViewById(R.id.results_text_view);

        // Assume we get the total amount and participants from intent
        double totalAmount = getIntent().getDoubleExtra("total_amount", 0);
        HashMap<String, Double> contributions = (HashMap<String, Double>) getIntent().getSerializableExtra("contributions");

        calculateAndDisplayResults(totalAmount, contributions);

        Button shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(v -> shareResults());
    }

    private void calculateAndDisplayResults(double totalAmount, HashMap<String, Double> contributions) {
        // Logic to calculate splits and display results
    }

    private void shareResults() {
        // Logic to share results using Android's sharing intents
    }
}