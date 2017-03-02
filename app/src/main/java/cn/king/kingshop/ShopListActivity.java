package cn.king.kingshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by king on 2017/2/28.
 */

public class ShopListActivity extends AppCompatActivity {

    @ViewInject(R.id.tab_indicator)
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        ViewUtils.inject(this);

        initTab();
    }

    private void initTab() {
        TabLayout.Tab tabDefault = mTabLayout.newTab();
        tabDefault.setText(R.string.defaultval);
        mTabLayout.addTab(tabDefault);

        TabLayout.Tab tabSale = mTabLayout.newTab();
        tabSale.setText(R.string.sales);
        mTabLayout.addTab(tabSale);

        TabLayout.Tab tabPrice = mTabLayout.newTab();
        tabPrice.setText(R.string.price);
        mTabLayout.addTab(tabPrice);

    }
}
