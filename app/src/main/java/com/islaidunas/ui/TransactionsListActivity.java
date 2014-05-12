package com.islaidunas.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.islaidunas.ui.base.BaseFragmentAcitivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;


public class TransactionsListActivity extends BaseFragmentAcitivity {

    private IslaidunasSqLiteOpenHelper databaseHelper = null;

    @Override
    protected Fragment createFragment() {
        return TransactionsListFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
