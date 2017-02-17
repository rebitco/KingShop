package cn.king.kingshop.bean;

import java.io.Serializable;

/**
 * Created by king on 2016/11/28.
 */

public class BaseBean implements Serializable {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
