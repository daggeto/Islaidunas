package com.islaidunas.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.islaidunas.IslaidunasApplication;
import com.islaidunas.R;
import com.islaidunas.core.ui.MainActivity;

import butterknife.ButterKnife;

/**
 * Created by daggreto on 2014.10.03.
 */
public class ResourcesUtils {
    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = IslaidunasApplication.getContext().getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public static String addWhiteSpaceToEnd(String original){
        return original + " ";
    }

    public static int getColor(int id){
        return IslaidunasApplication.getContext().getResources().getColor(id);
    }

    public static float getActionBarSize(){
        Context context = IslaidunasApplication.getContext();

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);

        int[] actionBarSize = new int[] { android.R.attr.actionBarSize };
        TypedArray a = context.obtainStyledAttributes(typedValue.data, actionBarSize);
        float size = a.getDimensionPixelSize(0, -1);
        a.recycle();
        return size;
    }

    public static ViewGroup getToolBarContent(){
        MainActivity context = (MainActivity) IslaidunasApplication.getActivityContext();
        Toolbar actionBar = context.getToolbar();
        return ButterKnife.findById(actionBar, R.id.toolbar_content);
    }

    public static ViewGroup getToolbarContainer(){
        MainActivity context = (MainActivity) IslaidunasApplication.getActivityContext();
        return ButterKnife.findById(context, R.id.toolbar_container);
    }
}
