package com.xsl.secondhand.bean;

import java.util.List;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/22,Time:13:52
 * Description:
 */

public class KnowledgeData {


    private boolean success;
    private List<KnowledgeBean> itemList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<KnowledgeBean> getItemList() {
        return itemList;
    }

    public void setItemList(List<KnowledgeBean> itemList) {
        this.itemList = itemList;
    }
}
