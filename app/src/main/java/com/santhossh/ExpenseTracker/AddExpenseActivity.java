package com.santhossh.ExpenseTracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {

    private EditText titleInput, amountInput;
    private Spinner payerSpinner;
    private LinearLayout participantSharesLayout;
    private ArrayList<EditText> participantInputs = new ArrayList<>();
    private ArrayList<String> participantNames = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        titleInput = findViewById(R.id.expense_title);
        amountInput = findViewById(R.id.expense_amount);
        payerSpinner = findViewById(R.id.payer_spinner);
        participantSharesLayout = findViewById(R.id.participant_shares_layout);
        Button addParticipantButton = findViewById(R.id.add_participant_button);

        // Initialize spinner adapter
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, participantNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payerSpinner.setAdapter(spinnerAdapter);

        addParticipantButton.setOnClickListener(v -> addParticipantField());
    }

    private void addParticipantField() {
        // Prompt to enter the name of the new participant
        EditText nameInput = new EditText(this);
        nameInput.setHint("Enter participant name");
        participantSharesLayout.addView(nameInput);

        Button confirmAddButton = new Button(this);
        confirmAddButton.setText("Confirm");
        participantSharesLayout.addView(confirmAddButton);

        confirmAddButton.setOnClickListener(view -> {
            String participantName = nameInput.getText().toString().trim();
            if (!participantName.isEmpty()) {
                participantNames.add(participantName);
                spinnerAdapter.notifyDataSetChanged(); // Update spinner

                // Add a field for the participant's share
                EditText participantShareInput = new EditText(this);
                participantShareInput.setHint("Enter share for " + participantName);
                participantSharesLayout.addView(participantShareInput);
                participantInputs.add(participantShareInput);

                // Remove name input and confirm button after adding
                participantSharesLayout.removeView(nameInput);
                participantSharesLayout.removeView(confirmAddButton);
            } else {
                Toast.makeText(this, "Participant name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveExpense(View view) {
        String title = titleInput.getText().toString().trim();
        double amount;
        try {
            amount = Double.parseDouble(amountInput.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Double> participantShares = new HashMap<>();
        for (int i = 0; i < participantInputs.size(); i++) {
            String participantName = participantNames.get(i);
            double share;
            try {
                share = Double.parseDouble(participantInputs.get(i).getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid share amount", Toast.LENGTH_SHORT).show();
                return;
            }
            participantShares.put(participantName, share);
        }

        String payer = payerSpinner.getSelectedItem().toString();

        Expense expense = new Expense(title, amount, payer, participantShares);
        new ExpenseDatabaseHelper(this).addExpense(expense);

        setResult(RESULT_OK);
        finish();
    }
}
