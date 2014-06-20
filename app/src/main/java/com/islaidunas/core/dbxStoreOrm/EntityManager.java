package com.islaidunas.core.dbxStoreOrm;

import android.util.Log;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Class for managing dropbox entities
 */
public class EntityManager {
    private DbxDatastore store;

    public EntityManager(DbxDatastore store){
        this.store = store;
    }

    public Query createQuery(){
        return new Query();
    }

    public List<Object> getResultList(Query query){
        List<Object> result = new ArrayList<Object>();
        try {
            DbxTable table = store.getTable(query.getTable().getSimpleName());

            if(!table.query().hasResults()){
                return  result;
            }

            List<DbxRecord> records = table.query(query.getCriteries()).asList();


        } catch (DbxException e) {
            Log.e("DbxException", e.toString());
            return result;
        }
        return result;
    }

    public class Query{
        //TODO: refactor to QueryBuilder
        private Class table;
        private DbxFields criteries;


        public Query forTable(Class table) {
            this.table = table;
            return this;
        }

        public Query withCriterie(String name, String value){
            criteries.set(name, value);
            return this;
        }

        public Class getTable() {
            return table;
        }

        public DbxFields getCriteries() {
            return criteries;
        }
    }

}
