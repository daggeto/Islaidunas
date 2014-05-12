package com.islaidunas.dto;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by daggreto on 2014.05.06.
 */
public class TransactionListItemViewDto {

    private Transaction transaction;

    private String title;
    private Category category;
    private String amount;
    private String date;

    public TransactionListItemViewDto(Transaction transaction){
        this.transaction = transaction;
        this.title = transaction.getTitle();
        this.category = transaction.getCategory();
        this.amount = parseAmount(transaction.getAmount());
        this.date = formatDate(transaction.getDate());
    }

    private String parseAmount(BigDecimal amount){
        return amount.toString().concat(" LT");
    }

    private String formatDate(Date date){
        return DateFormat.format("EEEE, MMM d, yyyy", date).toString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
