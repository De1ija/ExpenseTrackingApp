package com.example.android.expensetrackingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.expensetrackingapp.data.ExpenseContract.ExpenseEntry;
/**
 * Created by Bane on 4/5/2018.
 */

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    public ExpensesAdapter (Context context){
        mContext = context;
    }
    @Override
    public ExpensesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new ExpensesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpensesAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        int id = mCursor.getInt(mCursor.getColumnIndex(ExpenseEntry._ID));
        holder.itemView.setTag(id);

        int amountI = mCursor.getInt(mCursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_AMOUNT));
        String amount = Integer.toString(amountI);
        String category = mCursor.getString(mCursor.getColumnIndex(ExpenseEntry.COLUMN_EXPENSE_CATEGORY));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(ExpenseEntry.COLUMN_TIMESTAMP));

        holder.amountView.setText(amount);
        holder.categoryView.setText(category);
        holder.timestampView.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    class ExpensesAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView amountView;
        TextView categoryView;
        TextView timestampView;

        public ExpensesAdapterViewHolder(View view) {
            super(view);

            amountView = view.findViewById(R.id.amount_tv);
            categoryView = view.findViewById(R.id.category_tv);
            timestampView = view.findViewById(R.id.timestamp_tv);
        }
    }
}
