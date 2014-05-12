package com.islaidunas.services.impl.dao;

import android.content.Context;

import com.islaidunas.domain.Category;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by daggreto on 2014.05.11.
 */
public class CategoryJpaDao extends GenericDao<Category, String> {

    public CategoryJpaDao(Context context) {
        super(context);
    }

    @Override
    Dao<Category, String> createDao() throws SQLException {
        return getDatabaseHelper().getDao(Category.class);
    }
}
