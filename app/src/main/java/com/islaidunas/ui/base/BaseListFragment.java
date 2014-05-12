package com.islaidunas.ui.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

/**
 * Created by artas on 2014.05.06.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseFragmentAcitivity)getActivity()).inject(this);
    }
}
