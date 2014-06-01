package com.islaidunas.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.islaidunas.adapter.TransactionsListAdapter;
import com.islaidunas.domain.Transaction;
import com.islaidunas.screen.TransactionListScreen;

import javax.inject.Inject;

import mortar.Mortar;

/**
 * Created by daggreto on 2014.05.13.
 */
public class TransactionListView extends ListView {

    @Inject TransactionListScreen.Presenter presenter;

    public TransactionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Mortar.inject(context, this);

        setAdapter(new TransactionsListAdapter(context, presenter.getTransactions()));

        setOnItemClickListener(new OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.goToTransaction(getTransactionId(getAdapter().getItem(position)));
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        presenter.takeView(this);
    }

    private Integer getTransactionId(Object tranasaction){
        return ((Transaction)tranasaction).getId();
    }
}
