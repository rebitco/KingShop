package cn.king.kingshop.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.king.kingshop.R;
import cn.king.kingshop.bean.Wares;

/**
 * Created by king on 2017/1/13.
 */

public class HotWaresAdapter extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder> {

    private List<Wares> mDatas;
    private LayoutInflater mLayoutFlater;

    public HotWaresAdapter(List<Wares> wares) {
        mDatas = wares;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mLayoutFlater = LayoutInflater.from(parent.getContext());
        View view = mLayoutFlater.inflate(R.layout.template_hot, null);

        return new ViewHolder(view);
    }

    public List getDatas() {
        return mDatas;
    }

    //清除数据
    public void clearData(List<Wares> datas) {
        mDatas.clear();
        notifyItemRangeRemoved(0, mDatas.size());
    }

    //添加数据
    public void addData(List<Wares> datas) {
        addData(0, datas);
    }
    public void addData(int position, List<Wares> datas) {
        if(datas != null && datas.size()>0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(position, datas.size());
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wares ware = mDatas.get(position);

        holder.tvPrice.setText("￥" + ware.getPrice());
        holder.tvTitle.setText(ware.getName());
        holder.draweeView.setImageURI(Uri.parse(ware.getImgUrl()));
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size()>0) {
            return mDatas.size();
        }

        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView draweeView;
        TextView tvTitle;
        TextView tvPrice;
        Button btBuy;

        public ViewHolder(View itemView) {
            super(itemView);
            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        }
    }

}
