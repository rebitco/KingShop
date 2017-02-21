package cn.king.kingshop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.king.kingshop.bean.Tab;
import cn.king.kingshop.fragment.CartFragment;
import cn.king.kingshop.fragment.CatagoryFragment;
import cn.king.kingshop.fragment.HomeFragment;
import cn.king.kingshop.fragment.HotFragment;
import cn.king.kingshop.fragment.UserFragment;
import cn.king.kingshop.widget.FragmentTabHost;

public class MainActivity extends AppCompatActivity {

    private ImageView ivTabIcon;
    private TextView tvTabText;

    private FragmentTabHost mTabHost;
    private List<Tab> mTabs = new ArrayList<>(5);

    private CartFragment cartFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }

    public void initTab() {
        Tab tabHome = new Tab(R.string.home, R.drawable.selector_tab_home, HomeFragment.class);
        Tab tabHot = new Tab(R.string.hot, R.drawable.selector_tab_hot, HotFragment.class);
        Tab tabCatagory = new Tab(R.string.catagory, R.drawable.selector_tab_category, CatagoryFragment.class);
        Tab tabCart = new Tab(R.string.cart, R.drawable.selector_tab_cart, CartFragment.class);
        Tab tabUser = new Tab(R.string.mine, R.drawable.selector_tab_user, UserFragment.class);

        mTabs.add(tabHome);
        mTabs.add(tabHot);
        mTabs.add(tabCatagory);
        mTabs.add(tabCart);
        mTabs.add(tabUser);

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.setCurrentTab(0);
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);//去掉icon间隔的分割线 , 由父控件去设置

        for (Tab tab: mTabs) {
            mTabHost.addTab(mTabHost.newTabSpec(getString(tab.getTabText())).setIndicator(getTabView(tab)),
                    tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)) {
                    //刷新购物车信息
                    refreshCartData();
                }
            }
        });
    }

    private void refreshCartData() {
        if (cartFragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cartFragment = (CartFragment) fragment;
                cartFragment.refreshData();
            }
        } else {
            cartFragment.refreshData();
        }
    }

    public View getTabView(Tab tab) {
        View view = View.inflate(this, R.layout.tab_indicator, null);
        ivTabIcon = (ImageView) view.findViewById(R.id.icon_tab);
        tvTabText = (TextView) view.findViewById(R.id.txt_indicator);
        ivTabIcon.setImageResource(tab.getTabIcon());
        tvTabText.setText(tab.getTabText());

        return view;
    }

}
