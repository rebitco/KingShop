package cn.king.kingshop.adapter;

import android.content.Context;

import java.util.List;

import cn.king.kingshop.BaseViewHolder;

/**
 * Created by king on 2017/2/4.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {


    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }
}
