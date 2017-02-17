package cn.king.kingshop;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import cn.king.kingshop.adapter.BaseAdapter;

/**
 * Created by king on 2017/1/18.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private SparseArray<View> views;
    private BaseAdapter.OnItemClickListener mOnItemClickListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);

        this.views = new SparseArray<View>();
        this.mOnItemClickListener = listener;
    }

    public Button getButton(int viewId) {
        return retrieveView(viewId);
    }

    public TextView getTextView(int viewId) {
        return retrieveView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return retrieveView(viewId);
    }


    public CheckBox getCheckBox(int viewId) {
        return retrieveView(viewId);
    }

    public View getView(int viewId) {
        return retrieveView(viewId);
    }

    /**
     * 通过id找到view
     * @param viewId 资源id
     * @param <T> 泛型, 继承于View
     * @return 返回一个View对象
     */
    protected <T extends View> T retrieveView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }

        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
