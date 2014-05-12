package com.islaidunas.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.islaidunas.R;
import com.islaidunas.adapter.CategorySpinnerAdapter;
import com.islaidunas.domain.Category;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by daggreto on 2014.05.07.
 */
public class AddTransactionFragment extends BaseFragment implements AdapterView.OnItemSelectedListener{

    @Inject CategoryJpaDao categoryJpaDao;

    //TODO: Back on action button
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.transaction_category);
        List<Category> categories = categoryJpaDao.findAll();
        spinner.setAdapter(new CategorySpinnerAdapter(this.getActivity(), categories));
        spinner.setOnItemSelectedListener(this);
        return v;
    }

    public static AddTransactionFragment newInstance(){
        return new AddTransactionFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
