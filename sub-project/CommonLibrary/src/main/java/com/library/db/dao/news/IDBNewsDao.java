package com.library.db.dao.news;

import com.library.db.bean.DBNewsBean;

import java.util.List;

/**
 * @author jzy
 * created at 2018/7/3
 */
public interface IDBNewsDao {
    void add(String newsId);

    void clearExpired();

    boolean exists(String newsId);

    List<DBNewsBean> getAll();
}
