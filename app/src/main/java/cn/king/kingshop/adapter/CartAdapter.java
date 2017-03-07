package cn.king.kingshop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import cn.king.kingshop.BaseViewHolder;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.ShoppingCart;
import cn.king.kingshop.utils.CartProvide;
import cn.king.kingshop.widget.NumAddSubView;

/**
 * Created by king on 2017/2/18.
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {
    private CheckBox checkBox;
    private TextView textView;
    private CartProvide mProvide;


    public CartAdapter(Context context, List<ShoppingCart> carts, CheckBox cb, TextView tv) {
        super(context, R.layout.template_cart, carts);

        setCheckBox(cb);
        setTextView(tv);

        mProvide = CartProvide.getCartProvide();

        setOnItemClickListener(this);

        showTotalPrice();
    }

    @Override
    public void convert(BaseViewHolder viewHolder, final ShoppingCart cart) {
//        viewHolder.getCheckBox(R.id.checkbox).setChecked(cart.isChecked());
        CheckBox checkBox = (CheckBox) viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(cart.isChecked());

        SimpleDraweeView sdv = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        sdv.setImageURI(cart.getImgUrl());

        viewHolder.getTextView(R.id.text_title).setText(cart.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥" + cart.getPrice());

        NumAddSubView numAddSub = (NumAddSubView) viewHolder.getView(R.id.num_control);
        numAddSub.setValue(cart.getCount());
        numAddSub.setMaxVal(100);

        numAddSub.setOnButtonClickListener(new NumAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View v, int value) {
                cart.setCount(value);
                mProvide.update(cart);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClick(View v, int value) {
                cart.setCount(value);
                mProvide.update(cart);
                showTotalPrice();
            }
        });
    }

    private float getTotalPrice() {
        float sum = 0;
        if(mDatas != null && mDatas.size() > 0) {
            for(ShoppingCart cart : mDatas) {
                if(checkBox.isChecked()) {
                    sum += cart.getCount()*cart.getPrice();
                }
            }
        }

        return sum;

    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        ShoppingCart item = getItem(position);
        item.setIsChecked(!item.isChecked());
        notifyItemChanged(position);//更新数据

        checkQueue();
        showTotalPrice();
    }

    private void checkQueue() {
        int queueCount = 0;
        int checkedNum = 0;
        if(mDatas != null) {
            queueCount = mDatas.size();
            for(ShoppingCart cart : mDatas) {
                if(!cart.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkedNum ++;
                }
            }

            if(queueCount == checkedNum) {
                checkBox.setChecked(true);
            }
        }
    }

    public void checkAllChecked(boolean isCkecked) {
        if(mDatas != null && mDatas.size() > 0) {

            int i = 0;
            for(ShoppingCart cart : mDatas) {
                cart.setIsChecked(isCkecked);
                notifyItemChanged(i);
                i ++;
            }
        }
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setCheckBox(CheckBox cb) {
        this.checkBox = cb;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全选 全不选
                checkAllChecked(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }

    public void deleteItemData() {
//        if (mDatas != null && mDatas.size() > 0) {
//            for(ShoppingCart cart : mDatas) {
//                if(cart.isChecked()) {
//                    int position = mDatas.indexOf(cart);
//                    mProvide.delete(cart);
//                    mDatas.remove(cart);
//                    notifyItemRemoved(position);
//                }
//            }
//        }

        if(mDatas.size() > 0 && mDatas != null) {
            for(Iterator iterator = mDatas.iterator(); iterator.hasNext();) {
                ShoppingCart cart = (ShoppingCart) iterator.next();
                if(cart.isChecked()) {
                    int position = mDatas.indexOf(cart);
                    mProvide.delete(cart);
                    iterator.remove();
                    notifyItemRemoved(position);
                }
            }
        }

    }
}
