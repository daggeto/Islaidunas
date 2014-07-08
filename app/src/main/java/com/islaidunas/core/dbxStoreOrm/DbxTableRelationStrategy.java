package com.islaidunas.core.dbxStoreOrm;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxRecord;
import com.islaidunas.core.dbxStoreOrm.mapper.DbxComplexEntityNode;

/**
 * Created by daggreto on 2014.06.29.
 */
public interface DbxTableRelationStrategy {

    void insertEntity(DbxComplexEntityNode entity, String parentId, DbxDatastore store);
}
