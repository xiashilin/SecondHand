package com.xsl.secondhand.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.utils.statusbar.StatusBarUtils;


/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/2/25,Time:11:29
 * Description:
 */

public class CollectActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    private Toolbar mToolbar;


    @Override
    protected int getLayout() {
        return R.layout.ay_message;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();

    }

    private void initToolBar() {
        StatusBarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("我的收藏");
    }

}
