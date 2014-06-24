package com.islaidunas.dao.dbx;

import com.islaidunas.core.dbxStoreOrm.EntityManager;
import com.islaidunas.core.dbxStoreOrm.dao.GenericJpaDao;
import com.islaidunas.domain.Transaction;

/**
 * Created by daggreto on 2014.06.16.
 */
public class TransactionJpaDao extends GenericJpaDao<Transaction, String> {

    public TransactionJpaDao(EntityManager entityManager) {
        super(entityManager);
    }
}
