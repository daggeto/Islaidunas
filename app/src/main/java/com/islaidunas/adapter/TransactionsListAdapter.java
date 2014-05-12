package com.islaidunas.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.islaidunas.R;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dto.TransactionViewDto;

import java.util.List;

/**
 * Created by artas on 2014.05.06.
 */
public class TransactionsListAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    List<Transaction> transactions;

    public TransactionsListAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = ((FragmentActivity)context).getLayoutInflater().inflate(R.layout.list_item_transaction, null);
        }

        TransactionViewDto txDto = new TransactionViewDto(getItem(position));
        txDto.persistToView(convertView);

        return convertView;
    }

    @Override
    public int getCount() {
        return transactions.size();
    }
}
