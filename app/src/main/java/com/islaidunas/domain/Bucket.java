package com.islaidunas.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Entity represents financial "Bucket". Container where money ar distributed.
 */
@DatabaseTable(tableName = "bucket")
public class Bucket {

    @DatabaseField( generatedId = true)
    private int id;

    @DatabaseField
    private String code;

    @DatabaseField
    private String name;

    public Bucket(){

    }

    public Bucket(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Bucket)){
           return false;
        }

        Bucket target = (Bucket) o;
        return code.equals(target.getCode());
    }
}
