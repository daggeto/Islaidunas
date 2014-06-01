package com.islaidunas;

import android.app.Application;

import com.islaidunas.core.module.ApplicationModule;

import dagger.ObjectGraph;
import mortar.Mortar;
import mortar.MortarScope;

/**
 * @author artas
 */
public class IslaidunasApplication extends Application {
    private MortarScope rootScope;

    @Override
    public void onCreate() {
        super.onCreate();
        rootScope = Mortar.createRootScope(BuildConfig.DEBUG, ObjectGraph.create(new ApplicationModule(this.getApplicationContext())));
    }

    @Override public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return rootScope;
        }
        return super.getSystemService(name);
    }

}
