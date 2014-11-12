package com.islaidunas.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

import com.islaidunas.IslaidunasApplication;

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
}
