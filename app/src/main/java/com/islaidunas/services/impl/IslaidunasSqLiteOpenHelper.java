package com.islaidunas.services.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;

import com.islaidunas.IslaidunasApp;
import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by daggreto on 2014.05.10.
 */
public class IslaidunasSqLiteOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 11;
    public static final String DATABASE_NAME = "Islaidunas.db";
    private Context context;

    @Inject CategoryJpaDao categoryJpaDao;

    public IslaidunasSqLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        ((IslaidunasApp)context.getApplicationContext()).inject(this);
        try {
            TableUtils.createTableIfNotExists(connectionSource, Transaction.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            initCategoryTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            int status = TableUtils.dropTable(connectionSource, Category.class, true);
            status = TableUtils.dropTable(connectionSource, Transaction.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }

    private void initCategoryTable(){
        categoryJpaDao.save(parseCategory(R.array.bankphone));
        categoryJpaDao.save(parseCategory(R.array.clothes));
        categoryJpaDao.save(parseCategory(R.array.entertainment));
        categoryJpaDao.save(parseCategory(R.array.food));
        categoryJpaDao.save(parseCategory(R.array.healthcare));
        categoryJpaDao.save(parseCategory(R.array.household));
        categoryJpaDao.save(parseCategory(R.array.personal));
        categoryJpaDao.save(parseCategory(R.array.rent));
        categoryJpaDao.save(parseCategory(R.array.transport));
        categoryJpaDao.save(parseCategory(R.array.travel));
    }

    private Category parseCategory(int resource){
        TypedArray categoryItem = context.getResources().obtainTypedArray(resource);
        String code = context.getResources().getResourceName(resource);
        context.getResources().getIdentifier(categoryItem.getString(1), null, null);
        return new Category(code, categoryItem.getString(0), categoryItem.getString(1));
    }
}
