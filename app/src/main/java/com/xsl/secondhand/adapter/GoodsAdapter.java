package com.xsl.secondhand.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.xsl.secondhand.R;
import com.xsl.secondhand.activity.GoodsDetailActivity;
import com.xsl.secondhand.utils.PicassoImageLoader;
import com.xsl.secondhand.widget.CircleImageView;

import java.util.List;
import java.util.Random;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2017/12/22,Time:17:00
 * Description:
 */

public class GoodsAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {

    List<AVObject> mItemList;
    Context mContext;
    OnItemClickListener mOnItemClickListener = null;

    public GoodsAdapter(Context context, List<AVObject> datas) {
        this.mItemList = datas;
        this.mContext = context;
    }

    public void setList(List<AVObject> itemList) {
        this.mItemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.nearby_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.mNearbyTitle.setText(mItemList.get(position).getString("title"));
        holder.mNearbyPrice.setText("￥" + mItemList.get(position).getInt("price") + "");
        String url = mItemList.get(position).getAVUser("owner").getAVFile("image").getUrl();
        if (!TextUtils.isEmpty(url)) {
            new PicassoImageLoader().displayImage(mContext, url, holder.mNearOwnerImg);
        }
        holder.mNearOwner.setText(mItemList.get(position).getAVUser("owner") == null ? "" : mItemList.get(position).getAVUser("owner").getUsername());
        if (TextUtils.isEmpty(holder.mNearPeople.getText())) {
            holder.mNearPeople.setText(new Random().nextInt(501) + "人看过");
        }
        if (TextUtils.isEmpty(holder.mTime.getText())) {
            holder.mTime.setText(new Random().nextInt(51) + "分钟前来过");
        }
        new PicassoImageLoader().displayImage(mContext, mItemList.get(position).getAVFile("image") == null ? "www" : mItemList.get(position).getAVFile("image").getUrl(), holder.mGoodsImg);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                intent.putExtra("title", mItemList.get(position).getString("title"));
                intent.putExtra("description", mItemList.get(position).getString("description"));
                intent.putExtra("userName", mItemList.get(position).getAVUser("owner").getUsername());
                intent.putExtra("img", mItemList.get(position).getAVFile("image").getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }


    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

class ViewHolder extends RecyclerView.ViewHolder {

    View rootView;
    CircleImageView mNearOwnerImg;
    /****惟愿无事常相见****/
    TextView mNearOwner;
    ImageView mGoodsImg;
    /****   出售我可爱的舍不得的手机啦****/
    TextView mNearbyTitle;
    /****￥49****/
    TextView mNearbyPrice;
    /****300人看过****/
    TextView mNearPeople;
    /****300人看过****/
    TextView mTime;

    /****   越南进口红心火龙果4个装 单果约350~400g 新鲜水果****/

    public ViewHolder(View itemView) {
        super(itemView);
        rootView = itemView.findViewById(R.id.cardView);
        mGoodsImg = itemView.findViewById(R.id.goods_img);
        mNearOwnerImg = itemView.findViewById(R.id.near_owner_img);
        mTime = itemView.findViewById(R.id.time);
        mNearOwner = itemView.findViewById(R.id.near_owner);
        mNearbyTitle = itemView.findViewById(R.id.nearby_title);
        mNearbyPrice = itemView.findViewById(R.id.nearby_price);
        mNearPeople = itemView.findViewById(R.id.near_people);
    }
}