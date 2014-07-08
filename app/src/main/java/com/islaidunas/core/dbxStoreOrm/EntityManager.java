package com.islaidunas.core.dbxStoreOrm;

import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.islaidunas.core.dbxStoreOrm.enums.RelationType;
import com.islaidunas.core.dbxStoreOrm.mapper.DbxComplexEntityNode;
import com.islaidunas.core.dbxStoreOrm.mapper.EntityMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing dropbox entities
 */
public class EntityManager {
    public static final String TAG = "DbxException";
    private DbxDatastore store;
    private EntityMapper entityMapper;

    public EntityManager(DbxDatastore store, EntityMapper entityMapper){
        this.entityMapper = entityMapper;
        this.store = store;
    }

    public Query createQuery(){
        return new Query();
    }

    public boolean persistEntity(Object entity){
        try {
            String id = entityMapper.getId(entity);
            DbxTable table = store.getTable(entity.getClass().getSimpleName());
            DbxRecord record = table.get(id);

            if(record != null){
                record.deleteRecord();
            }

            DbxComplexEntityNode fields = entityMapper.parseEntity(entity);

            insertComplexEntity(fields, null);

            store.sync();

            return true;

        }  catch (Exception e) {
            Logger.error(TAG, e.toString());
            return false;
        }


    }

    private void insertComplexEntity(DbxComplexEntityNode node, String parentId){
        DbxTable table = store.getTable(node.getTableName());

        DbxRecord record = table.insert(node.getFields());

        String nodeId = record.getId();

        if(!node.hasChilds()){
            return;
        }

        for(DbxComplexEntityNode child : node.getChilds()){
            insertComplexEntity(child, nodeId);
        }

    }

    public Object findById(Class entityType, Object id){
        try{
            DbxTable table = store.getTable(entityType.getSimpleName());
            DbxRecord record = table.get((String) id);
            if(record == null){
                return null;
            }
            return entityMapper.getInstanceOf(entityType, record);
        } catch (Exception e) {
            Logger.error(TAG, e.toString());
            return new ArrayList<Object>();
        }

    }

    public List<Object> getResultList(Query query){
        try {
            List<Object> result = new ArrayList<Object>();
            DbxTable table = store.getTable(query.getTableName());

            if(!table.query().hasResults()){
                return  result;
            }

            List<DbxRecord> records = table.query(query.getCriteries()).asList();
            for(DbxRecord record : records){
                result.add(entityMapper.getInstanceOf(query.getTableClass(), record));
            }

            return result;
        } catch (Exception e) {
            Logger.error(TAG, e.toString());
            return new ArrayList<Object>();
        }

    }

    public DbxTableRelationStrategy resolveRealtionStrategy(RelationType type){
        if(RelationType.manyToMany.equals(type)) return new ManyToOneStrategy();

        return new NoRelationStrategy();
    }

    public static class Query{
        //TODO: refactor to QueryBuilder
        private Class table;
        private DbxFields criteries = new DbxFields();


        public Query forTable(Class table) {
            this.table = table;
            return this;
        }

        public Query withCriterie(String name, String value){
            criteries.set(name, value);
            return this;
        }

        public String getTableName() {
            return table.getSimpleName();
        }
        public Class getTableClass() {
            return table;
        }

        public DbxFields getCriteries() {
            return criteries;
        }
    }

    public class ManyToOneStrategy implements DbxTableRelationStrategy{

        @Override
        public void insertEntity(DbxComplexEntityNode entity, String parentId, DbxDatastore store) {
            //TODO: create first strategy, transaction -> category. In what sequance execute
        }
    }

    public class NoRelationStrategy implements DbxTableRelationStrategy{

        @Override
        public void insertEntity(DbxComplexEntityNode entity, String parentId, DbxDatastore store) {

        }
    }

}
