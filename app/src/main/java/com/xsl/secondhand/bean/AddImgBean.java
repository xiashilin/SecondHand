package com.xsl.secondhand.bean;

/**
 * Email:1479714932@qq.com
 *
 * @author:xsl Date:2018/4/22,Time:15:32
 * Description:
 */

public class AddImgBean {
    private String url;
    private Boolean isNet;

    public AddImgBean() {
        super();
    }

    public AddImgBean(String url, Boolean isNet) {
        super();
        this.url = url;
        this.isNet = isNet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsNet() {
        return isNet;
    }

    public void setIsNet(Boolean isNet) {
        this.isNet = isNet;
    }

}
