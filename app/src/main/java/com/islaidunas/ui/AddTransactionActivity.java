package com.islaidunas.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.islaidunas.R;
import com.islaidunas.ui.base.BaseFragmentAcitivity;

public class AddTransactionActivity extends BaseFragmentAcitivity {

    @Override
    protected Fragment createFragment() {
        return AddTransactionFragment.newInstance();
    }

}
