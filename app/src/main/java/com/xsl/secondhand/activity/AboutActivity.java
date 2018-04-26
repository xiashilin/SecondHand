package com.xsl.secondhand.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.ioc.ViewById;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/22,Time:18:03
 * Description:
 */

public class AboutActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected int getLayout() {
        return R.layout.ay_about;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("关于");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
