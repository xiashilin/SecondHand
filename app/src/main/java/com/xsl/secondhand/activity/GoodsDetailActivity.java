package com.xsl.secondhand.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.squareup.picasso.Picasso;
import com.xsl.secondhand.R;
import com.xsl.secondhand.adapter.DetailCommentAdapter;
import com.xsl.secondhand.base.BaseActivity;
import com.xsl.secondhand.utils.PicassoImageLoader;
import com.xsl.secondhand.utils.SnackBarUtils;
import com.xsl.secondhand.utils.ioc.OnClick;
import com.xsl.secondhand.utils.ioc.ViewById;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/21,Time:19:56
 * Description:
 */

public class GoodsDetailActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.detail_desc)
    private TextView mDetailDesc;
    @ViewById(R.id.detail_title)
    private TextView mDetailTitle;
    @ViewById(R.id.detail_name)
    private TextView mDetailName;
    @ViewById(R.id.rl_comment)
    private RecyclerView mRlComment;
    @ViewById(R.id.img_collect)
    private ImageView collect;
    @ViewById(R.id.img_zan)
    private ImageView zan;

    @ViewById(R.id.banner)
    private ImageView img;

    private List<String> commentList;

    @Override
    protected int getLayout() {
        return R.layout.ay_fruit_detail;
    }

    @Override
    protected void initEventAndData() {
        initToolBar();
        initData();
        commentList = new ArrayList<>();
        commentList.add("这款还好就是领子太大了，小一些会更秀气精致，希望以后改进，长度宽度刚刚好。");
        commentList.add("实惠，比实体店便宜好多！");
        commentList.add("款式简洁大方，聚划算再加上100元生日券，到手价很划算。");
        commentList.add("面料很舒服，花色也漂亮，这么轻薄的裙子夏天穿肯定凉快。这个版型穿起来像不像孕妇装?_?｀本人160cm，大概105斤左右吧，穿着到小腿肚的位置。");
        commentList.add("衣服面料很舒服 版型不错 老公穿着很合身 很好看");
        commentList.add("有点酸?数量太少了，昨天收到的今天就剩下这几个了，我有葡萄酒，过来听你讲故事");
        commentList.add("快递包装很好！好大的一箱子啊。每一个果子都用泡沫带包装着，很干净很好！真的好好吃！甜！甜！甜！大小均匀，沒有坏果皮薄水份足，让我好舒心，沃柑沒让我失望，真的好吃新鲜，服务又好赞五星！快递小哥给力也赞五星！");
        commentList.add("打开就可以看出卖家满满的诚意。苹果很大个，包装也特别严实，检查了一下，没有坏果。吃了一个。很脆甜。很满意。吃完会回购。");

        mRlComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DetailCommentAdapter peCommentAdapter = new DetailCommentAdapter(this, commentList);
        mRlComment.setNestedScrollingEnabled(false);
        mRlComment.setAdapter(peCommentAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        mDetailDesc.setText(intent.getStringExtra("description"));
        mDetailTitle.setText(intent.getStringExtra("title"));
        mDetailName.setText(intent.getStringExtra("userName"));
        new PicassoImageLoader().displayImage(mContext, intent.getStringExtra("img"), img);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon_back);
        mToolbar.setTitle("商品详情");
    }

    @OnClick({R.id.tv_zan, R.id.tv_chat, R.id.tv_collect, R.id.tv_need})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.tv_zan:
                Picasso.with(mContext).load(R.mipmap.ic_zaned).into(zan);
                break;
            case R.id.tv_chat:
                goActivity(LeaveMessageActivity.class);
                break;
            case R.id.tv_collect:
                Picasso.with(mContext).load(R.mipmap.ic_collected).into(collect);
                break;
            case R.id.tv_need:
                SnackBarUtils.show(mContext, "暂未开发");
                break;
        }

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
