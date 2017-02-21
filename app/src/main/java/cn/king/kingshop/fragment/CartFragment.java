package cn.king.kingshop.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.king.kingshop.R;
import cn.king.kingshop.adapter.CartAdapter;
import cn.king.kingshop.adapter.decoration.DividerItemDecoration;
import cn.king.kingshop.bean.ShoppingCart;
import cn.king.kingshop.utils.CartProvide;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CartFragment extends BaseFragment {

    private CartAdapter mCartAdapter;
    private List<ShoppingCart> list;
    private CartProvide mCartProvide;

    @ViewInject(R.id.recyclerview_cart)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;
    @ViewInject(R.id.text_total)
    private TextView mTextView;


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void init() {

        mCartProvide = new CartProvide(getContext());
        list = mCartProvide.getAll();
        mCartAdapter = new CartAdapter(getContext(), list, mCheckBox, mTextView);
        showData();
    }

    private void showData() {
        mRecyclerView.setAdapter(mCartAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        mCartAdapter.clear();
        List<ShoppingCart> datas = mCartProvide.getAll();
        mCartAdapter.addData(datas);
    }

}
