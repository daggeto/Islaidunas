package com.islaidunas.core.dbxStoreOrm;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by daggreto on 2014.06.15.
 */
public class Logger {

    public static void error(String tag, String message){
        Log.e(tag, message);
    }
    public static void warn(String tag, String message) { Log.w(tag, message);}
}
