package com.islaidunas.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.islaidunas.R;
import com.islaidunas.adapter.CategorySpinnerAdapter;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dto.TransactionAddViewDto;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.services.impl.dao.TransactionJpaDao;
import com.islaidunas.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by daggreto on 2014.05.07.
 */
public class AddTransactionFragment extends BaseFragment implements AdapterView.OnItemSelectedListener,
        TextWatcher{

    public static String EXTRA_TX_ID = "com.islaidunas.ui.tx_id";

    @Inject CategoryJpaDao categoryJpaDao;
    @Inject TransactionJpaDao transactionJpaDao;

    private TransactionAddViewDto transaction;

    //TODO: Back on action button
    //TODO: Find model view binder
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        int id = getActivity().getIntent().getIntExtra(AddTransactionFragment.EXTRA_TX_ID, -1);
        transaction = getTransaction(id);

    }

    private TransactionAddViewDto getTransaction(int id){
        if(id > -1){
            return new TransactionAddViewDto(transactionJpaDao.findById(id));
        }
        return new TransactionAddViewDto();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        EditText amountView = (EditText) v.findViewById(R.id.amount);
        amountView.setText(transaction.getAmount());
        amountView.addTextChangedListener(this);

        Spinner spinner = (Spinner) v.findViewById(R.id.transaction_category);
        List<Category> categories = categoryJpaDao.findAll();
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this.getActivity(), categories);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(transaction.getCategory()));
        spinner.setOnItemSelectedListener(this);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.transaction_add_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        case R.id.save_transaction :
            transaction.setTitle("Islaida");
            transaction.getTransaction().setDate(new Date());
            transactionJpaDao.saveOrUpdate(transaction.getTransaction());
            Navigator.goToTransactionsList(this.getActivity());
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public static AddTransactionFragment newInstance(){
        return new AddTransactionFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        transaction.setCategory((Category) adapterView.getSelectedItem());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        transaction.setAmount(editable.toString());
    }
}
