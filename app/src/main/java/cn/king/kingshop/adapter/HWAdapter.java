package cn.king.kingshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.king.kingshop.BaseViewHolder;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.ShoppingCart;
import cn.king.kingshop.bean.Wares;
import cn.king.kingshop.utils.CartProvide;

/**
 * Created by king on 2017/2/18.
 */

public class HWAdapter extends SimpleAdapter<Wares> {
    private CartProvide mProvide;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot, datas);
        mProvide = new CartProvide(context);
    }

    @Override
    public void convert(BaseViewHolder viewHolder, final Wares ware) {
        SimpleDraweeView sdv = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        sdv.setImageURI(Uri.parse(ware.getImgUrl()));

        viewHolder.getTextView(R.id.tv_title).setText(ware.getName());
        viewHolder.getTextView(R.id.tv_price).setText("￥" + ware.getPrice());

        //加购物车
        viewHolder.getButton(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProvide.put(convertData(ware));

                Toast.makeText(mContext, R.string.addCart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ShoppingCart convertData(Wares ware) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(ware.getId());
        cart.setName(ware.getName());
        cart.setPrice(ware.getPrice());
        cart.setDescription(ware.getDescription());
        cart.setImgUrl(ware.getImgUrl());

        return cart;
    }
}
