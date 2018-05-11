package com.example.android.expensetrackingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.expensetrackingapp.data.ExpenseContract.ExpenseEntry;

/**
 * Created by Bane on 4/1/2018.
 */

public class ExpenseDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ExpenseDbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "account.db";

    public static final int DATABASE_VERSION = 1;

    public ExpenseDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_EXPENSE_TABLE = "CREATE TABLE " + ExpenseEntry.TABLE_NAME + " ("
                + ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExpenseEntry.COLUMN_EXPENSE_AMOUNT + " INTEGER NOT NULL, "
                + ExpenseEntry.COLUMN_EXPENSE_CATEGORY + " TEXT NOT NULL, "
                + ExpenseEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL );";

        sqLiteDatabase.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
