package com.islaidunas.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.islaidunas.R;
import com.islaidunas.domain.Transaction;
import com.islaidunas.ui.view.TransactionListItemView;

import java.util.List;

/**
 * Created by artas on 2014.05.06.
 */
public class TransactionsListAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    List<Transaction> transactions;

    public TransactionsListAdapter(Context context, List<Transaction> transactions) {
        super(context, R.layout.list_item_transaction, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = new TransactionListItemView(context, getItem(position));
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }
}
