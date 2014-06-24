package com.islaidunas.core.module;

import android.content.Context;
import android.util.Log;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.islaidunas.core.dbxStoreOrm.EntityManager;
import com.islaidunas.core.dbxStoreOrm.dao.GenericJpaDao;
import com.islaidunas.core.dbxStoreOrm.mapper.EntityMapper;
import com.islaidunas.core.ui.MainActivity;
import com.islaidunas.dao.dbx.TransactionJpaDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(library = true, complete = false)
public class DbxModule {

    @Provides
    @Singleton
    DbxAccountManager provideDbxAccountManager(Context context){
        return DbxAccountManager.getInstance(context, "4y1y08z6k86rhgo", "h40y2jfc4fr3bmk");
    }

    @Provides
    DbxAccount provideDbxAccount(DbxAccountManager manager){
        return manager.getLinkedAccount();
    }

    @Provides @Singleton
    DbxDatastore provideDbxDatastore(DbxAccount account){
        try {
            return DbxDatastore.openDefault(account);
        } catch (DbxException e) {
            Log.e("DbxException", e.toString());
            return null;
        }
    }

    @Provides @Singleton
    EntityMapper provideEntityMapper(){
        return new EntityMapper();
    }

    @Provides
    EntityManager provideEntityManager(DbxDatastore store, EntityMapper entityMapper){
        return new EntityManager(store, entityMapper);
    }

    @Provides TransactionJpaDao provideTransactionJpaDao(EntityManager entityManager){
        return new TransactionJpaDao(entityManager);
    }
}
