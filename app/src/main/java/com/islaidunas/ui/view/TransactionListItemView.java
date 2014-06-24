package com.islaidunas.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.islaidunas.R;
import com.islaidunas.domain.Category;
import com.islaidunas.domain.Transaction;
import com.islaidunas.dto.TransactionAddViewDto;

import java.math.BigDecimal;

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
        int resourceId = 0;
        if(item == null){
            return getDefaultDrawable();
        }

        resourceId = getContext().getResources().getIdentifier(item.getSrc(), "drawable", getContext().getPackageName());

        if(resourceId == 0){
            return getDefaultDrawable();
        }

        return getDrawableIcon(resourceId);
    }

    private Drawable getDrawableIcon(int resourceId) {
        return getContext().getResources().getDrawable(resourceId);
    }

    private Drawable getDefaultDrawable() {
        int resId = getContext().getResources().getIdentifier("def", "drawable", getContext().getPackageName());
        return getDrawableIcon(resId);
    }

}
