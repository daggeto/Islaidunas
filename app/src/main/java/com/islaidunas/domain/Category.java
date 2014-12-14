package com.islaidunas.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Created by daggreto on 2014.05.07.
 */

@DatabaseTable(tableName = "category")
public class Category {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String code;

    @DatabaseField
    private String title;

    @DatabaseField
    private String src;

    /**
     * Sign describes direction of transaction. If True the tx is positive.
     */
    @DatabaseField
    private boolean sign;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Bucket bucket;

    public Category(){}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public boolean getSign(){
        return sign;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getTitle()).toString();
    }

    @Override
    public boolean equals(Object o) {
        Category other = (Category) o;
        return new EqualsBuilder()
                .append(this.getCode(), other.getCode())
                .append(this.getTitle(), other.getTitle())
                .isEquals();
    }


}
