package cn.king.kingshop.adapter;



import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.king.kingshop.BaseViewHolder;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.Wares;

/**
 * Created by king on 2017/2/6.
 */

public class WaresAdapter extends SimpleAdapter<Wares> {

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
    }

    @Override
    public void convert(BaseViewHolder viewHolder, Wares item) {
        SimpleDraweeView sdv = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        sdv.setImageURI(Uri.parse(item.getImgUrl()));//商品图片

        viewHolder.getTextView(R.id.ware_title).setText(item.getName());//商品标题
        viewHolder.getTextView(R.id.ware_price).setText("￥" + item.getPrice());//商品价格
    }
}
