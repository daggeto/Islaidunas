package com.islaidunas.services.impl.dao;

import android.content.Context;

import com.islaidunas.domain.Transaction;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by daggreto on 2014.05.11.
 */
public class TransactionJpaDao extends GenericDao<Transaction, Integer>{

    public TransactionJpaDao(Context context){
        super(context);
    }

    @Override
    Dao<Transaction, Integer> createDao() throws SQLException {
            return getDatabaseHelper().getDao(Transaction.class);
    }

}
