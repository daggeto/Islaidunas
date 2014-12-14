package com.islaidunas.ui.view.listener;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by daggreto on 2014.08.16.
 */
public class CategorySelectListener implements AdapterView.OnItemClickListener {

    private View currentView;

    public CategorySelectListener() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        currentView = view;
    }

}
