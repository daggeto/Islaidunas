package com.islaidunas.ui;

import android.content.Context;
import android.content.Intent;

/**
 * Created by daggreto on 2014.05.07.
 */
public class Navigator {
    public static void goToAddTransaction(Context context){
        Intent i = new Intent(context, AddTransactionActivity.class);
        context.startActivity(i);
    }
}
