package cn.king.kingshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import cn.king.kingshop.adapter.HWAdapter;
import cn.king.kingshop.adapter.decoration.DividerItemDecoration;
import cn.king.kingshop.bean.Page;
import cn.king.kingshop.bean.Wares;
import cn.king.kingshop.utils.Pager;
import cn.king.kingshop.widget.MyToolBar;

/**
 * Created by king on 2017/2/28.
 */

public class ShopListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener, View.OnClickListener {

    @ViewInject(R.id.tab_indicator)
    private TabLayout mTabLayout;
    @ViewInject(R.id.toolbar)
    private MyToolBar mToolBar;
    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;
    @ViewInject(R.id.recyclerview_warelist)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.txt_summary)
    private TextView tvSummary;

    private long campaignId;
    private int orderBy = 0;
    private static final int TAG_DEFAULT = 0;
    private static final int TAG_SALES = 1;
    private static final int TAG_PRICE = 2;

    private static final int LIST_SINGLE = 0;
    private static final int LIST_GRID = 1;

    private HWAdapter mAdapter;
    private Pager pager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);
        ViewUtils.inject(this);

        campaignId = getIntent().getLongExtra(Contants.CAMPAIGN_ID, 0);

        initToolBar();
        initTab();
        initData();
    }

    private void initToolBar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopListActivity.this.finish();
            }
        });

        mToolBar.setRightButtonIcon(R.mipmap.icon_grid_32);
        mToolBar.setTag(LIST_SINGLE);
        mToolBar.setRightButtonOnClickListener(this);
    }

    private void initTab() {
        TabLayout.Tab tabDefault = mTabLayout.newTab();
        tabDefault.setText(R.string.defaultval);
        tabDefault.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tabDefault);

        TabLayout.Tab tabSale = mTabLayout.newTab();
        tabSale.setText(R.string.sales);
        tabSale.setTag(TAG_SALES);
        mTabLayout.addTab(tabSale);

        TabLayout.Tab tabPrice = mTabLayout.newTab();
        tabPrice.setText(R.string.price);
        tabPrice.setTag(TAG_PRICE);
        mTabLayout.addTab(tabPrice);

        mTabLayout.addOnTabSelectedListener(this);//tab切换监听事件

    }

    private void initData() {
        pager = Pager.newBuild().setUrl(Contants.API.WARE_CAMPAIGN_LIST)
                                        .putParam("campaignId", campaignId)
                                        .putParam("orderBy", orderBy)
                                        .setOnPageListener(this)
                                        .setLoadMore(true)
                                        .setRefreshLayout(mRefreshLayout)
                                        .build(this, new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
    }

    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        tvSummary.setText("共有" + totalCount + "款商品");

        if(mAdapter == null) {
            mAdapter = new HWAdapter(this, datas);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mAdapter.refreshData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if (tag == LIST_SINGLE) {
            mToolBar.getRightButton().setTag(LIST_SINGLE);
            mToolBar.setRightButtonIcon(R.mipmap.icon_grid_32);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.resetLayout(R.layout.template_hot);

        } else if(tag == LIST_GRID) {
            mToolBar.getRightButton().setTag(LIST_GRID);
            mToolBar.setRightButtonIcon(R.mipmap.icon_list_32);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            mAdapter.resetLayout(R.layout.template_grid_wares);
        }
    }
}
