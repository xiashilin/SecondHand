package com.xsl.secondhand.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xsl.secondhand.base.LazyFragment;

import java.util.List;

/**
 * @author xsl
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<LazyFragment> fragments;
    private String[] mSubTitle;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<LazyFragment> fragments, String[] subTitle) {
        super(fm);
        this.fragments = fragments;
        this.mSubTitle = subTitle;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mSubTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSubTitle[position];
    }

    //防止fragment自动销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}