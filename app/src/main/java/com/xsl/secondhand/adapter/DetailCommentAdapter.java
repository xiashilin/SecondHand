package com.xsl.secondhand.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xsl.secondhand.R;

import java.util.List;


/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/2/22,Time:11:49
 * Description:
 */

public class DetailCommentAdapter extends RecyclerView.Adapter<DetailCommentAdapter.CommentViewHolder> {
    private Context mContext;
    private List<String> commentList;

    public DetailCommentAdapter(Context context, List<String> commentList) {
        this.mContext = context;
        this.commentList = commentList;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.detail_comment.setText(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView detail_comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            detail_comment = itemView.findViewById(R.id.detail_comment);
        }
    }
}
