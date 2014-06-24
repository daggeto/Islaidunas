package com.islaidunas.dao;

import android.content.Context;

import com.islaidunas.domain.Transaction;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by daggreto on 2014.05.11.
 */
public class TransactionJpaDao extends GenericJpaDao<Transaction, Integer> {

    public TransactionJpaDao(Context context){
        super(context);
    }

    @Override
    Dao<Transaction, Integer> createDao() throws SQLException {
            return getDatabaseHelper().getDao(Transaction.class);
    }

}
