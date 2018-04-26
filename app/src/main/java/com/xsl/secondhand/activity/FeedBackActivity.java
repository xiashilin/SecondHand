package com.xsl.secondhand.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;

import com.xsl.secondhand.R;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/22,Time:17:57
 * Description:
 */

public class FeedBackActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.feedback_content)
    EditText mContent;

    @Override
    protected int getLayout() {
        return R.layout.ay_feedback;
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
        mToolbar.setTitle("反馈");
    }

    @OnClick(R.id.feedback_btn_submit)
    public void Click() {
        if (TextUtils.isEmpty(mContent.getText().toString().trim())) {
            SnackBarUtils.show(mContext, "请输入内容");
            return;
        }
        ToastUtils.show(mContext, "反馈成功！我们会尽快处理");
        finish();
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
