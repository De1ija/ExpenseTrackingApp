package com.example.android.expensetrackingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bane on 4/1/2018.
 */

public class ExpenseContract {

    private ExpenseContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.expensetrackingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXPENSES = "expenses";

    public static final class ExpenseEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPENSES);

        public static final String TABLE_NAME = "expenses";

       /* public static final String _ID = BaseColumns._ID;*/

       public static final String COLUMN_EXPENSE_AMOUNT = "amount";
       public static final String COLUMN_EXPENSE_CATEGORY = "category";
       public static final String COLUMN_TIMESTAMP = "timedate";


    }
}
