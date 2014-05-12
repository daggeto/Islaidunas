package com.islaidunas.module;

import android.content.Context;

import com.islaidunas.IslaidunasApp;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.islaidunas.services.impl.dao.TransactionJpaDao;
import com.islaidunas.ui.AddTransactionFragment;
import com.islaidunas.ui.TransactionsListFragment;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daggreto on 2014.05.11.
 */
//TODO: find way how to load all classes with @inject annotation
@Module(
        injects = {
                IslaidunasApp.class,
                TransactionsListFragment.class,
                TransactionJpaDao.class,
                CategoryJpaDao.class,
                IslaidunasSqLiteOpenHelper.class,
                AddTransactionFragment.class
        }
)
public class IslaidunasModule {
    private Context context;

    public IslaidunasModule(Context context){
        this.context = context;
    }

    @Provides @Singleton IslaidunasSqLiteOpenHelper provideIslaidunasSqLiteOpenHelper(){
        return OpenHelperManager.getHelper(context, IslaidunasSqLiteOpenHelper.class);
    }

    @Provides @Singleton TransactionJpaDao privedeTransactionJpaDao(){
            return new TransactionJpaDao(context);
    }

    @Provides @Singleton CategoryJpaDao provideCategoryJpaDao(){
        return new CategoryJpaDao(context);
    }

}
