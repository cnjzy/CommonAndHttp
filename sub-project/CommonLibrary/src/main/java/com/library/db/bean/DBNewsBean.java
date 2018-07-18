package com.library.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.library.db.bean.DBBaseBean;

/**
 * @author jzy
 * created at 2018/7/3
 */
@DatabaseTable(tableName = "news")
public class DBNewsBean extends DBBaseBean {
    @DatabaseField
    private String newsId;
    @DatabaseField
    private long createTime;

    public DBNewsBean() {
    }

    public DBNewsBean(String newsId, long createTime) {
        this.newsId = newsId;
        this.createTime = createTime;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
