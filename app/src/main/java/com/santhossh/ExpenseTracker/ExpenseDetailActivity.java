package com.santhossh.ExpenseTracker;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ExpenseDetailActivity extends AppCompatActivity {

    private TextView titleTextView, amountTextView, payerTextView;
    private LinearLayout participantDetailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        titleTextView = findViewById(R.id.detail_title);
        amountTextView = findViewById(R.id.detail_amount);
        payerTextView = findViewById(R.id.detail_payer);
        participantDetailsLayout = findViewById(R.id.participant_details_layout);

        // Get the Expense object from the Intent
        Expense expense = (Expense) getIntent().getSerializableExtra("expense");

        // Display the details in the UI
        if (expense != null) {
            titleTextView.setText(expense.getTitle());
            amountTextView.setText(String.valueOf(expense.getAmount()));
            payerTextView.setText("Payer: " + expense.getPayer());

            // Display participant details
            for (String participant : expense.getParticipantShares().keySet()) {
                double share = expense.getParticipantShares().get(participant);
                TextView participantTextView = new TextView(this);
                participantTextView.setText(participant + ": " + share);
                participantDetailsLayout.addView(participantTextView);
            }
        }
    }
}
