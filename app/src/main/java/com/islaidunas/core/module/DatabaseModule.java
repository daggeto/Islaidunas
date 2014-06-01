package com.islaidunas.core.module;

import android.content.Context;

import com.islaidunas.core.ui.MainActivity;
import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.services.impl.dao.TransactionJpaDao;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(injects = {IslaidunasSqLiteOpenHelper.class}, library = true, complete = false)
public class DatabaseModule {

    @Provides TransactionJpaDao privedeTransactionJpaDao(Context context) {
        return new TransactionJpaDao(context);
    }

    @Provides CategoryJpaDao provideCategoryJpaDao(Context context) {
        return new CategoryJpaDao(context);
    }
}
