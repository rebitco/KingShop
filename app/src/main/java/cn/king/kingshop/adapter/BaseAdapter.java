package cn.king.kingshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.king.kingshop.BaseViewHolder;

/**
 * Created by king on 2017/1/18.
 */

public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected Context mContext;
    public int mLayoutResId;

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }
    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        this.mLayoutResId = layoutResId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, null);
        BaseViewHolder viewHolder = new BaseViewHolder(view, mListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getItem(position);
        convert((H) holder, item);
    }

    public abstract void convert(H viewHolder, T item);

    public T getItem(int position) {
        return position >= mDatas.size() ? null : mDatas.get(position);
    }

    public List<T> getDatas() {
        return mDatas;
    }

    //清除数据
    public void clear() {
        if(mDatas == null || mDatas.size() <= 0) {
            return;
        }

        for(Iterator iterator = mDatas.iterator(); iterator.hasNext();) {
            T t = (T) iterator.next();
            int position = mDatas.indexOf(t);
            iterator.remove();
            notifyItemRemoved(position);
        }
    }

    public void refreshData(List<T> list){

        clear();
        if(list !=null && list.size()>0){
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }


    //从列表中删除某项
    public void removeItem(T t) {
        int position = mDatas.indexOf(t);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    //添加数据
    public void addData(List<T> datas) {
        addData(0, datas);
    }
    public void addData(int position, List<T> list) {
        if (list == null || list.size() > 0) {
            for (T t : list) {
                mDatas.add(position, t);
                notifyItemInserted(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas==null||mDatas.size()<=0 ? 0 : mDatas.size();
    }

    //点击事件
    public OnItemClickListener mListener = null;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }
}
