package com.islaidunas.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.islaidunas.R;
import com.islaidunas.adapter.TransactionsListAdapter;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.services.impl.dao.TransactionJpaDao;
import com.islaidunas.ui.base.BaseListFragment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by daggreto on 2014.05.06.
 */
public class TransactionsListFragment extends BaseListFragment{

    @Inject TransactionJpaDao transactionJpaDao;

    public static TransactionsListFragment newInstance(){
        return new TransactionsListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.transactions_list);
        List<Transaction> transactionList = null;
        try {
            transactionList = transactionJpaDao.getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setListAdapter(new TransactionsListAdapter(this.getActivity(), transactionList));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_transaction, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_transaction :
               Navigator.goToAddTransaction(this.getActivity());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Transaction createTx(String name, BigDecimal amount, Date date){
        Transaction tx = new Transaction();
        tx.setTitle(name);
        tx.setAmount(amount);
        tx.setDate(date);
        //transactionJpaDao.save(tx);
        return tx;
    }

}
