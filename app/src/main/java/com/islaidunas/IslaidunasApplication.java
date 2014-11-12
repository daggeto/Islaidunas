package com.islaidunas;

import android.app.Application;
import android.content.Context;

import com.islaidunas.core.module.ApplicationModule;

import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;

/**
 * @author artas
 */
public class IslaidunasApplication extends Application {
    private MortarScope rootScope;
    public static Context context;
    public static Context activityContext;

    @Override
    public void onCreate() {
        super.onCreate();
        IslaidunasApplication.context = getApplicationContext();
        rootScope = Mortar.createRootScope(BuildConfig.DEBUG, ObjectGraph.create(new ApplicationModule(this.getApplicationContext())));
    }

    @Override public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return rootScope;
        }
        return super.getSystemService(name);
    }

    public static Context getContext(){
        return IslaidunasApplication.context;
    }
    public static Context getActivityContext(){
        return activityContext;
    }
}
