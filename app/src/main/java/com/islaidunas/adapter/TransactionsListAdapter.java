package com.islaidunas.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dto.TransactionListItemViewDto;

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

        TransactionListItemViewDto txDto = new TransactionListItemViewDto(getItem(position));
        persistToView(convertView, txDto);

        return convertView;
    }

    protected void persistToView(View view, TransactionListItemViewDto transaction){
        ImageView imageView = (ImageView) view.findViewById(R.id.transaction_category);
        imageView.setImageDrawable(getDrawable(transaction.getCategory()));

        TextView transactionName = (TextView) view.findViewById(R.id.transaction_name);
        transactionName.setText(transaction.getTitle());

        TextView transactionCategory = (TextView) view.findViewById(R.id.transaction_date);
        transactionCategory.setText(transaction.getCategory().getTitle());

        TextView transactionAmount = (TextView) view.findViewById(R.id.transaction_amount);
        transactionAmount.setText(transaction.getAmount());
    }

    private Drawable getDrawable(Category item){
        int resourceId = context.getResources().getIdentifier(item.getSrc(), "drawable", context.getPackageName());

        if(resourceId == 0){
            resourceId = context.getResources().getIdentifier("def", "drawable", context.getPackageName());
        }

        return context.getResources().getDrawable(resourceId);
    }


    @Override
    public int getCount() {
        return transactions.size();
    }
}
