package com.islaidunas.services.impl.dao;

import android.content.Context;

import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daggreto on 2014.05.11.
 */
abstract class GenericJpaDao<T, K>{
   private IslaidunasSqLiteOpenHelper databaseHelper;

    private Dao<T, K> dao;

    abstract Dao<T,K> createDao() throws SQLException;
    public GenericJpaDao(Context context){
        try {
            databaseHelper = OpenHelperManager.getHelper(context, IslaidunasSqLiteOpenHelper.class);
            dao = createDao();
        } catch (SQLException e) {
            //TODO: optimise
            e.printStackTrace();
        }
    }

    public Dao.CreateOrUpdateStatus saveOrUpdate(T entity){
        try {
            return getDao().createOrUpdate(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Dao.CreateOrUpdateStatus(false, false, 0);
    }

    public int delete(T entity){
        try{
            return getDao().delete(entity);
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public int save(T entity){
        try {
            return dao.create(entity);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Dao<T, K> getDao(){
        return dao;
    }

    public List<T> findAll(){
        try {
            return getDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public T findById(K id){
        try {
            return getDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public IslaidunasSqLiteOpenHelper getDatabaseHelper(){
        return databaseHelper;
    }

}
