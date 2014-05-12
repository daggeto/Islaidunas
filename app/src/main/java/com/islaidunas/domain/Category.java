package com.islaidunas.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Created by daggreto on 2014.05.07.
 */

@DatabaseTable(tableName = "category")
public class Category {
    @DatabaseField(id = true)
    private String code;
    @DatabaseField
    private String title;

    @DatabaseField
    private String src;

    public Category(){}

    public Category(String code, String title, String src){
        this.code = code;
        this.title = title;
        this.src = src;
    }

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
