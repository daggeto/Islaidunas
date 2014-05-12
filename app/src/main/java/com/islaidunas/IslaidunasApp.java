package com.islaidunas;

import android.app.Application;

import com.islaidunas.module.IslaidunasModule;
import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * @author artas
 */
public class IslaidunasApp extends Application {
    private ObjectGraph graph;
    @Inject IslaidunasSqLiteOpenHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        graph = ObjectGraph.create(getModules().toArray());
        inject(this);
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public List<Object> getModules(){
        return Arrays.<Object>asList(new IslaidunasModule(this.getApplicationContext()));
    }

    public ObjectGraph getGraph(){return graph;}

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
