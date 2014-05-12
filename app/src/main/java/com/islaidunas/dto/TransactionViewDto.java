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
public class TransactionViewDto {

    private Transaction transaction;

    private String title;
    private Category category;
    private BigDecimal amount;
    private String date;

    public TransactionViewDto(Transaction transaction){
        this.transaction = transaction;
        this.title = transaction.getTitle();
        this.category = transaction.getCategory();
        this.amount = transaction.getAmount();
        this.date = formatDate(transaction.getDate());
    }

    public void persistToView(View view){
        TextView transactionName = (TextView) view.findViewById(R.id.transaction_name);
        transactionName.setText(title);

        TextView transactionCategory = (TextView) view.findViewById(R.id.transaction_date);
        transactionCategory.setText(date);

        TextView transactionAmount = (TextView) view.findViewById(R.id.transaction_amount);
        transactionAmount.setText(parseAmount(amount));
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
