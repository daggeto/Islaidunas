package com.islaidunas.domain;

import com.islaidunas.core.dbxStoreOrm.mapper.DbxField;
import com.islaidunas.core.dbxStoreOrm.mapper.DbxId;
import com.islaidunas.core.dbxStoreOrm.mapper.DbxTable;
import com.islaidunas.core.dbxStoreOrm.mapper.EntityMapper;
import com.j256.ormlite.field.DatabaseField;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by daggreto on 2014.05.06.
 */

@DbxTable
public class Transaction {

    @DbxId
    private String id;

    @DbxField
    private String title;

    @DbxField
    private BigDecimal amount;

    @DbxField
    private Date date;

    @DbxField
    private Category category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
