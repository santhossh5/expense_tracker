// ExpenseDatabaseHelper.java
package com.santhossh.ExpenseTracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 1;

    // Tables and columns
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_PARTICIPANTS = "participants";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_PAYER = "payer";

    private static final String COLUMN_EXPENSE_ID = "expense_id"; // Foreign key for participants table
    private static final String COLUMN_PARTICIPANT_NAME = "participant_name";
    private static final String COLUMN_PARTICIPANT_SHARE = "participant_share";

    public ExpenseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create expenses table
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_PAYER + " TEXT" + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);

        // Create participants table
        String CREATE_PARTICIPANTS_TABLE = "CREATE TABLE " + TABLE_PARTICIPANTS + "("
                + COLUMN_EXPENSE_ID + " INTEGER,"
                + COLUMN_PARTICIPANT_NAME + " TEXT,"
                + COLUMN_PARTICIPANT_SHARE + " REAL,"
                + "FOREIGN KEY(" + COLUMN_EXPENSE_ID + ") REFERENCES " + TABLE_EXPENSES + "(" + COLUMN_ID + "))";
        db.execSQL(CREATE_PARTICIPANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTS);
        onCreate(db);
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert into expenses table
        ContentValues expenseValues = new ContentValues();
        expenseValues.put(COLUMN_TITLE, expense.getTitle());
        expenseValues.put(COLUMN_AMOUNT, expense.getAmount());
        expenseValues.put(COLUMN_PAYER, expense.getPayer());
        long expenseId = db.insert(TABLE_EXPENSES, null, expenseValues);

        // Insert each participant's share into the participants table
        for (String participant : expense.getParticipantShares().keySet()) {
            ContentValues participantValues = new ContentValues();
            participantValues.put(COLUMN_EXPENSE_ID, expenseId);
            participantValues.put(COLUMN_PARTICIPANT_NAME, participant);
            participantValues.put(COLUMN_PARTICIPANT_SHARE, expense.getParticipantShares().get(participant));
            db.insert(TABLE_PARTICIPANTS, null, participantValues);
        }

        db.close();
    }

    // Method to retrieve all expenses and participants
    public ArrayList<Expense> getAllExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve expenses
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXPENSES, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                String payer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAYER));

                // Query to retrieve participants for this expense
                Cursor participantCursor = db.rawQuery("SELECT * FROM " + TABLE_PARTICIPANTS + " WHERE " + COLUMN_EXPENSE_ID + " = ?", new String[]{String.valueOf(id)});
                HashMap<String, Double> participantShares = new HashMap<>();
                if (participantCursor.moveToFirst()) {
                    do {
                        String participantName = participantCursor.getString(participantCursor.getColumnIndexOrThrow(COLUMN_PARTICIPANT_NAME));
                        double participantShare = participantCursor.getDouble(participantCursor.getColumnIndexOrThrow(COLUMN_PARTICIPANT_SHARE));
                        participantShares.put(participantName, participantShare);
                    } while (participantCursor.moveToNext());
                }
                participantCursor.close();

                // Create Expense object
                expenses.add(new Expense(id, title, amount, payer, participantShares));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }
}
