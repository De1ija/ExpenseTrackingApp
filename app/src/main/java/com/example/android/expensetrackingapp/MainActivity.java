package com.example.android.expensetrackingapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.expensetrackingapp.data.ExpenseContract;
import com.example.android.expensetrackingapp.data.ExpenseContract.ExpenseEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXPENSES_LOADER = 1;

    ExpensesAdapter mExpensesAdapter;
    RecyclerView mExpensesRecyclerView;

    private EditText mEnterAmount;
    private EditText mEnterCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mExpensesRecyclerView = findViewById(R.id.expenses_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mExpensesRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mExpensesRecyclerView.getContext(),
                layoutManager.getOrientation());
        mExpensesRecyclerView.addItemDecoration(dividerItemDecoration);

        mExpensesAdapter = new ExpensesAdapter(this);
        mExpensesRecyclerView.setAdapter(mExpensesAdapter);


        FloatingActionButton fab = findViewById(R.id.expense_insert_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewExpenseDialog();
            }
        });

        getLoaderManager().initLoader(EXPENSES_LOADER, null, this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                String stringId = Integer.toString(id);
                Uri uri = ExpenseContract.ExpenseEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                final Uri dialogUri = uri;

                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Warning");
                alertDialog.setMessage("About to delete expense!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getContentResolver().delete(dialogUri, null, null);
                        getLoaderManager().restartLoader(EXPENSES_LOADER, null, MainActivity.this);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Keep expense", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getLoaderManager().restartLoader(EXPENSES_LOADER, null, MainActivity.this);
                    }
                });
                alertDialog.show();
            }
        }).attachToRecyclerView(mExpensesRecyclerView);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ExpenseContract.ExpenseEntry.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mExpensesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mExpensesAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_all_expenses:
                deleteAllExpenses();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllExpenses(){
        int rowsDeleted = getContentResolver().delete(ExpenseEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from pet database");
    }

    private void openNewExpenseDialog(){
        final AlertDialog enterExpenseDialog = new AlertDialog.Builder(MainActivity.this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_expense, null);
        mEnterAmount = dialogView.findViewById(R.id.enter_amount_et);
        mEnterCategory = dialogView.findViewById(R.id.enter_category_et);

        Button enterExpense = dialogView.findViewById(R.id.save_expense_button);
        enterExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExpanse();
                enterExpenseDialog.dismiss();
            }
        });
        enterExpenseDialog.setView(dialogView);
        enterExpenseDialog.setTitle(R.string.expense_dialog_title);
        enterExpenseDialog.show();

        mEnterAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEnterAmount.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(mEnterAmount, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        mEnterAmount.requestFocus();
    }

    private void saveExpanse(){

        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MMM.yyyy", Locale.getDefault());
        Date date = new Date();
        String timestamp = formatDate.format(date);

        int amount;
        String category;
        if(mEnterAmount.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_LONG).show();
            return;
        }else{
            String amountString = mEnterAmount.getText().toString();
            amount = Integer.parseInt(amountString);
        }

        if(mEnterCategory.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter category", Toast.LENGTH_LONG).show();
        }else {
            category = mEnterCategory.getText().toString();

            ContentValues cv = new ContentValues();
            cv.put(ExpenseEntry.COLUMN_EXPENSE_AMOUNT, amount);
            cv.put(ExpenseEntry.COLUMN_EXPENSE_CATEGORY, category);
            cv.put(ExpenseEntry.COLUMN_TIMESTAMP, timestamp);

            Uri newUri = getContentResolver().insert(ExpenseEntry.CONTENT_URI, cv);
            if(newUri == null){
                Toast.makeText(this, "Error entering expense", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Expense saved", Toast.LENGTH_LONG).show();
            }
        }
    }
}
