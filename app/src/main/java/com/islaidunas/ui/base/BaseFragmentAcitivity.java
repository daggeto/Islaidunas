package com.islaidunas.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.islaidunas.IslaidunasApp;
import com.islaidunas.R;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by artas on 2014.05.06.
 */
public abstract class BaseFragmentAcitivity extends FragmentActivity{

    private ObjectGraph graph;
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGraph();

        setContentView(R.layout.activity_base_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
    }

    private void initGraph(){
        graph = ((IslaidunasApp) getApplication()).getGraph().plus(getModules().toArray());
    }

    @Override protected void onDestroy() {
        graph = null;
        super.onDestroy();
    }


    protected List<Object> getModules() {
        return Arrays.<Object>asList();
    }

    public void inject(Object object) {
        graph.inject(object);
    }

}
