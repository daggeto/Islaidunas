package com.islaidunas.core.dbxStoreOrm.dao;

import com.islaidunas.core.dbxStoreOrm.EntityManager;

import java.util.List;

/**
 * Created by daggreto on 2014.06.11.
 */
public class GenericJpaDao<T, ID> implements GenericDao<T, ID>{

    EntityManager entityManager;

    public GenericJpaDao(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void save(T entity) {

    }

    @Override
    public void update(T entity) {

    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public T findById(ID id) {
        return null;
    }
}
