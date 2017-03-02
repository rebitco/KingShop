package cn.king.kingshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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

public class ShopListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares> {

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

    private HWAdapter mAdapter;

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
                finish();
            }
        });
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

    private void initData() {
        Pager pager = Pager.newBuild().setUrl(Contants.API.WARE_CAMPAIGN_LIST)
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

    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {

    }
}
