package com.xsl.secondhand.utils;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2017/11/28,Time:11:02
 * Description:
 */

public class FragmentManagerHelper {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    /**
     * 添加fragment
     *
     * @param fragment
     */
    public void addFragment(Fragment fragment) {
        //开启事务
        FragmentTransaction fg = mFragmentManager.beginTransaction();
        //添加到容器
        fg.add(mContainerViewId, fragment);
        //提交事务
        fg.commit();
    }

    /**
     * 替换fragment
     *
     * @param fragment
     */
    public void switchFragment(Fragment fragment) {
        //开启事务
        FragmentTransaction fg = mFragmentManager.beginTransaction();
        List<Fragment> childFragments = mFragmentManager.getFragments();
        for (Fragment childFragment : childFragments) {
            fg.hide(childFragment);
        }
        if (!childFragments.contains(fragment)) {
            fg.add(mContainerViewId, fragment);
        } else {
            fg.show(fragment);
        }
        fg.commit();
    }

}
