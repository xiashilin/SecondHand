package com.xsl.secondhand.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:21:24
 * Description:
 */

public abstract class DragFragmentPagerAdapter extends FragmentPagerAdapter {

    private View mCurrentView;

    public DragFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            mCurrentView = (View) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            mCurrentView = fragment.getView();
        }
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }
}
