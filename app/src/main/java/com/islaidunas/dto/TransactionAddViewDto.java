package com.islaidunas.dto;

import android.text.format.DateFormat;

import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by daggreto on 2014.05.12.
 */
public class TransactionAddViewDto {

    private Transaction transaction;

    public TransactionAddViewDto(){
        this(new Transaction());
    }

    public TransactionAddViewDto(Transaction transaction){
        if(transaction == null){
            transaction = new Transaction();
        }else{
            this.transaction = transaction;
        }
    }

    public String getDate() {
        return formatDate(transaction.getDate());
    }

    public void setDate(String date) {
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getTitle() {
        return transaction.getTitle();
    }

    public void setTitle(String title) {
        transaction.setTitle(title);
    }

    public Category getCategory() {
        return transaction.getCategory();
    }

    public void setCategory(Category category) {
        transaction.setCategory(category);
    }

    public String getAmount() {
        if(transaction.getAmount() == null){
            return "0";
        }
        return transaction.getAmount().toString();
    }

    public void setAmount(String amount) {
        if(amount == null || amount.isEmpty()){
            transaction.setAmount(BigDecimal.ZERO);
        }
        try {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
            df.setParseBigDecimal(true);

            amount = amount.replaceFirst("^0+(?!$)", "");
            transaction.setAmount((BigDecimal) df.parseObject(amount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(Date date){
        return DateFormat.format("EEEE, MMM d, yyyy", date).toString();
    }

}
