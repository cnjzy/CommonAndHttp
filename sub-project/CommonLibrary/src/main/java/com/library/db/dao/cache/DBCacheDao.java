package com.library.db.dao.cache;

import android.content.Context;

import com.library.db.bean.DBCacheBean;
import com.library.db.dao.DBBaseDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jzy on 2017/6/16.
 */

public class DBCacheDao extends DBBaseDao<DBCacheBean> implements IDBCacheDao {
    public static final long CACHE_DATE_HOUR = 60 * 60 * 1000;


    public DBCacheDao(Context context) {
        super(context, new DBCacheBean());
    }

    /**
     * 按照type添加数据
     *
     * @param type
     * @param json
     * @return
     */
    public int addCacheWithType(int type, String json) {
        try {
            DBCacheBean dbCacheBean = new DBCacheBean();
            dbCacheBean.setType(type);
            dbCacheBean.setJson(json);
            return add(dbCacheBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 按照type获取缓存
     *
     * @param type
     * @return
     */
    public DBCacheBean getCacheWithType(int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        List<DBCacheBean> list = getByParams(params, null, true);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 按照type删除数据
     *
     * @param type
     */
    public void delCacheWithType(int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        deleteByParams(params);
    }


    /**
     * 获取缓存
     *
     * @param id
     * @param type
     * @return
     */
    public DBCacheBean getCache(String id, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("cache_id", id);
        List<DBCacheBean> list = getByParams(params, null, true);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取缓存列表
     *
     * @param key
     * @return
     */
    public List<DBCacheBean> getCacheList(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        List<DBCacheBean> list = getByParams(params, null, true);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @return
     */
    public DBCacheBean getCache(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        List<DBCacheBean> list = getByParams(params, null, true);
        if (list != null && list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 添加缓存
     *
     * @param id
     * @param type
     * @param json
     */
    public void saveCache(String id, int type, String json) {
        // 删除缓存
        delCache(id, type);
        // 添加缓存
        DBCacheBean cacheBean = new DBCacheBean();
        cacheBean.setType(type);
        cacheBean.setCache_id(id);
        cacheBean.setJson(json);
        add(cacheBean);
    }

    /**
     * 报错缓存
     *
     * @param key
     * @param json
     */
    public void saveCache(String key, String json) {
        delCache(key);
        DBCacheBean cacheBean = new DBCacheBean();
        cacheBean.setKey(key);
        cacheBean.setJson(json);
        add(cacheBean);
    }

    /**
     * 删除缓存
     *
     * @param id
     * @param type
     */
    public void delCache(String id, int type) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("cache_id", id);
        deleteByParams(params);
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void delCache(String key) {
        Map<String, Object> params = new HashMap<>();
        params.put("key", key);
        deleteByParams(params);
    }
}
