package com.xsl.secondhand.fragment;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;
import com.xsl.secondhand.R;
import com.xsl.secondhand.activity.AboutActivity;
import com.xsl.secondhand.activity.CollectActivity;
import com.xsl.secondhand.activity.FeedBackActivity;
import com.xsl.secondhand.activity.MainActivity;
import com.xsl.secondhand.activity.MessageActivity;
import com.xsl.secondhand.activity.ReleaseGoodsActivity;
import com.xsl.secondhand.activity.UserInfoActivity;
import com.xsl.secondhand.activity.LoginActivity;
import com.xsl.secondhand.base.BaseFragment;
import com.xsl.secondhand.bean.UserBean;
import com.xsl.secondhand.utils.SharedPUtils;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.widget.SettingItemView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:20:12
 * Description:
 */

public class MeFragment extends BaseFragment implements SettingItemView.OnSettingItemClick {
    @ViewById(R.id.title)
    TextView toolbar;
    @ViewById(R.id.user_icon)
    private ImageView mUserIcon;
    /****惟愿无事常相见****/
    @ViewById(R.id.user_name)
    private TextView mUserName;
    @ViewById(R.id.item_info)
    private SettingItemView mItemInfo;
    @ViewById(R.id.item_money)
    private SettingItemView mItemMoney;
    @ViewById(R.id.item_feedback)
    private SettingItemView mItemFeedBack;
    @ViewById(R.id.item_collection)
    private SettingItemView mItemCollection;
    @ViewById(R.id.item_about)
    private SettingItemView mItemAbout;
    @ViewById(R.id.item_release)
    private SettingItemView mItemRelease;

    @Override
    protected int getLayoutRes() {
        return R.layout.fg_me;
    }

    @Override
    protected void initView() {
        toolbar.setText(R.string.title_me);
        mItemInfo.setOnSettingItemClick(this);
        mItemMoney.setOnSettingItemClick(this);
        mItemFeedBack.setOnSettingItemClick(this);
        mItemCollection.setOnSettingItemClick(this);
        mItemAbout.setOnSettingItemClick(this);
        mItemRelease.setOnSettingItemClick(this);
    }

    @Override
    public void initData() {
        refrashData();
    }


    @OnClick({R.id.user_info, R.id.user_name})
    private void click(View view) {
        switch (view.getId()) {
            case R.id.user_info:
                if (checkLogin()) {
                    this.startActivityForResult(new Intent(getContext(), UserInfoActivity.class), 42);
                } else {
                    ToastUtils.show(getContext(), "请登录...");
                    this.startActivityForResult(new Intent(getContext(), LoginActivity.class), 42);
                    ((MainActivity) mContext).finish();
                }

                break;
            case R.id.user_name:
                if (!checkLogin())
                    ToastUtils.show(getContext(), "请登录...");
                this.startActivityForResult(new Intent(getContext(), LoginActivity.class), 42);
                ((MainActivity) mContext).finish();
                break;
        }

    }

    @Override
    public void click(View view, boolean isChecked) {
        if (checkLogin()) {

            switch (view.getId()) {
                case R.id.item_info:
                    goActivity(getContext(), MessageActivity.class);
                    break;
                case R.id.item_release:
                    goActivity(getContext(), ReleaseGoodsActivity.class);
                    break;
                case R.id.item_money:
                    SnackBarUtils.show(getContext(), "暂未开发");
                    break;
                case R.id.item_feedback:
                    goActivity(getContext(), FeedBackActivity.class);
                    break;
                case R.id.item_collection:
                    goActivity(getContext(), CollectActivity.class);
                    break;
                case R.id.item_about:
                    goActivity(getContext(), AboutActivity.class);
                    break;
            }

        } else {
            ToastUtils.show(getContext(), "请登录...");
            this.startActivityForResult(new Intent(getContext(), LoginActivity.class), getActivity().RESULT_FIRST_USER);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42) {
            refrashData();
        }
    }

    private void refrashData() {
        if (AVUser.getCurrentUser() != null) {
            mUserName.setText(AVUser.getCurrentUser().getUsername());
            Picasso.with(mContext).load(AVUser.getCurrentUser().getAVFile("image") == null ? "www" : AVUser.getCurrentUser().getAVFile("image").getUrl()).into(mUserIcon);
        } else {
            mUserName.setText("点击登录");
            Picasso.with(mContext).load(R.mipmap.ic_def_icon).into(mUserIcon);
        }
    }
}
