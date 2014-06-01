package com.islaidunas.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dto.TransactionAddViewDto;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by daggreto on 2014.05.06.
 */
public class TransactionListItemView extends LinearLayout {

    @InjectView(R.id.transaction_name)
    TextView title;

    @InjectView(R.id.transaction_category)
    ImageView category;

    @InjectView(R.id.transaction_amount)
    TextView amount;

    @InjectView(R.id.transaction_date)
    TextView date;



    public TransactionListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransactionListItemView(Context context, Transaction transaction) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.list_item_transaction, this, true);

        ButterKnife.inject(this);

        category.setImageDrawable(getDrawable(transaction.getCategory()));
        title.setText(transaction.getTitle());
        date.setText(TransactionAddViewDto.formatDate(transaction.getDate()));
        amount.setText(parseAmount(transaction.getAmount()));
    }

    private String parseAmount(BigDecimal amount){
        return amount.toString().concat(" LT");
    }

    private Drawable getDrawable(Category item){
        int resourceId = getContext().getResources().getIdentifier(item.getSrc(), "drawable", getContext().getPackageName());

        if(resourceId == 0){
            resourceId = getContext().getResources().getIdentifier("def", "drawable", getContext().getPackageName());
        }

        return getContext().getResources().getDrawable(resourceId);
    }

}
