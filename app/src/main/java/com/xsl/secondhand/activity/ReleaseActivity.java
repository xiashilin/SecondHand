package com.xsl.secondhand.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/22,Time:14:55
 * Description:
 */

public class ReleaseActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    /****请输入通知名称****/
    @ViewById(R.id.ev_name)
    private EditText mEvName;
    /****请输入通知内容****/
    @ViewById(R.id.ev_introduction)
    private EditText mEvIntroduction;
    @ViewById(R.id.mProgess)
    private ProgressBar mMProgess;

    @Override
    protected int getLayout() {
        return R.layout.ay_release;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();

    }

    @OnClick(R.id.btn_submit)
    public void onclick() {
        if ("".equals(mEvName.getText().toString()) && mEvName.getText().toString().length() < 5) {
            SnackBarUtils.show(mContext, "通知名称不正确");
            return;
        }
        if ("".equals(mEvIntroduction.getText().toString()) && mEvIntroduction.getText().toString().length() < 10) {
            SnackBarUtils.show(mContext, "通知内容格式不正确");
            return;
        }
        mMProgess.setVisibility(View.VISIBLE);
        AVObject notify = new AVObject("Notify");
        notify.put("notifyTitle", mEvName.getText().toString());
        notify.put("notifyContent", mEvIntroduction.getText().toString());
        notify.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mMProgess.setVisibility(View.GONE);
                    ToastUtils.show(mContext, "发布成功！");
                    mContext.finish();
                } else {
                    mMProgess.setVisibility(View.GONE);
                    SnackBarUtils.show(mContext, e.getMessage());
                }
            }
        });

    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("发布通知");
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
