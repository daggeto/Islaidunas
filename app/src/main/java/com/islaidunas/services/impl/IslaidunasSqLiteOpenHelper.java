package com.islaidunas.services.impl;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;

import com.islaidunas.R;
import com.islaidunas.domain.Bucket;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.CategorySign;
import com.islaidunas.domain.Transaction;
import com.islaidunas.services.impl.dao.BucketJpaDao;
import com.islaidunas.services.impl.dao.CategoryJpaDao;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import mortar.Mortar;

public class IslaidunasSqLiteOpenHelper extends OrmLiteSqliteOpenHelper {

    public static final int DATABASE_VERSION = 26;
    public static final String DATABASE_NAME = "Islaidunas.db";
    private Context context;

    @Inject CategoryJpaDao categoryJpaDao;
    @Inject BucketJpaDao bucketJpaDao;

    public IslaidunasSqLiteOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            Mortar.inject(context, this);
            TableUtils.createTableIfNotExists(connectionSource, Transaction.class);
            TableUtils.createTableIfNotExists(connectionSource, Category.class);
            TableUtils.createTableIfNotExists(connectionSource, Bucket.class);
            initBucketTable();
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
            status = TableUtils.dropTable(connectionSource, Bucket.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }

    private void initCategoryTable(){
        categoryJpaDao.save(persistCategory(R.array.bankphone));
        categoryJpaDao.save(persistCategory(R.array.clothes));
        categoryJpaDao.save(persistCategory(R.array.entertainment));
        categoryJpaDao.save(persistCategory(R.array.food));
        categoryJpaDao.save(persistCategory(R.array.healthcare));
        categoryJpaDao.save(persistCategory(R.array.household));
        categoryJpaDao.save(persistCategory(R.array.personal));
        categoryJpaDao.save(persistCategory(R.array.rent));
        categoryJpaDao.save(persistCategory(R.array.transport));
        categoryJpaDao.save(persistCategory(R.array.travel));
        categoryJpaDao.save(persistCategory(R.array.salary));
        categoryJpaDao.save(persistCategory(R.array.extra));
        categoryJpaDao.save(persistCategory(R.array.saved));
    }

    private void initBucketTable(){
        bucketJpaDao.save(persistBucket(R.array.casual));
        bucketJpaDao.save(persistBucket(R.array.savings));
        bucketJpaDao.save(persistBucket(R.array.travels));
    }

    private Category persistCategory(int resource){
        TypedArray categoryItem = context.getResources().obtainTypedArray(resource);
        Category category = new Category();

        String code = context.getResources().getResourceName(resource);
        category.setCode(code);
        category.setTitle(categoryItem.getString(0));

        context.getResources().getIdentifier(categoryItem.getString(1), null, null);

        category.setSrc(categoryItem.getString(1));
        category.setSign(Boolean.parseBoolean(categoryItem.getString(2)));
        category.setBucket(findBucket(categoryItem.getString(3)));

        return category;
    }

    private Bucket findBucket(String code){
        return bucketJpaDao.findBucketByCode(code);
    }

    private Bucket persistBucket(int resource){
        TypedArray bucketItem = context.getResources().obtainTypedArray(resource);

        Bucket bucket = new Bucket();

        String code = context.getResources().getResourceEntryName(resource);
        bucket.setCode(code);

        bucket.setName(bucketItem.getString(0));

        return bucket;
    }
}
