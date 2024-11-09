package com.santhossh.ExpenseTracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private OnExpenseClickListener onExpenseClickListener;

    // Constructor now takes both the expense list and the click listener
    public ExpenseAdapter(List<Expense> expenseList, OnExpenseClickListener onExpenseClickListener, Context context) {
        this.expenseList = expenseList;
        this.onExpenseClickListener = onExpenseClickListener;
        this.context = context;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        // Get the expense data for the current position
        Expense expense = expenseList.get(position);

        // Set the title and amount
        holder.titleTextView.setText(expense.getTitle());
        holder.amountTextView.setText(String.format("$%.2f", expense.getAmount()));
        holder.participantCountTextView.setText("Participants: " + expense.getParticipantShares().size());

        // Handle item click to show details
        holder.itemView.setOnClickListener(v -> {
            // Use the listener to pass the clicked expense to the activity
            onExpenseClickListener.onExpenseClick(expense);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder class to hold the views for each expense item
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView amountTextView;
        public TextView participantCountTextView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);

            // Initialize views
            titleTextView = itemView.findViewById(R.id.expense_title);
            amountTextView = itemView.findViewById(R.id.expense_amount);
            participantCountTextView = itemView.findViewById(R.id.expense_participants);
        }
    }

    // Interface to handle item clicks
    public interface OnExpenseClickListener {
        void onExpenseClick(Expense expense);
    }
}
