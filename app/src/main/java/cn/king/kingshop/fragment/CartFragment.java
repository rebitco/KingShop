package cn.king.kingshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import cn.king.kingshop.MainActivity;
import cn.king.kingshop.R;
import cn.king.kingshop.adapter.CartAdapter;
import cn.king.kingshop.adapter.decoration.DividerItemDecoration;
import cn.king.kingshop.bean.ShoppingCart;
import cn.king.kingshop.utils.CartProvide;
import cn.king.kingshop.widget.MyToolBar;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CartFragment extends BaseFragment implements View.OnClickListener {

    private CartAdapter mCartAdapter;
    private List<ShoppingCart> list;
    private CartProvide mCartProvide;

    @ViewInject(R.id.recyclerview_cart)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;
    @ViewInject(R.id.text_total)
    private TextView mTextView;
    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;
    @ViewInject(R.id.button_pay)
    private Button btPay;
    @ViewInject(R.id.button_delete)
    private Button btDel;

    private static final int STATE_EDIT = 1;
    private static final int STATE_COMPLETE = 2;


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
        changeToolbar();
    }

    private void changeToolbar() {
        mToolBar.setRightButtonOnClickListener(this);
        mToolBar.getRightButton().setTag(STATE_EDIT);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        //添加右边按钮
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
        mCartAdapter.showTotalPrice();//这个一定要调用,添加到购物车后,切换到购物车的合计才会刷新,因为切换Tab只调用refreshData(),不会调用CartFragment的init();
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if(STATE_EDIT == tag) {
            //编辑状态
            mToolBar.getRightButton().setText(R.string.complete);
            mToolBar.getRightButton().setTag(STATE_COMPLETE);
            mCheckBox.setChecked(false);
            mTextView.setVisibility(View.GONE);
            btPay.setVisibility(View.GONE);
            btDel.setVisibility(View.VISIBLE);
            mCartAdapter.checkAllChecked(false);//列表选中的取反

        } else if(STATE_COMPLETE == tag) {
            //完成状态
            mToolBar.getRightButton().setText(R.string.editor);
            mToolBar.getRightButton().setTag(STATE_EDIT);
            mCheckBox.setChecked(true);
            mTextView.setVisibility(View.VISIBLE);
            btPay.setVisibility(View.VISIBLE);
            btDel.setVisibility(View.GONE);
            mCartAdapter.checkAllChecked(true);//列表选中的取反
            mCartAdapter.showTotalPrice();
        }
    }

    @OnClick(R.id.button_delete)
    private void deleteItem(View v) {
        mCartAdapter.deleteItemData();
    }
}
