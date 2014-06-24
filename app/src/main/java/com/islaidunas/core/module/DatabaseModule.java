package com.islaidunas.core.module;

import android.content.Context;

import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.islaidunas.dao.CategoryJpaDao;
import com.islaidunas.dao.TransactionJpaDao;

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
