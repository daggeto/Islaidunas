package com.islaidunas.core.dbxStoreOrm.dao;

import com.islaidunas.core.dbxStoreOrm.EntityManager;
import com.islaidunas.domain.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static com.islaidunas.core.dbxStoreOrm.EntityManager.*;

/**
 * Created by daggreto on 2014.06.11.
 */
public class GenericJpaDao<T, ID> implements GenericDao<T, ID>{

    private EntityManager entityManager;
    private Class entityType;

    public GenericJpaDao(EntityManager entityManager){
        this.entityManager = entityManager;
        this.entityType  = ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    @Override
    public void save(T entity) {
        entityManager.persistEntity(entity);
    }

    @Override
    public void update(T entity) {
        entityManager.persistEntity(entity);
    }

    @Override
    public List<T> findAll() {
        EntityManager.Query q = new EntityManager.Query().forTable(entityType);
        return (List<T>) entityManager.getResultList(q);
    }

    @Override
    public T findById(ID id) {
        return (T) entityManager.findById(entityType, id);
    }

    public void saveOrUpdate(T entity) {
    }

    public void delete(T entity) {
    }
}
