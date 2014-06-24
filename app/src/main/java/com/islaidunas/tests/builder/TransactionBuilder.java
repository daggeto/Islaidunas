package com.islaidunas.tests.builder;

import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by daggreto on 2014.06.15.
 */
public class TransactionBuilder {
    private Transaction currentTx;

    public TransactionBuilder(){
        currentTx = new Transaction();
    }

    public TransactionBuilder(String id){
        this();
        currentTx.setId(id);
    }

    public TransactionBuilder withTitle(String title){
        currentTx.setTitle(title);
        return this;
    }

    public TransactionBuilder withAmount(BigDecimal amount){
        currentTx.setAmount(amount);
        return this;
    }

    public TransactionBuilder withDate(Date date){
        currentTx.setDate(date);
        return this;
    }

    public TransactionBuilder withCategory(Category category){
        currentTx.setCategory(category);
        return this;
    }

    public Transaction build(){
        return currentTx;
    }

}
