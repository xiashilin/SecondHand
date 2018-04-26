package com.xsl.secondhand.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.wang.avi.AVLoadingIndicatorView;
import com.xsl.secondhand.R;
import com.xsl.secondhand.activity.LoginActivity;
import com.xsl.secondhand.activity.MainActivity;
import com.xsl.secondhand.activity.ReleaseActivity;
import com.xsl.secondhand.adapter.NotifyAdapter;
import com.xsl.secondhand.base.BaseFragment;
import com.xsl.secondhand.bean.Constant;
import com.xsl.secondhand.bean.DataBean;
import com.xsl.secondhand.bean.KnowledgeBean;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ToastUtils;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.xsl.secondhand.widget.EmptyRecyclerView;
import com.xsl.secondhand.widget.LimitScrollerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:20:13
 * Description:
 */

public class NoticeFragment extends BaseFragment {
    @ViewById(R.id.title)
    TextView toolbar;
    @ViewById(R.id.knowledge_rcView)
    private EmptyRecyclerView mRecyclerView;
    @ViewById(R.id.empty_view)
    private View mEmptyView;
    @ViewById(R.id.refreshlayout)
    private SwipeRefreshLayout mRefreshLayout;
    @ViewById(R.id.avi)
    private AVLoadingIndicatorView mAvi;
    @ViewById(R.id.avi_loadmore)
    private AVLoadingIndicatorView mAviLoadMore;
    @ViewById(R.id.layout_loadmore)
    private LinearLayout mLayoutLoadMore;
    @ViewById(R.id.float_btn)
    FloatingActionButton mFloatingActionButton;
    @ViewById(R.id.limitScroll)
    LimitScrollerView limitScrollerView;
    private NotifyAdapter mNotifyAdapter;
    private List<AVObject> mItemList;
    private int mPage = 0;
    private boolean mIsLoadMore = true;//是否可以加载更多
    private MyLimitScrollAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fg_knowledge;
    }

    @Override
    protected void initView() {
        toolbar.setText(R.string.title_knowledge);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        mItemList = new ArrayList<>();
        mNotifyAdapter = new NotifyAdapter(mContext, mItemList);
        mRecyclerView.setAdapter(mNotifyAdapter);
        mRecyclerView.setmEmptyView(mEmptyView);
        mRecyclerView.hideEmptyView();
        showLoading();
        getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
        //API:1、设置数据适配器
        adapter = new MyLimitScrollAdapter();
        limitScrollerView.setDataAdapter(adapter);

        initDataBean();
    }

    private void initDataBean() {


        List<DataBean> datas = new ArrayList<>();
        datas.add(new DataBean(R.mipmap.ic_notify, "失物招领，本人捡到一部手机，请电15226541121"));
        datas.add(new DataBean(R.mipmap.ic_notify, "考研讲座，本周五阶梯教室展开讲座"));
        datas.add(new DataBean(R.mipmap.ic_notify, "我心有猛虎，细嗅蔷薇；盛宴之后，泪流满面"));
        datas.add(new DataBean(R.mipmap.ic_notify, "紧急通知，本周五下午全体人员到阶梯教室102开会"));
        datas.add(new DataBean(R.mipmap.ic_notify, "恭喜我校学生获得江苏省感动人物称号"));

        adapter.setDatas(datas);


    }

    @Override
    public void initData() {
        //设置下拉刷新样式
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新事件
                mPage = 0;
                mIsLoadMore = true;
                getDataFromServer(Constant.GET_DATA_TYPE_NOMAL);
            }
        });
        //监听上拉加载更多
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener());
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkLogin()) {
                    Intent intent = new Intent(mContext, ReleaseActivity.class);
                    mContext.startActivity(intent);
                } else {
                    ToastUtils.show(getContext(), "请登录...");
                    NoticeFragment.this.startActivityForResult(new Intent(getContext(), LoginActivity.class), getActivity().RESULT_FIRST_USER);
                    ((MainActivity) mContext).finish();
                }

            }
        });
    }

    public void getDataFromServer(final int type) {

        AVQuery<AVObject> avQuery = new AVQuery<>("Notify");
        avQuery.orderByDescending("createdAt");
        avQuery.setSkip(mPage * 10);
        avQuery.limit(10);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        stopRefresh();
                        hideLoading();
                        stopLoadingMore();

                        //更新界面数据
                        if (Constant.GET_DATA_TYPE_NOMAL == type) {
                            //正常模式下，清空之前数据，重新加载
                            mItemList.clear();
                            mItemList = list;
                        } else {
                            //加载更多模式
                            mItemList.addAll(list);
                        }
                        //如果获取的数据不足一页，代表当前已经没有更过数据，关闭加载更多
                        if (list.size() < 10) {
                            mIsLoadMore = false;
                        }
                        mNotifyAdapter.setList(mItemList);
                        mNotifyAdapter.notifyDataSetChanged();
                    } else if (mPage != 0) mPage = mPage - 1;
                } else {
                    stopRefresh();
                    hideLoading();
                    stopLoadingMore();
                    e.printStackTrace();
                    SnackBarUtils.show(mContext, e.getCode() + "");
                }
            }
        });
    }

    /**
     * 开启加载中动画
     */

    public void showLoading() {
        mAvi.smoothToShow();
    }

    /**
     * 关闭加载中动画
     */

    public void hideLoading() {
        if (mAvi.isShown()) {
            mAvi.smoothToHide();
        }
    }

    public void stopRefresh() {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    public void stopLoadingMore() {
        mLayoutLoadMore.setVisibility(View.GONE);
        mAviLoadMore.smoothToHide();
    }

    /**
     * 开启加载更多动画
     */
    public void startLoadingMore() {
        mLayoutLoadMore.setVisibility(View.VISIBLE);
        mAviLoadMore.smoothToShow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * RecyclerView 滑动监听器
     */
    class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        int mScrollThreshold = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //当前RecyclerView显示出来的最后一个的item的position
            int lastPosition = -1;

            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    //通过LayoutManager找到当前显示的最后的item的position
                    lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                    //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                    int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                    lastPosition = findMax(lastPositions);
                }

                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    if (!recyclerView.canScrollVertically(1)) {
                        recyclerView.smoothScrollToPosition(lastPosition);
                        if (!mIsLoadMore) {
                            SnackBarUtils.show(mContext, "木有更多数据了...");
                            return;
                        }
                        //此时需要请求更多数据，显示加载更多界面
                        mPage++;
                        startLoadingMore();
                        getDataFromServer(Constant.GET_DATA_TYPE_LOADMORE);
                    }
                }

            }


        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);

            boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
            if (isSignificantDelta) {
                if (dy > 0) {
                    onScrollUp();
                } else {
                    onScrollDown();
                }
            }
        }

        private void onScrollUp() {
            mFloatingActionButton.setVisibility(View.GONE);
        }

        private void onScrollDown() {
            mFloatingActionButton.setVisibility(View.VISIBLE);
        }

    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    //TODO 修改适配器绑定数据
    class MyLimitScrollAdapter implements LimitScrollerView.LimitScrollAdapter {

        private List<DataBean> datas;

        public void setDatas(List<DataBean> datas) {
            this.datas = datas;
            //API:2、开始滚动
            limitScrollerView.startScroll();
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public View getView(int index) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.limit_scroller_item, null, false);
            ImageView iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            TextView tv_text = (TextView) itemView.findViewById(R.id.tv_text);

            //绑定数据
            DataBean data = datas.get(index);
            itemView.setTag(data);
            iv_icon.setImageResource(data.getIcon());
            tv_text.setText(data.getText());
            return itemView;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        limitScrollerView.startScroll();    //API:2、开始滚动
    }

    @Override
    public void onStop() {
        super.onStop();
        limitScrollerView.cancel();
    }
}
