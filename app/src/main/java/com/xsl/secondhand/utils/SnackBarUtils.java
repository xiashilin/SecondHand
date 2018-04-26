package com.xsl.secondhand.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by xsl on 2016/6/28.
 * Class desc: 单例土司
 */
public class SnackBarUtils {


    public static void show(View view) {
        show(view, null);
    }

    public static void show(Context context, String msg) {
        show(((Activity) context), msg);
    }

    public static void show(Activity activity, String msg) {
        show(activity.getWindow().getDecorView(), msg);
    }


    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

}
