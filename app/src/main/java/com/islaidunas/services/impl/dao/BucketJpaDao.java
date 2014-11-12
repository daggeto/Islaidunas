package com.islaidunas.services.impl.dao;

import android.content.Context;
import android.widget.Toast;

import com.islaidunas.domain.Bucket;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

import java.sql.SQLException;

public class BucketJpaDao extends GenericJpaDao<Bucket, Integer> {
    public BucketJpaDao(Context context) {
        super(context);
    }

    @Override
    Dao<Bucket, Integer> createDao() throws SQLException {
        return getDatabaseHelper().getDao(Bucket.class);
    }

    public Bucket findBucketByCode(String code){
        try {
            PreparedQuery<Bucket> query =
                    getDao().queryBuilder()
                            .where().eq("code", code)
                            .prepare();

            return getDao().queryForFirst(query);
        } catch (SQLException e) {
            return null;
        }
    }
}
