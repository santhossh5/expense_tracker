package com.santhossh.ExpenseTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class AddExpenseActivity extends Activity {

    private EditText titleInput, amountInput;
    private LinearLayout participantsLayout;
    private ArrayList<EditText> participantInputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        titleInput = findViewById(R.id.expense_title);
        amountInput = findViewById(R.id.total_amount);
        participantsLayout = findViewById(R.id.participants_layout);
        participantInputs = new ArrayList<>();

        Button addParticipantButton = findViewById(R.id.add_participant_button);
        addParticipantButton.setOnClickListener(v -> addParticipantField());

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveExpense());
    }

    private void addParticipantField() {
        EditText participantInput = new EditText(this);
        participantInputs.add(participantInput);
        participantsLayout.addView(participantInput);
    }

    private void saveExpense() {
        String title = titleInput.getText().toString();
        double amount = Double.parseDouble(amountInput.getText().toString());

        ArrayList<String> participants = new ArrayList<>();
        for (EditText input : participantInputs) {
            participants.add(input.getText().toString());
        }

        Expense expense = new Expense(title, amount, participants);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("expense", expense);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}