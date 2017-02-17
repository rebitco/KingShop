package cn.king.kingshop.bean;

import java.io.Serializable;

/**
 * Created by king on 2017/2/5.
 */

public class Category extends BaseBean implements Serializable {

    private String name;
    private int sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
