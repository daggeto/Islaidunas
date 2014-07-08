package com.islaidunas.core.dbxStoreOrm.mapper;

import com.dropbox.sync.android.DbxFields;
import com.islaidunas.core.dbxStoreOrm.enums.RelationType;

import java.util.List;

/**
 * Created by daggreto on 2014.06.24.
 */
public class DbxComplexEntityNode {

    private List<DbxComplexEntityNode> childs;
    private DbxFields fields;
    private Class entityType;
    private RelationType relationType;

    public List<DbxComplexEntityNode> getChilds() {
        return childs;
    }

    public void setChilds(List<DbxComplexEntityNode> childs) {
        this.childs = childs;
    }

    public DbxFields getFields() {
        return fields;
    }

    public void setFields(DbxFields fields) {
        this.fields = fields;
    }

    public void addChild(DbxComplexEntityNode child){
        childs.add(child);
    }

    public void setEntityType(Class entityType) {
        this.entityType = entityType;
    }

    public String getTableName(){
        return entityType.getSimpleName();
    }

    public boolean hasChilds(){
        return !childs.isEmpty();
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }
}
