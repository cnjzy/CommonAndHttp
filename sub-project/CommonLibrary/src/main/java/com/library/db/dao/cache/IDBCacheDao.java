package com.library.db.dao.cache;

import com.library.db.bean.DBCacheBean;

import java.util.List;

public interface IDBCacheDao {

    int addCacheWithType(int type, String json);

    DBCacheBean getCacheWithType(int type);

    void delCacheWithType(int type);

    DBCacheBean getCache(String id, int type);

    List<DBCacheBean> getCacheList(String key);

    DBCacheBean getCache(String key);

    void saveCache(String id, int type, String json);

    void saveCache(String key, String json);

    void delCache(String id, int type);

    void delCache(String key);
}
