package com.islaidunas.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.islaidunas.R;

public class SpinnerDropDownView extends LinearLayout {

    public SpinnerDropDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerDropDownView(Context context, String text, boolean opened) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.spinner_dropdown, this, true);

    }
}
