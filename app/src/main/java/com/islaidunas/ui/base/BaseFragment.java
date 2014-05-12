package com.islaidunas.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by daggreto on 2014.05.07.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseFragmentAcitivity)getActivity()).inject(this);
    }

}
