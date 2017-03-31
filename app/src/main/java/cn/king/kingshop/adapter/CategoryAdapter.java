package cn.king.kingshop.adapter;

import android.content.Context;

import java.util.List;

import cn.king.kingshop.BaseViewHolder;
import cn.king.kingshop.R;
import cn.king.kingshop.bean.Category;

/**
 * Created by king on 2017/2/5.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    public void convert(BaseViewHolder viewHolder, Category item) {
        viewHolder.getTextView(R.id.text).setText(item.getName());
    }

}
