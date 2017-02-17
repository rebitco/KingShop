package cn.king.kingshop.bean;

import java.io.Serializable;

/**
 * Created by king on 2017/2/4.
 */

public class ShoppingCart extends Wares implements Serializable {

    private int count;//在商品的基础下 ,增加商品数量
    private boolean isChecked = true; //增加是否被选中

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
