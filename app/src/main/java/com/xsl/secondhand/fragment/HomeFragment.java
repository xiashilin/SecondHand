package com.xsl.secondhand.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.xsl.secondhand.R;
import com.xsl.secondhand.adapter.SimpleFragmentPagerAdapter;
import com.xsl.secondhand.base.BaseFragment;
import com.xsl.secondhand.base.LazyFragment;
import com.xsl.secondhand.utils.PicassoImageLoader;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:20:11
 * Description:
 */

public class HomeFragment extends BaseFragment {
    @ViewById(R.id.title)
    TextView toolbar;
    @ViewById(R.id.banner)
    Banner banner;
    @ViewById(R.id.layout_tab)
    TabLayout mTabLayout;
    @ViewById(R.id.view_pager)
    ViewPager mViewPager;

    private SimpleFragmentPagerAdapter mAdapter;

    private List<LazyFragment> mFragments = new ArrayList<>();
    private String tabTitles[] = new String[]{"新鲜的", "附近的"};

    @Override
    protected int getLayoutRes() {
        return R.layout.fg_home;
    }

    @Override
    protected void initView() {
        toolbar.setText(R.string.title_home);
        mAdapter = new SimpleFragmentPagerAdapter(getChildFragmentManager(), mFragments, tabTitles);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSmoothScrollingEnabled(true);
    }

    @Override
    public void initData() {
        List<String> images = new ArrayList<>();

        images.add("http://lc-c8h5pfqx.cn-n1.lcfile.com/ad4e1516ccb439a577c2.jpg");
        images.add("http://lc-c8h5pfqx.cn-n1.lcfile.com/5bcacef5cc0ffdd270bc.jpg");
        images.add("http://lc-c8h5pfqx.cn-n1.lcfile.com/66458e5945f1910d51db.jpg");
        images.add("http://lc-c8h5pfqx.cn-n1.lcfile.com/3919cd8da18715dca231.jpg");
        banner.setImages(images).setImageLoader(new PicassoImageLoader()).start();
        banner.setDelayTime(10000);
        mFragments.add(NearbyFragment.newInstance());
        mFragments.add(RecommendFragment.newInstance());
    }
}
