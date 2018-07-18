package com.library.db.bean;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by jzy on 2017/6/16.
 */

public class DBBaseBean implements Serializable {
    /**
     * 本地主键
     */
    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
