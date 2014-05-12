package com.islaidunas.services.impl.dao;

import android.content.Context;

import com.islaidunas.IslaidunasApp;
import com.islaidunas.services.impl.IslaidunasSqLiteOpenHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by daggreto on 2014.05.11.
 */
abstract class GenericDao<T, K>{
    @Inject IslaidunasSqLiteOpenHelper databaseHelper;

    private Dao<T, K> dao;

    abstract Dao<T,K> createDao() throws SQLException;

    public GenericDao(Context context){
        ((IslaidunasApp)context.getApplicationContext()).getGraph().inject(this);
        try {
            dao = createDao();
        } catch (SQLException e) {
            //TODO: optimise
            e.printStackTrace();
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

    public IslaidunasSqLiteOpenHelper getDatabaseHelper(){
        return databaseHelper;
    }

}
