package com.library.db.dao.news;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.Where;
import com.library.db.bean.DBNewsBean;
import com.library.db.dao.DBBaseDao;
import com.library.host.HostHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jzy on 2017/6/16.
 */

public class DBNewsDao extends DBBaseDao<DBNewsBean> implements IDBNewsDao {
    public static final long CACHE_TIME_OUT = 2 * 24 * 60 * 60 * 1000;


    private DBNewsDao(Context context) {
        super(context, new DBNewsBean());
    }

    private static class HOLDER {
        static final IDBNewsDao INS = new DBNewsDao(HostHelper.getInstance().getAppContext());
    }

    public static IDBNewsDao getInstance() {
        return HOLDER.INS;
    }

    @Override
    public void add(String newsId) {
        add(new DBNewsBean(newsId, System.currentTimeMillis()));
    }

    @Override
    public void clearExpired() {
        long expiredTime = System.currentTimeMillis() - CACHE_TIME_OUT;
        try {
            DeleteBuilder<DBNewsBean, Integer> builder = dao.deleteBuilder();
            Where<DBNewsBean, Integer> where = builder.where();
            where.le("createTime", expiredTime);
            builder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String newsId) {
        Map<String, Object> params = new HashMap<>();
        params.put("newsId", newsId);
        List<DBNewsBean> list = getByParams(params, null, true);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<DBNewsBean> getAll() {
        return getByParams(null, null, true);
    }
}
