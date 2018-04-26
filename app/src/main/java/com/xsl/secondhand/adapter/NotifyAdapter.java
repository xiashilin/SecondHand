package com.xsl.secondhand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.xsl.secondhand.R;

import java.util.List;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/9,Time:22:51
 * Description:
 */

public class NotifyAdapter extends RecyclerView.Adapter<NotifyViewHolder> implements View.OnClickListener {
    private List<AVObject> avObjectList;
    private Context mContext;
    private GoodsAdapter.OnItemClickListener mOnItemClickListener = null;

    public NotifyAdapter(Context context, List<AVObject> avObjectList) {
        this.mContext = context;
        this.avObjectList = avObjectList;
    }

    public void setList(List<AVObject> itemList) {
        this.avObjectList = itemList;
    }

    @Override
    public NotifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.knowledge_item, null);
        NotifyViewHolder viewHolder = new NotifyViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final NotifyViewHolder holder, final int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.mName.setText(avObjectList.get(position).getString("notifyTitle"));
        holder.mContent.setText(avObjectList.get(position).getString("notifyContent"));
    }


    @Override
    public int getItemCount() {
        return avObjectList.size();
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

    public void setOnItemClickListener(GoodsAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

class NotifyViewHolder extends RecyclerView.ViewHolder {

    public TextView mName;
    public TextView mContent;
    public View rootView;

    public NotifyViewHolder(View itemView) {
        super(itemView);
        rootView = itemView.findViewById(R.id.cardView);
        mName = itemView.findViewById(R.id.tv_name);
        mContent = itemView.findViewById(R.id.tv_content);
    }
}
