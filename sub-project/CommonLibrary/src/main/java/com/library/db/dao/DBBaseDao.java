package com.library.db.dao;

import android.content.Context;
import android.text.TextUtils;

import com.library.db.DatabaseHelper;
import com.library.db.bean.DBBaseBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/9.
 */
public class DBBaseDao<T extends DBBaseBean> {
    protected Context context;
    protected Dao<T, Integer> dao;
    private DatabaseHelper helper;
    private T t;

    public DBBaseDao(Context context, T t) {
        try {
            this.t = t;
            this.context = context;
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(t.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最后一条数据ID
     *
     * @return
     */
    private int getLastId() {
        try {
            List list = dao.queryBuilder().selectColumns("id").orderBy("id", false).offset(0l).limit(1l).query();
            if (list != null && list.size() > 0) {
                DBBaseBean vo = (DBBaseBean) list.get(0);
                return vo.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 添加
     *
     * @param vo
     */
    public synchronized int add(T vo) {
        try {
            if (dao.create(vo) == 1) {
                return getLastId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 修改
     *
     * @param vo
     */
    public void update(T vo) {
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 修改
     */
    public void updateByParams(Map<String, Object> colParams, Map<String, Object> whereParams) {
        try {
            UpdateBuilder<T, Integer> builder = dao.updateBuilder();
            // 添加替换参数
            if (colParams != null && colParams.size() > 0) {
                Where<T, Integer> where = builder.where();
                Iterator<String> it = colParams.keySet().iterator();
                while (it.hasNext()) {
                    where.and();
                    String key = it.next();
                    builder.updateColumnValue(key, colParams.get(key));
                }
            }

            // 添加条件
            if (whereParams != null && whereParams.size() > 0) {
                Where<T, Integer> where = builder.where();
                Iterator<String> it = whereParams.keySet().iterator();
                while (it.hasNext()) {
                    where.and();
                    String key = it.next();
                    where.eq(key, whereParams.get(key));
                }
            }
            builder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(int id, String type) {
        try {
            dao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件删除
     *
     * @param params
     */
    public void deleteByParams(Map<String, Object> params) {
        try {
            DeleteBuilder<T, Integer> builder = dao.deleteBuilder();
            if (params != null && params.size() > 0) {
                Where<T, Integer> where = builder.where();
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    where.eq(key, params.get(key));
                    if (it.hasNext()) {
                        where.and();
                    }
                }
            }
            builder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T getById(int id) {
        try {
            return dao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照条件查询
     *
     * @param params
     */
    protected List<T> getByParams(Map<String, Object> params, String colName, boolean isAsc) {
        try {
            QueryBuilder<T, Integer> builder = dao.queryBuilder();
            if (params != null && params.size() > 0) {
                Where<T, Integer> where = builder.where();
                Iterator<String> it = params.keySet().iterator();
                int m = 0;
                while (it.hasNext()) {
                    if (m != 0) {
                        where.and();
                    }
                    String key = it.next();
                    where.eq(key, params.get(key));
                    m++;
                }
            }

            if (!TextUtils.isEmpty(colName)) {
                builder.orderBy(colName, isAsc);
            }

            return builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
