package com.xsl.secondhand;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVOSCloud;


/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:19:52
 * Description:
 */

public class APP extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AVOSCloud.initialize(this, "C8h5pfqxIpPWoQd6tXjWPUPS-gzGzoHsz", "34fwK1Kg3cd0dlC2ew1cRCFw");
    }

    public static Context getContext() {
        return context;
    }
}
