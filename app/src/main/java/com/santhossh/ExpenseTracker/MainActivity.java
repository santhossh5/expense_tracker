package com.santhossh.ExpenseTracker;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseClickListener {

    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private ArrayList<Expense> expenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpenseDatabaseHelper dbHelper = new ExpenseDatabaseHelper(this);
        expenseList = dbHelper.getAllExpenses(); // Load expenses from the database

        recyclerView = findViewById(R.id.recycler_view_expenses);
        expenseAdapter = new ExpenseAdapter(expenseList, this, this);  // Pass 'this' as the listener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(expenseAdapter);

        FloatingActionButton addExpenseButton = findViewById(R.id.add_expense_button);
        addExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            addExpenseLauncher.launch(intent); // Use the ActivityResultLauncher
        });
    }

    // Refresh the list when returning from AddExpenseActivity
    @Override
    protected void onResume() {
        super.onResume();
        expenseList.clear();
        expenseList.addAll(new ExpenseDatabaseHelper(this).getAllExpenses());
        expenseAdapter.notifyDataSetChanged();
    }

    // Define ActivityResultLauncher
    private final ActivityResultLauncher<Intent> addExpenseLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Refresh the expense list when returning from AddExpenseActivity
                    expenseList.clear();
                    expenseList.addAll(new ExpenseDatabaseHelper(this).getAllExpenses());
                    expenseAdapter.notifyDataSetChanged();
                }
            }
    );

    // Implement the onExpenseClick method to handle item clicks
    @Override
    public void onExpenseClick(Expense expense) {
        // When an expense item is clicked, open the ExpenseDetailActivity
        Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
        intent.putExtra("expense", expense);
        startActivity(intent);
    }
}
