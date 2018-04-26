package com.xsl.secondhand.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xsl.secondhand.utils.ioc.ViewUtils;


/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2017/11/29,Time:19:39
 * Description:
 */

public abstract class LazyFragment extends Fragment {
    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isViewCreated;
    /**
     * Fragment对用户可见的标记
     */
    private boolean isUIVisible;

    protected Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewUtils.inject(this, view);
        mContext=getContext();
        isViewCreated = true;
        lazyLoad();
        initView(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            initData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;

        }
    }


    /**
     * 返回布局
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 具体的view，由子类实现
     */
    protected abstract void initView(View view);

    /**
     * 具体的业务逻辑，由子类实现
     */
    public abstract void initData();

}
