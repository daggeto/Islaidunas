package com.islaidunas.core.dbxStoreOrm.dao;

import java.util.List;

/**
 * Created by daggreto on 2014.06.11.
 */
interface GenericDao<T, ID> {

    void save(T entity);
    void update(T entity);
    List<T> findAll();
    T findById(ID id);
}
