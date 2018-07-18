package com.test.plugin.data.req;

import com.common.http.bean.BaseReq;

/**
 * @author jzy
 * created at 2018/6/7
 */
public class ReqNewsList15 extends BaseReq {
    private int cat_ids;
    private int type = 1;
    private int page = 1;
    private int page_size = 20;

    public ReqNewsList15() {
    }

    public ReqNewsList15(int cat_ids, int type) {
        this.cat_ids = cat_ids;
        this.type = type;
    }

    public ReqNewsList15(int cat_ids, int type, int page) {
        this.cat_ids = cat_ids;
        this.type = type;
        this.page = page;
    }

    @Override
    public String getUrlPath() {
        return "/news/list/index";
    }

    public int getCat_ids() {
        return cat_ids;
    }

    public void setCat_ids(int cat_ids) {
        this.cat_ids = cat_ids;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }
}
